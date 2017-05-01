package krypt.com.krypt.video;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import io.realm.Realm;
import krypt.com.krypt.utils.PrimaryKeyFactory;

/**
 * Created by hackean on 4/30/17.
 */

public class VideoEncryptionHandler {

    private static VideoEncryptionHandler instance;
    private List<EncryptionHandler> encryptionObservers = new ArrayList<>();

    public static VideoEncryptionHandler newInstance() {
        if (instance == null) {
            instance = new VideoEncryptionHandler();
        }
        return instance;
    }

    private VideoEncryptionHandler() {

    }

    public void register(EncryptionHandler encryptionHandler) {
        this.encryptionObservers.add(encryptionHandler);
    }

    public List<EncryptedVideo> encypt(Set<Video> videos) throws IOException, VideoEncryptionException {
        String directory = getKryptifiedDirectory();
        List<EncryptedVideo> encryptedVideos = new ArrayList<>();
        for (Video v : videos) {
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
            EncryptedVideo enc = new EncryptedVideo();
            enc.setId(PrimaryKeyFactory.getInstance().nextKey(EncryptedVideo.class));
            enc.setOriginalPath(v.getPath());
            realm.copyToRealm(enc);


            File outputFile = new File(directory
                    .concat("/")
                    .concat(String.valueOf(enc.getId()))
                    .concat(".enc"));
            File inputFile = new File(v.getPath());

            FileOutputStream fos = new FileOutputStream(outputFile);
            FileInputStream fis = new FileInputStream(inputFile);

            try {
                encrypt(fis, fos);
            } catch (Exception e){
                realm.close();
                throw new VideoEncryptionException(e);
            } finally {
                fis.close();
                fos.close();
            }

            realm.commitTransaction();
            encryptedVideos.add(enc);
            for (EncryptionHandler e : this.encryptionObservers) {
                e.onVideoEncrypted(enc);
            }
            // TODO Delete file from user path
        }
        return encryptedVideos;
    }

    public void decrypt(EncryptedVideo encryptedVideo) throws IOException {
        String encryptedFile = getKryptifiedDirectory() + "/" + encryptedVideo.getId() + ".enc";
        FileInputStream fis = new FileInputStream(encryptedFile);
        FileOutputStream fos = new FileOutputStream(encryptedVideo.getOriginalPath());
        VideoEncryptionHandler.newInstance().decrypt(fis, fos);
        Video video = new Video(encryptedVideo.getOriginalPath(), null);
        for (EncryptionHandler e : this.encryptionObservers) {
            e.onVideoDecrypted(video);
        }
    }

    private void encrypt(FileInputStream source, FileOutputStream destination) throws Exception {
        byte[] k = "iqwfbjcfbbvcbmvb".getBytes();
        SecretKeySpec key = new SecretKeySpec(k, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        CipherOutputStream cos = new CipherOutputStream(destination, cipher);
        byte[] buf = new byte[1024];
        int read;
        while ((read = source.read(buf)) != -1) {
            cos.write(buf, 0, read);
        }
        source.close();
        destination.flush();
        cos.close();
        destination.close();
    }

    private void decrypt(FileInputStream source, FileOutputStream destination) {

    }

    public String getKryptifiedDirectory() throws IOException {
        if (isExternalStorageWritable()) {

            File file;

            if (Build.VERSION.SDK_INT >= 19) {
                file = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                        .getAbsoluteFile() + "/kryptified");
            } else {
                file = new File(Environment
                        .getExternalStorageDirectory()
                        .getAbsoluteFile() + "/Documents" + "/kryptified");
            }

            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new IOException("Couldn't create file encryption directory");
                }
            }
            return file.getAbsolutePath();
        } else {
            throw new IOException("External Storage not found");
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public interface EncryptionHandler {
        void onVideoEncrypted(EncryptedVideo encryptedVideo);

        void onVideoDecrypted(Video video);
    }
}

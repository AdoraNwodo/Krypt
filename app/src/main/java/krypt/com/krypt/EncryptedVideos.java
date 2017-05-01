package krypt.com.krypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import krypt.com.krypt.utils.MessageToast;
import krypt.com.krypt.video.EncryptedVideo;
import krypt.com.krypt.video.EncryptedVideoViewAdapter;
import krypt.com.krypt.video.Video;
import krypt.com.krypt.video.VideoEncryptionException;
import krypt.com.krypt.video.VideoEncryptionHandler;
import krypt.com.krypt.video.VideoEvent;

public class EncryptedVideos extends Fragment implements VideoEvent.EncryptedVideoActionListener, VideoEncryptionHandler.EncryptionHandler{

    @BindView(R.id.encrypted_videos)
    RecyclerView encryptedVideosRecyclerView;

    EncryptedVideoViewAdapter adapter;

    VideoEncryptionHandler handler;

    EncryptedVideo currentlyEncryptedVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = VideoEncryptionHandler.newInstance();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.encrypted_videos, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.register(this);

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<EncryptedVideo> query = realm.where(EncryptedVideo.class);
        List<EncryptedVideo> encryptedVideoList = query.findAll();
        adapter = new EncryptedVideoViewAdapter(getContext(), encryptedVideoList, this);

        encryptedVideosRecyclerView.setAdapter(adapter);
        encryptedVideosRecyclerView.setHasFixedSize(true);
        encryptedVideosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPlayClicked(EncryptedVideo encryptedVideo) {
        VideoEncryptionHandler handler = VideoEncryptionHandler.newInstance();
        try {
            String kryptDirectory = handler.getKryptifiedDirectory();
            File currentFile = getContext().getDir("temp", Context.MODE_PRIVATE);

            String path = currentFile.getAbsolutePath();

            if (path.endsWith("/")){
                path = path.concat("temp.run");
            } else {
                path = path.concat("/temp.run");
            }

            FileInputStream source = new FileInputStream(kryptDirectory + "/" + encryptedVideo.getId() + ".enc");
            FileOutputStream destination =  new FileOutputStream(new File(path));

            handler.decrypt(source, destination);
            destination.flush();
            destination.close();

            Intent i = new Intent(getContext(), VideoPlayerActivity.class);
            i.putExtra("path", path);
            startActivity(i);
        } catch (Exception e){
            MessageToast.showSnackBar(getContext(), e.getMessage());
        }
    }

    @Override
    public void onDecryptClicked(EncryptedVideo encryptedVideo) {
        try {
            handler.decrypt(encryptedVideo);
        } catch (IOException e){
            MessageToast.showSnackBar(getContext(), e.getMessage());
        } catch (VideoEncryptionException e){
            MessageToast.showSnackBar(getContext(), "Error occurred from encryption library");
            e.printStackTrace();
        }
    }

    @Override
    public void onVideoEncrypted(EncryptedVideo encryptedVideo) {
        currentlyEncryptedVideo = encryptedVideo;
        adapter.addEncryptedVideo(encryptedVideo);
    }

    @Override
    public void onVideoDecrypted(Video video) {
        MessageToast.showSnackBar(getContext(), "Video was decrypted successfully");
    }
}

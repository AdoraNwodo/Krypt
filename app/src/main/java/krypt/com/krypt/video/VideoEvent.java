package krypt.com.krypt.video;

/**
 * Created by hackean on 4/29/17.
 */

public class VideoEvent {

    public interface VideoActionListener {

        void onVideoClicked(Video video);
        void onVideoSelected(Video video);
        void onVideoDeselected(Video video);

    }

    public interface EncryptedVideoActionListener {
        void onPlayClicked(EncryptedVideo encryptedVideo);
        void onDecryptClicked(EncryptedVideo encryptedVideo);
    }
}

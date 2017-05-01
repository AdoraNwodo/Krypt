package krypt.com.krypt.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krypt.com.krypt.R;

import static krypt.com.krypt.video.VideoEvent.*;

/**
 * Created by hackean on 4/30/17.
 */

public class EncryptedVideoViewAdapter extends RecyclerView.Adapter<EncryptedVideoViewAdapter.EncryptedVideoViewHolder> {

    private Context context;
    private EncryptedVideoActionListener encryptedVideoActionListener;
    private List<EncryptedVideo> encryptedVideos;


    public EncryptedVideoViewAdapter(Context context, List<EncryptedVideo> encryptedVideos, EncryptedVideoActionListener encryptedVideoActionListener) {
        this.context = context;
        this.encryptedVideoActionListener = encryptedVideoActionListener;
        this.encryptedVideos = new ArrayList<>(encryptedVideos);
    }

    @Override
    public EncryptedVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.single_encrypted_video_row, parent, false);
        return new EncryptedVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EncryptedVideoViewHolder holder, int position) {
        EncryptedVideo currentVideo = encryptedVideos.get(position);
        String videoName = currentVideo.getOriginalPath();
        holder.videoName.setText(videoName.substring(videoName.lastIndexOf("/") + 1));
    }

    @Override
    public int getItemCount() {
        return encryptedVideos.size();
    }

    public synchronized void addEncryptedVideo(EncryptedVideo encryptedVideo) {
        int pos = encryptedVideos.size();
        encryptedVideos.add(encryptedVideo);
        notifyItemInserted(pos);
    }

    public class EncryptedVideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_name)
        TextView videoName;

        @BindView(R.id.play_video)
        ImageButton playVideo;

        @BindView(R.id.decrypt_video)
        ImageButton decryptVideo;

        public EncryptedVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.decrypt_video)
        public void decryptClicked() {
            int pos = getAdapterPosition();
            encryptedVideoActionListener.onDecryptClicked(encryptedVideos.get(pos));
            encryptedVideos.remove(pos);
        }


    }
}

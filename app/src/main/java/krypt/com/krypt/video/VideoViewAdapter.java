package krypt.com.krypt.video;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import krypt.com.krypt.R;

/**
 * Created by hackean on 4/28/17.
 */

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.VideoViewHolder>{

    private List<Video>videos;
    private Context context;
    private OnClickVideoListener onClickVideoListener;

    public VideoViewAdapter(Context context, OnClickVideoListener onClickVideoListener, List<Video> videos) {
        this.videos = videos;
        this.context = context;
        this.onClickVideoListener = onClickVideoListener;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.single_video_view, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {

        File file = new File(videos.get(position).getPath());

        holder.videoName.setText(file.getName());
        Glide.with(context)
                .load(Uri.fromFile(file))
                .into(holder.videoThumb);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.video_thumb)
        ImageView videoThumb;

        @BindView(R.id.video_name)
        TextView videoName;

        VideoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }


        @Override
        public void onClick(View v) {
            VideoViewAdapter.this.onClickVideoListener.play(videos.get(getAdapterPosition()));
        }
    }

}

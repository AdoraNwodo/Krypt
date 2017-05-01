package krypt.com.krypt.video;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import krypt.com.krypt.R;

import static krypt.com.krypt.video.VideoEvent.*;
/**
 * Created by hackean on 4/28/17.
 */

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.VideoViewHolder>{

    private List<Video>videos;
    private Context context;
    private VideoActionListener videoActionListener;
    private Set<Integer> selectedViews = new TreeSet<>();

    public VideoViewAdapter(Context context, VideoActionListener videoActionListener, List<Video> videos) {
        this.videos = videos;
        this.context = context;
        this.videoActionListener = videoActionListener;
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
        if (selectedViews.contains(position)) {
            holder.layout.setBackgroundColor(Color.BLACK);
            holder.videoName.setBackgroundColor(Color.WHITE);
            holder.videoName.setTextColor(Color.BLACK);
            holder.setIsRecyclable(false);
        }
        Glide.with(context)
                .load(Uri.fromFile(file))
                .into(holder.videoThumb);
    }

    public void addVideo(Video video) {
        int pos = videos.size();
        videos.add(video);
        notifyItemInserted(pos);
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.video_thumb)
        ImageView videoThumb;

        @BindView(R.id.video_name)
        TextView videoName;

        @BindView(R.id.single_video_layout)
        RelativeLayout layout;

        VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            if (selectedViews.contains(position)){
                layout.setBackgroundColor(Color.WHITE);
                TextView videoName = (TextView)layout.getChildAt(1);
                videoName.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                videoName.setTextColor(Color.WHITE);
                selectedViews.remove(position);
                this.setIsRecyclable(true);
                videoActionListener.onVideoDeselected(videos.get(position));
            } else {
                videoActionListener.onVideoClicked(videos.get(position));
            }
        }

        @Override
        public boolean onLongClick(View v) {

            int position= getAdapterPosition();

            if (!selectedViews.contains(position)) {
                this.layout.setBackgroundColor(Color.BLACK);
                this.videoName.setBackgroundColor(Color.WHITE);
                this.videoName.setTextColor(Color.BLACK);
                selectedViews.add(position);
                this.setIsRecyclable(false);
            }

            videoActionListener.onVideoSelected(videos.get(position));
            return true;
        }
    }
}

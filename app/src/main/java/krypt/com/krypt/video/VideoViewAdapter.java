package krypt.com.krypt.video;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

    public VideoViewAdapter(Context context, List<Video> videos) {
        this.videos = videos;
        this.context = context;
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


    class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_thumb)
        ImageView videoThumb;

        @BindView(R.id.video_name)
        TextView videoName;

        VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

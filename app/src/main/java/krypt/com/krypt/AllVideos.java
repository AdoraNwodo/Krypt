package krypt.com.krypt;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import krypt.com.krypt.video.Video;
import krypt.com.krypt.video.VideoViewAdapter;

public class AllVideos extends Fragment {

    @BindView(R.id.videos)
    RecyclerView videosView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.all_videos, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        List<Video> videos = getPublicVideos();
        VideoViewAdapter videoViewAdapter = new VideoViewAdapter(getContext(), videos);

        videosView.setAdapter(videoViewAdapter);
        videosView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        videosView.setHasFixedSize(true);

    }

    public List<Video> getPublicVideos() {
        List<String> externalVideos = getExternalVideos();
        List<String>internalVideos = getInternalVideos();

        List<Video> videos = new ArrayList<>();

        for (String video : externalVideos) {
            videos.add(new Video(video));
        }

        for(String video: internalVideos){
            videos.add(new Video(video));
        }

        return videos;
    }

    public List<String> getExternalVideos() {
        return load(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    public List<String> getInternalVideos() {
        return load(MediaStore.Video.Media.INTERNAL_CONTENT_URI);
    }

    public List<String> load(Uri uri) {
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor c = getContext().getContentResolver().query(uri, projection, null, null, null);

        List<String> videos = new ArrayList<>();

        if (c != null) {
            while (c.moveToNext()) {
                videos.add(c.getString(0));
            }
            c.close();
        }

        return videos;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (dpWidth / 180);
    }

}

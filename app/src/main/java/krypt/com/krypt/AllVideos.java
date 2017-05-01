package krypt.com.krypt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import krypt.com.krypt.utils.MessageToast;
import krypt.com.krypt.video.EncryptedVideo;
import krypt.com.krypt.video.Video;
import krypt.com.krypt.video.VideoEncryptionException;
import krypt.com.krypt.video.VideoEncryptionHandler;
import krypt.com.krypt.video.VideoEvent;
import krypt.com.krypt.video.VideoViewAdapter;

public class AllVideos extends Fragment implements VideoEvent.VideoActionListener, View.OnClickListener, VideoEncryptionHandler.EncryptionHandler {

    @BindView(R.id.videos)
    RecyclerView videosView;

    Set<Video> selectedVideos = new TreeSet<>();
    Map<String, Video> selectedVideosMap = new HashMap<>();

    Videos mActivity;

    Toolbar toolbar;

    VideoViewAdapter videoViewAdapter;

    VideoEncryptionHandler handler;

    private boolean widgetAdded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        handler = VideoEncryptionHandler.newInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.all_videos, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = (Videos) getActivity();
        this.toolbar = this.mActivity.getToolbar();
    }

    @Override
    public void onStart() {
        super.onStart();

        handler.register(this);
        List<Video> videos = getPublicVideos();
        videoViewAdapter = new VideoViewAdapter(getContext(), this, videos);

        videosView.setAdapter(videoViewAdapter);
        videosView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        videosView.setHasFixedSize(true);

    }

    public List<Video> getPublicVideos() {
        List<String> externalVideos = getExternalVideos();
        List<String> internalVideos = getInternalVideos();

        List<Video> videos = new ArrayList<>();
        int i = 0;
        for (String video : externalVideos) {
            videos.add(new Video(video, i));
            i++;
        }

        for (String video : internalVideos) {
            videos.add(new Video(video, i));
            i++;
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
        return (int) (dpWidth / 180);
    }

    @Override
    public void onVideoClicked(Video video) {
        Intent i = new Intent(getContext(), VideoPlayerActivity.class);
        i.putExtra("path", video.getPath());
        startActivity(i);
    }

    @Override
    public void onVideoSelected(Video video) {
        if (!selectedVideos.contains(video)) {
            selectedVideos.add(video);
        }

        if (!widgetAdded) {
            Button button = new Button(getContext());
            button.setText("Encrypt");
            button.setOnClickListener(this);
            toolbar.addView(button);
            widgetAdded = true;
        }

    }

    @Override
    public void onVideoDeselected(Video video) {
        if (selectedVideos.contains(video)) {
            selectedVideos.remove(video);
        }

        if (widgetAdded) {
            if (selectedVideos.size() == 0) {
                toolbar.removeAllViews();
                widgetAdded = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        final VideoEncryptionHandler handler = VideoEncryptionHandler.newInstance();

        for (Video vid: selectedVideos) {
            selectedVideosMap.put(vid.getPath(), vid);
        }

        try {
            handler.encypt(new TreeSet<>(selectedVideos));
        } catch (IOException e) {
            MessageToast.showSnackBar(getContext(), e.getMessage());
        } catch (VideoEncryptionException e){
            MessageToast.showSnackBar(getContext(), "Error occurred from encryption library");
            e.printStackTrace();
        }
    }

    @Override
    public void onVideoEncrypted(EncryptedVideo encryptedVideo) {
        Video vid = selectedVideosMap.get(encryptedVideo.getOriginalPath());
        selectedVideos.remove(vid);
        if (selectedVideos.size() < 1) {
            videoViewAdapter.setVideos(new ArrayList<Video>());
            File videoFile = new File(vid.getPath());
            if (!videoFile.delete()) {
                MessageToast.showSnackBar(getContext(), "Couldn't delete " + videoFile.getAbsoluteFile());
            }
            videoViewAdapter.setVideos(getPublicVideos());
        }

        MessageToast.showSnackBar(getContext(), "Video was encrypted successfully");
    }

    @Override
    public void onVideoDecrypted(Video video) {
        video.setSerialNumber(selectedVideos.size());
        videoViewAdapter.addVideo(video);
    }
}
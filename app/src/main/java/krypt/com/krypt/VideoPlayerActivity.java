package krypt.com.krypt;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerActivity extends AppCompatActivity {

    @BindView(R.id.player)
    EasyVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);


        String path = getIntent().getStringExtra("path");
        Uri uri = Uri.fromFile(new File(path));
        player.setSource(uri);
        player.start();
    }
}

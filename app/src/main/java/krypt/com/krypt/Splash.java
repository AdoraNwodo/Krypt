package krypt.com.krypt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends FragmentActivity {

    private static final String USERNAME = "username";
    private static final int SLEEP_TIME = 5000;

//    @BindView(R.id.image_splash)
//    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.launch_info), MODE_PRIVATE);

        String username = preferences.getString(USERNAME, null);

        if (username!= null){
            this.startActivity(LoginActivity.class);
        } else {
             this.startActivity(SignUp.class);
        }
    }

    private void startActivity(final Class<? extends AppCompatActivity> activity) {
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                    Intent i = new Intent(Splash.this, activity);
                    startActivity(i);
                    finish();
                } catch (Exception ignored) {

                }
            }
        };
        background.start();
    }
}

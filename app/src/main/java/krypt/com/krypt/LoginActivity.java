package krypt.com.krypt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krypt.com.krypt.utils.MessageToast;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        String password = this.password.getText().toString();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.launch_info), MODE_PRIVATE);
        String pin = preferences.getString("pin", "");

        if (password.compareTo(pin) == 0){
            MessageToast.showSnackBar(this, "Login successful");

            Intent intent = new Intent(this, Videos.class);
            startActivity(intent);
            finish();

        } else {
            MessageToast.showSnackBar(this, "Invalid pin");
        }
    }

}

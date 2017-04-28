package krypt.com.krypt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krypt.com.krypt.utils.MessageToast;

public class SignUp extends AppCompatActivity {


    @BindView(R.id.username) EditText edtUsername;
    @BindView(R.id.pin) EditText edtPIN;
    @BindView(R.id.confirm_pin) EditText edtConfirmPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSignUp)
    public void onSignupClicked() {
        String username = edtUsername.getText().toString();
        String pin = edtPIN.getText().toString();
        String confirmPIN = edtConfirmPIN.getText().toString();

        if (!username.isEmpty() && !pin.isEmpty() && !confirmPIN.isEmpty()){

            if (pin.compareTo(confirmPIN) == 0){
                SharedPreferences preferences = getSharedPreferences(getString(R.string.launch_info), MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("username", username);
                editor.putString("pin", pin);
                editor.apply();

                MessageToast.showSnackBar(this, "Account setup was successful");
                Intent i = new Intent(SignUp.this, Videos.class);
                startActivity(i);
                finish();
            } else {
                MessageToast.showSnackBar(this, "PINS must match");
            }
        } else {
            MessageToast.showSnackBar(this, getString(R.string.signup_field_condition_text));
        }

    }



}

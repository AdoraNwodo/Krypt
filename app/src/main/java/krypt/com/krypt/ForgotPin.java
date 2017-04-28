package krypt.com.krypt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ForgotPin extends AppCompatActivity {

    Button login, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pin);

        login = (Button)findViewById(R.id.btnLogin);
        reset = (Button) findViewById(R.id.btnReset);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPin.this,LoginActivity.class);
                startActivity(i);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"You pressed reset", Toast.LENGTH_SHORT).show();
            }
        });


    }

}

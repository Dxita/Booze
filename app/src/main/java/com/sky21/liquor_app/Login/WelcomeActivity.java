package com.sky21.liquor_app.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sky21.liquor_app.Home.MainActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;

public class WelcomeActivity extends AppCompatActivity {
    Button signin,signup;
    ImageView backspace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        backspace=findViewById(R.id.backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(signin);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(signup);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedHelper.getKey(WelcomeActivity.this,"loggedin").equals("true")){
            Bundle bundle = new Bundle();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            if (bundle != null){
                intent.putExtras(bundle);
            }
            startActivity(intent);
        } else {
            return;
        }
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}

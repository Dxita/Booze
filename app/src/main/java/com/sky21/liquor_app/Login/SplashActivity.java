package com.sky21.liquor_app.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sky21.liquor_app.Home.MainActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;
public class SplashActivity extends AppCompatActivity implements Runnable {
    ImageView imageView;
    Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

     /*   Handler handler=new Handler();
        handler.postDelayed(this,3000);
        */
        imageView=(ImageView)findViewById(R.id.imageview);

        // Declare an imageView to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        anim.setDuration(900);// Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(SharedHelper.getKey(SplashActivity.this,"loggedin").equals("true")){
                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent1 = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent1);
                    finish();
                }
                //  run();
                // startActivity(new Intent(this,HomeActivity.class));
                // HomeActivity.class is the activity to go after showing the splash screen.
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(anim);


    }

    @Override
    public void run() {

        if(SharedHelper.getKey(SplashActivity.this,"loggedin").equals("true")){
            Intent intent=new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent1 = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(intent1);
            finish();
        }
    }
}
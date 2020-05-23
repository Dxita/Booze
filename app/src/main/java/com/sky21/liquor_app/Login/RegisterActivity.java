package com.sky21.liquor_app.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.sky21.liquor_app.CountryData;
import com.sky21.liquor_app.Home.MainActivity;
import com.sky21.liquor_app.OTPActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ImageView backspace;
    Button signup;
    ProgressBar progressBar;
    EditText name, mobile, password;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);
        signup = findViewById(R.id.signup);
        backspace = findViewById(R.id.backspace);

         code = "91";
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e = mobile.getText().toString();
                String p = password.getText().toString();
                String n = name.getText().toString();

                if (n.isEmpty()) {
                    name.requestFocus();
                    name.setError("Enter your Full name");

                } else if (e.isEmpty()) {
                    mobile.requestFocus();
                    mobile.setError("Enter your mobile number");
                } else if (p.isEmpty()) {
                    password.requestFocus();
                    password.setError("Enter your password");
                } else {
                    api();
                }


            }
        });


    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url="https://boozeapp.co/Booze-App-Api/api/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        String phonenumber = "+" + code + mobile.getText().toString();
                        SharedHelper.putKey(RegisterActivity.this,"number",phonenumber);

                        Intent signup = new Intent(getApplicationContext(), OTPActivity.class);

                        signup.putExtra("PHONE",phonenumber);
                        startActivity(signup);
                        Toast.makeText(RegisterActivity.this, "Thanks for signing up!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "The mobile or email has already been taken.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name.getText().toString());
                map.put("mobile_no", mobile.getText().toString());
                map.put("password", password.getText().toString());
                return map;
            }
        };

        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}

package com.sky21.liquor_app.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sky21.liquor_app.Home.MainActivity;
import com.sky21.liquor_app.Home.ProductsActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button signin;
    ImageView backspace;
    EditText phone, password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        phone = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);
        signin = findViewById(R.id.signin);
        backspace = findViewById(R.id.backspace);

        phone.setText("8290638499");
        password.setText("1234567");

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = phone.getText().toString();
                String p = password.getText().toString();


                if (m.isEmpty()) {
                    phone.requestFocus();
                    phone.setError("Enter Mobile number");
                } else if (p.isEmpty()) {
                    password.requestFocus();
                    password.setError("Enter password");
                } else {
                    api();
                }
            }
        });

    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://boozeapp.co/Booze-App-Api/api/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        SharedHelper.putKey(LoginActivity.this, "loggedin", "true");
                        JSONObject object = jsonObject.getJSONObject("data");

                        String token;
                        token = object.getString("token");

                        Intent signup = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(signup);
                        Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();

                        String phonenumber = phone.getText().toString();
                        SharedHelper.putKey(LoginActivity.this, "number", phonenumber);
                        SharedHelper.putKey(LoginActivity.this, "token", token);

                    } else {
                        Toast.makeText(LoginActivity.this, "We cant find an account with this credentials.", Toast.LENGTH_SHORT).show();
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
                map.put("mobile_no", phone.getText().toString());
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
}

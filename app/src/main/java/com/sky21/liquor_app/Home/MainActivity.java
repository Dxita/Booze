package com.sky21.liquor_app.Home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sky21.liquor_app.CartActivity;
import com.sky21.liquor_app.GPSTracker;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;
import com.sky21.liquor_app.UserAccount.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView addressTxt;
    ImageView profileIcon, searchView;
    GPSTracker gps;
    double latitude;
    double longitude;
    ImageView profile, search,cart;
    Handler handlers;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
    String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        addressTxt = findViewById(R.id.addressId);
        profile = findViewById(R.id.myprofileId);
        search = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
cart=findViewById(R.id.cart);
        token = SharedHelper.getKey(this,"token");

        gpsmethod();
        handlers = new Handler();
        handlers.postDelayed(new Runnable() {
            public void run() {
                gpsmethod();          // this method will contain your almost-finished HTTP calls
                handlers.postDelayed(this, 4000);
            }
        }, 4000);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        api();

    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://missionlockdown.com/BoozeApp/api/stores";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("response", response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("stores");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", object.getString("id"));
                            params.put("name", object.getString("name"));
                            params.put("lat", object.getString("lat"));
                            params.put("long", object.getString("long"));
                            params.put("address", object.getString("address"));

                            storeList.add(params);

                        }

                        Mainadapter mainadapter = new Mainadapter(getApplicationContext(), storeList);
                        recyclerView.setAdapter(mainadapter);
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void gpsmethod() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {

            gps = new GPSTracker(MainActivity.this);
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                String lat1 = String.valueOf(latitude);
                String lon1 = String.valueOf(longitude);

                SharedHelper.putKey(MainActivity.this, "lat", String.valueOf(latitude));
                SharedHelper.putKey(MainActivity.this, "lon", String.valueOf(longitude));
                //    Toast.makeText(MainActivity.this, ""+latitude+" "+longitude, Toast.LENGTH_SHORT).show();
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    addressTxt.setText(address);
                    SharedHelper.putKey(MainActivity.this, "location", address);
                    Log.d("add", address);
                }

            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }

        }
    }

    private class Mainadapter extends RecyclerView.Adapter<MyViewHolder> {

        Context context;
        ArrayList<HashMap<String, String>> storelist = new ArrayList<>();

        public Mainadapter(Context context, ArrayList<HashMap<String, String>> storeList) {
            this.context = context;
            this.storelist = storeList;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.store_recycler, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final HashMap<String, String> map = storelist.get(position);

            holder.store_name.setText(map.get("name"));
            holder.address.setText(map.get("address"));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                    intent.putExtra("ID", map.get("id"));
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return storeList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView store_name, address;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            store_name = itemView.findViewById(R.id.store_name);
            layout = itemView.findViewById(R.id.layout);
            address = itemView.findViewById(R.id.typeTxt);
        }
    }
}

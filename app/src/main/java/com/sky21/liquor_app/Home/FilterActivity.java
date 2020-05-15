package com.sky21.liquor_app.Home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    TextView backspace, unselect_all_tv;
    TextView category_tv, brand_tv, price_tv;
    RecyclerView recycler_view;
    FilterAdapter adapter;
    ArrayList<HashMap<String, String>> categorylist;
    ArrayList<HashMap<String, String>> brandlist;

    String token = "";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().hide();

        category_tv = findViewById(R.id.category_tv);
        brand_tv = findViewById(R.id.brand_tv);
        price_tv = findViewById(R.id.price_tv);
        // unselect_all_tv=findViewById(R.id.unselect_all_tv);

        category_tv.setBackgroundColor(Color.WHITE);
        brand_tv.setBackgroundColor(Color.GRAY);
        price_tv.setBackgroundColor(Color.GRAY);

        progressBar = findViewById(R.id.progressbar);
        recycler_view = findViewById(R.id.category_rv);



        backspace = findViewById(R.id.addressId);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        token = SharedHelper.getKey(FilterActivity.this, "token");

        category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_tv.setBackgroundColor(Color.WHITE);
                brand_tv.setBackgroundColor(Color.GRAY);
                price_tv.setBackgroundColor(Color.GRAY);
                categoryApiCall();
            }
        });

        brand_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_tv.setBackgroundColor(Color.GRAY);
                brand_tv.setBackgroundColor(Color.WHITE);
                price_tv.setBackgroundColor(Color.GRAY);
                brandsApiCall();
            }
        });

        price_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_tv.setBackgroundColor(Color.GRAY);
                brand_tv.setBackgroundColor(Color.GRAY);
                price_tv.setBackgroundColor(Color.WHITE);
            }
        });

      /*  unselect_all_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryRecyclerView();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        categoryApiCall();
    }

    private void setCategoryRecyclerView(ArrayList<HashMap<String, String>> list) {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FilterAdapter(this, list);
        recycler_view.setAdapter(adapter);
    }

    private void categoryApiCall() {
        progressBar.setVisibility(View.VISIBLE);
        categorylist = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://missionlockdown.com/BoozeApp/api/categories";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("response", response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("categories");
                        for (int j = 0; j < jsonArray.length(); j++) {

                            JSONObject object = jsonArray.getJSONObject(j);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", object.getString("id"));
                            params.put("category", object.getString("category"));
                            params.put("created_at", object.getString("created_at"));
                            params.put("updated_at", object.getString("updated_at"));

                            categorylist.add(params);
                        }
                        setCategoryRecyclerView(categorylist);
                    } else {
                        Toast.makeText(FilterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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

    private void brandsApiCall() {
        progressBar.setVisibility(View.VISIBLE);
        brandlist = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://missionlockdown.com/BoozeApp/api/brands";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("response", response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("brands");
                        for (int j = 0; j < jsonArray.length(); j++) {

                            JSONObject object = jsonArray.getJSONObject(j);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", object.getString("id"));
                            params.put("category", object.getString("brand"));
                            params.put("created_at", object.getString("created_at"));
                            params.put("updated_at", object.getString("updated_at"));

                            brandlist.add(params);
                        }
                        setCategoryRecyclerView(brandlist);
                    } else {
                        Toast.makeText(FilterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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

    public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

        Context context;
        ArrayList<HashMap<String, String>> list;

        public FilterAdapter(Context context, ArrayList<HashMap<String, String>> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.filter_item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HashMap<String, String> item = list.get(position);
            if (item.containsKey("category") == item.containsKey("category")){
                holder.checkbox.setText(item.get("category"));
            } else if (item.containsKey("brand") == item.containsKey("brand")){
                holder.checkbox.setText(item.get("brand"));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkbox;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkbox = itemView.findViewById(R.id.checkbox);
            }
        }
    }
}

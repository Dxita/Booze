package com.sky21.liquor_app.Home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.sky21.liquor_app.models.FilterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    TextView backspace, unselect_all_tv;
    TextView category_tv, brand_tv, price_tv;
    RecyclerView recycler_view;
    FilterAdapter adapter;
    List<FilterModel> categorylist;
    List<FilterModel> brandlist;

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

    private void setCategoryRecyclerView(List<FilterModel> list) {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FilterAdapter(this, list);
        recycler_view.setAdapter(adapter);
        recycler_view.getPreserveFocusAfterLayout();
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

                            categorylist.add(new FilterModel(params.get("category")));

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

                            brandlist.add(new FilterModel(params.get("brand")));
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

    public static class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

        Context context;
        //ArrayList<HashMap<String, String>> list;
        List<FilterModel> list;
        List<FilterModel> checkedlist = new ArrayList<>();

        public FilterAdapter(Context context, List<FilterModel> list) {
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
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final FilterModel item = list.get(position);
            if (item != null){
                //holder.checkbox.setOnCheckedChangeListener(null);
                holder.checkbox.setText(item.getItem_name());
                if (item.isSelected()){
                    holder.checkbox.setChecked(true);
                } else {
                    holder.checkbox.setChecked(false);
                    holder.setItemClickListener(new ViewHolder.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            CheckBox checkBox = (CheckBox)view;
                            if (checkBox.isChecked()){
                                checkedlist.add(list.get(position));
                                item.setSelected(true);
                            } else if (!checkBox.isChecked()){
                                checkedlist.remove(list.get(position));
                                item.setSelected(false);
                            }
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CheckBox checkbox;

            ItemClickListener listener;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.setIsRecyclable(false);
                checkbox = itemView.findViewById(R.id.checkbox);
            }

            public void setItemClickListener(ItemClickListener listener){
                this.listener = listener;
            }

            @Override
            public void onClick(View v) {
                this.listener.onItemClick(v,getLayoutPosition());
            }

            public interface ItemClickListener{
                void onItemClick(View view,int position);
            }
        }
    }
}

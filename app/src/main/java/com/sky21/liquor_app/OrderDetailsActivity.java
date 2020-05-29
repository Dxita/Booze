package com.sky21.liquor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView addressTxt;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManagers;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
    HashMap<String,String>data=new HashMap<>();
    String id,total_cost,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().hide();


        addressTxt=findViewById(R.id.addressId);
        recyclerView=findViewById(R.id.recyclerview);
        progressBar=findViewById(R.id.progressbar);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        token = SharedHelper.getKey(OrderDetailsActivity.this, "token");
        final Intent intent = getIntent();
        id = intent.getStringExtra("IDS");

        layoutManagers = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManagers);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        api();

    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="https://boozeapp.co/Booze-App-Api/api/orders-list?"+"store_id=1"+"&status=3";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("success").equalsIgnoreCase("true"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            // HashMap<String,String>map=new HashMap<>();
                            data.put("order_id", object.getString("order_id"));
                            data.put("total_cost", object.getString("total_cost"));

                            total_cost=data.get("total_cost");

                            JSONArray array=object.getJSONArray("order_products");
                            for (int j=0; j<array.length(); j++)
                            {
                                JSONObject obj=array.getJSONObject(j);
                                HashMap<String,String> param=new HashMap<>();
                                param.put("id",obj.getString("id"));
                                //  param.put("order_id",obj.getString("order_id"));
                                param.put("product_id",obj.getString("product_id"));
                                param.put("quantity",obj.getString("quantity"));
                                param.put("cost",obj.getString("cost"));

                                JSONObject o=obj.getJSONObject("product");
                                param.put("id",o.getString("id"));
                                param.put("name",o.getString("name"));
                                param.put("price",o.getString("price"));
                                param.put("size",o.getString("size"));
                                param.put("image",o.getString("image"));


                                JSONObject jbject1=object.getJSONObject("store");
                                {

                                    param.put("id",jbject1.getString("id"));
                                    param.put("s_name",jbject1.getString("name"));
                                    param.put("address",jbject1.getString("address"));
                                    param.put("license_number",jbject1.getString("license_number"));
                                    param.put("license_valid_till",jbject1.getString("license_valid_till"));
                                    param.put("lat",jbject1.getString("lat"));
                                    param.put("long",jbject1.getString("long"));

                                }
                                storeList.add(param);
                            }
                            Toast.makeText(OrderDetailsActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                            MyAdapter myadapter=new MyAdapter(OrderDetailsActivity.this,storeList);
                            recyclerView.setAdapter(myadapter);

                        }

                    }

                    else
                    {


                        Toast.makeText(OrderDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

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
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private class MyAdapter extends RecyclerView.Adapter<Hold> {

        Context context;

        ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
        public MyAdapter(OrderDetailsActivity context, ArrayList<HashMap<String, String>> storeList) {
            this.context=context;
            this.storeList=storeList ;
        }

        @NonNull
        @Override
        public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.historydetails,null);
            return new Hold(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Hold holder, int position) {
            final HashMap<String,String> param=storeList.get(position);
            holder.name.setText(param.get("name"));
            holder.price.setText(getString(R.string.rupee)+param.get("price"));
            holder.quantity.setText("Product QTY:"+" "+param.get("quantity"));
            Glide.with(context).load(param.get("image")).into(holder.image);




        }


        @Override
        public int getItemCount() {
            return storeList.size();
        }
    }

    private class Hold extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image;
        public Hold(View view) {
            super(view);

            name=view.findViewById(R.id.name);
            price=view.findViewById(R.id.price);
            quantity=view.findViewById(R.id.quantity);
            image=view.findViewById(R.id.productImageId);
        }
    }
}

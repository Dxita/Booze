package com.sky21.liquor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.sky21.liquor_app.Home.FilterActivity;
import com.sky21.liquor_app.Home.ProductsActivity;
import com.sky21.liquor_app.Home.SearchActivity;
import com.sky21.liquor_app.UserAccount.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    TextView addressTxt, filter;
    ImageView profile, search, cart;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManagers;
    ProductsActivity.Mainadapter mainadapter;
    String store_id, token;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();


        addressTxt = findViewById(R.id.addressId);
        profile = findViewById(R.id.myprofileId);
        search = findViewById(R.id.searchView);
        filter = findViewById(R.id.filter);
        cart = findViewById(R.id.cart);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(intent);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        token = SharedHelper.getKey(ListActivity.this, "token");
        final Intent intent = getIntent();
        store_id = intent.getStringExtra("ID");
        layoutManagers = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManagers);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        api();
    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="https://missionlockdown.com/BoozeApp/api/products?store_id="+store_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              /*  if(!(arrayList == null))
                    arrayList.clear();*/
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true"))
                    {


                            JSONArray jsonArray=jsonObject.getJSONArray("products");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject object=jsonArray.getJSONObject(i);

                                HashMap<String,String>map=new HashMap<>();
                                map.put("id", object.getString("id"));
                                SharedHelper.putKey(ListActivity.this,"product_id",object.getString("id"));
                                map.put("name",object.getString("name"));
                                map.put("price",object.getString("price"));
                                map.put("quantity",object.getString("quantity"));
                                map.put("image",object.getString("image"));
                                map.put("brand_id",object.getString("brand_id"));


                                arrayList.add(map);
                            }
                        Mainadapter mainadapter=new Mainadapter(getApplicationContext(),arrayList);
                            recyclerView.setAdapter(mainadapter);
                        }

                 /*       Intent intent=new Intent(getApplicationContext(),ProductsActivity.class);
                        intent.putExtra("data",map);
                        intent.putExtra("CODE",code);
                        startActivity(intent);*/

                    else
                    {
                        Toast.makeText(ListActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private class Mainadapter extends RecyclerView.Adapter<MyHolder> {
        Context context;

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        public Mainadapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
            this.context=context;
            this.arrayList=arrayList ;
        }

        @NonNull

        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.list,null);
            return new MyHolder(view);        }

        @Override
        public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

            final HashMap<String,String> map=arrayList.get(position);

            holder.name.setText(map.get("name"));
            holder.price.setText(getString(R.string.rupee)+map.get("price"));

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = (Integer.parseInt(holder.quantity.getText().toString()));
                    quantity = quantity + 1;
                    holder.quantity.setText(String.valueOf(quantity));
                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(holder.quantity.getText().toString())>0)
                    {
                        int quantity = (Integer.parseInt(holder.quantity.getText().toString()));
                        quantity = quantity - 1;
                        holder.quantity.setText(String.valueOf(quantity));
                    }
                    else
                    {}
                }
            });


            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int a = Integer.parseInt(String.valueOf(holder.quantity.getText().toString()));
                    int b = Integer.parseInt(String.valueOf(map.get("price")));
                    String cost = String.valueOf(a * b);

                    add_to_carts(map.get("id"), String.valueOf(b),cost);
                }
            });
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void add_to_carts(final String id, final String s, final String cost) {

      progressBar.setVisibility(View.VISIBLE);
        String url="https://missionlockdown.com/BoozeApp/api/add-to-cart";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject json=new JSONObject(response);
                    if (json.getString("success").equalsIgnoreCase("true"))
                    {
                        Toast.makeText(ListActivity.this, "Added To Cart !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "response :" +error, Toast.LENGTH_SHORT).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("product_id",id);
                map.put("quantity",s);
                map.put("cost",cost);
                Log.d("mAKKK", String.valueOf(map));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private class MyHolder extends RecyclerView.ViewHolder {
        TextView name,price,quantity;
        Button button;
        ImageView plus, minus;
        public MyHolder(View view) {
            super(view);

            name=view.findViewById(R.id.name);
            price=view.findViewById(R.id.price);
            quantity=view.findViewById(R.id.quantity);
            button=view.findViewById(R.id.add);
            plus=view.findViewById(R.id.plus);
            minus=view.findViewById(R.id.minus);
        }
    }
}

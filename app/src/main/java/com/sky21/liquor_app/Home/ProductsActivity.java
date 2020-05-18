package com.sky21.liquor_app.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.bumptech.glide.Glide;
import com.sky21.liquor_app.CartActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;
import com.sky21.liquor_app.UserAccount.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity {
    TextView addressTxt,filter;
    ImageView profile,search, cart;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
    String store_id,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().hide();

        addressTxt=findViewById(R.id.addressId);
        profile=findViewById(R.id.myprofileId);
        search=findViewById(R.id.searchView);
        filter=findViewById(R.id.filter);
        cart=findViewById(R.id.cart);
        recyclerView=findViewById(R.id.recyclerview);
        progressBar=findViewById(R.id.progressbar);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        token= SharedHelper.getKey(ProductsActivity.this,"token");
        final Intent intent=getIntent();
        store_id=intent.getStringExtra("ID");

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(intent);
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="https://missionlockdown.com/BoozeApp/api/products?store_id="+store_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);


                    Log.d("response",response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("products");
                        for(int j=0; j<jsonArray.length(); j++)
                        {

                            JSONObject object=jsonArray.getJSONObject(j);
                            HashMap<String,String> params=new HashMap<>();
                            params.put("id",object.getString("id"));
                            params.put("state_id",object.getString("state_id"));
                            params.put("name",object.getString("name"));
                            params.put("price",object.getString("price"));
                            params.put("image",object.getString("image"));
                            params.put("quantity",object.getString("quantity"));


                            storeList.add(params);

                        }

                        Mainadapter mainadapter=new Mainadapter(getApplicationContext(),storeList);
                        recyclerView.setAdapter(mainadapter);


                    }

                    else
                    {
                        Toast.makeText(ProductsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
        }){


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<>();
//                map.put("searched_name","old");
//                map.put("category_id","1");
//                map.put("brand_id","1");
//                return map;
//            }
        };

        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private class Mainadapter extends RecyclerView.Adapter<MyHolder> {
        Context context;
        ArrayList<HashMap<String, String>> storelist = new ArrayList<>();

        public Mainadapter(Context context, ArrayList<HashMap<String, String>> storeList) {
            this.context=context;
            this.storelist=storeList ;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.product_recycler,null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

            final HashMap<String,String> map=storelist.get(position);

            holder.price.setText(getString(R.string.rupee)+" "+map.get("price"));
            holder.name.setText(map.get("name"));
            Glide.with(context).load(map.get("image")).into(holder.product_image);

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int quantity = (Integer.parseInt(holder.counter.getText().toString()));
                    quantity = quantity + 1;
                    holder.counter.setText(String.valueOf(quantity));
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(holder.counter.getText().toString())>1)
                    {
                        int quantity = (Integer.parseInt(holder.counter.getText().toString()));
                        quantity = quantity - 1;
                        holder.counter.setText(String.valueOf(quantity));

                    }else
                    {

                    }
                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int a= Integer.parseInt(String.valueOf(holder.counter.getText().toString()));
                    int b=Integer.parseInt(String.valueOf(map.get("price")));

                    String cost= String.valueOf(a*b);
                    Log.d("total",cost);

                    add_to_cart(map.get("id"), a,cost);
                }
            });

        }

        @Override
        public int getItemCount() {

            return storelist.size();
        }
    }

    private void add_to_cart(final String s, final int id, final String toString) {

        progressBar.setVisibility(View.VISIBLE);
        String url="https://missionlockdown.com/BoozeApp/api/add-to-cart";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject json=new JSONObject(response);
                    if (json.getString("success").equalsIgnoreCase("true"))
                    {

                        Toast.makeText(ProductsActivity.this, "Added to cart !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.INVISIBLE);
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
                map.put("product_id",s);
                map.put("quantity", String.valueOf(id));
                map.put("cost",toString);

                Log.d("map", String.valueOf(map));
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
        CardView cardView;
        TextView name, price,counter;
        ImageView product_image,plus,minus;
        Button add;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            cardView=itemView.findViewById(R.id.card);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            product_image=itemView.findViewById(R.id.productImageId);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            counter=itemView.findViewById(R.id.quantity);
            add=itemView.findViewById(R.id.add);

        }
    }
}

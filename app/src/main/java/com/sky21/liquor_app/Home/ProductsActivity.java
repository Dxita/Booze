package com.sky21.liquor_app.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.bumptech.glide.Glide;
import com.sky21.liquor_app.CartActivity;
import com.sky21.liquor_app.DataTransferListener;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;
import com.sky21.liquor_app.UserAccount.ProfileActivity;
import com.sky21.liquor_app.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity implements DataTransferListener{
    TextView addressTxt, filter;
    TextView item_count_tv, price_count_tv;
    LinearLayout view_cart_ll, cart_bottom_sheet_ll;
    ImageView profile, search, cart;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;

    private int ftotal = 0;
    Map<String, Integer> subTotal = new HashMap<String, Integer>();

    private   int total =0;
    private int quantity=0;
    List<ProductModel> storeList_1 = new ArrayList<>();
    Mainadapter mainadapter;
    String store_id, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().hide();

        //BottomViewCartSheet
        cart_bottom_sheet_ll = findViewById(R.id.cart_bottom_sheet_ll);
        item_count_tv = findViewById(R.id.item_count_tv);
        price_count_tv = findViewById(R.id.price_count_tv);
        view_cart_ll = findViewById(R.id.view_cart_ll);

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

        cart_bottom_sheet_ll.setVisibility(View.GONE);


        token = SharedHelper.getKey(ProductsActivity.this, "token");
        final Intent intent = getIntent();
        store_id = intent.getStringExtra("ID");

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

        layoutManager = new LinearLayoutManager(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        api();
    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://missionlockdown.com/BoozeApp/api/products?store_id=" + store_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    Log.d("response", response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        for (int j = 0; j < jsonArray.length(); j++) {

                            JSONObject object = jsonArray.getJSONObject(j);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", object.getString("id"));
                            params.put("state_id", object.getString("state_id"));
                            params.put("name", object.getString("name"));
                            params.put("price", object.getString("price"));
                            params.put("image", object.getString("image"));
                            params.put("quantity", object.getString("quantity"));


                            storeList_1.add(new ProductModel(params));

                        }

                        setProductRecylerView(storeList_1);

                    } else {
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

    private void setProductRecylerView(List<ProductModel> list) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainadapter = new Mainadapter(this, list,this);
        recyclerView.setAdapter(mainadapter);
    }

    @Override
    public void getProductData(final String id, final int quantity, final String cost) {
        if (quantity >= 1){
            cart_bottom_sheet_ll.setVisibility(View.VISIBLE);
            item_count_tv.setText(quantity + " Items");
            price_count_tv.setText(getString(R.string.rupee) + " " + cost);
            view_cart_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_to_cart(id,quantity,cost);
                    Intent intent = new Intent(ProductsActivity.this,CartActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            cart_bottom_sheet_ll.setVisibility(View.GONE);
        }
    }

    //---------------------------- Add to cart Api---------------------------------------------
    private void add_to_cart(final String s, final int id, final String toString) {

        progressBar.setVisibility(View.VISIBLE);
        String url = "https://missionlockdown.com/BoozeApp/api/add-to-cart";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equalsIgnoreCase("true")) {

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
                Toast.makeText(getApplicationContext(), "response :" + error, Toast.LENGTH_SHORT).show();
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", s);
                map.put("quantity", String.valueOf(id));
                map.put("cost", toString);

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



    //------------------------------------- Adapter -------------------------------------------
    public class Mainadapter extends RecyclerView.Adapter<MyHolder> {
        Context context;
        List<ProductModel> storelist;
        DataTransferListener listener;

        public Mainadapter(Context context, List<ProductModel> storeList,DataTransferListener listener) {
            this.context = context;
            this.storelist = storeList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.product_recycler, null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
            final ProductModel map = storelist.get(position);

            if (map != null) {
                holder.price.setText(getString(R.string.rupee) + " " + map.getMapList().get("price"));
                holder.name.setText(map.getMapList().get("name"));
                Glide.with(context).load(map.getMapList().get("image")).into(holder.product_image);
                final String key = map.getMapList().get("id");

                holder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      cart_bottom_sheet_ll.setVisibility(View.VISIBLE);
                        quantity = (Integer.parseInt(holder.counter.getText().toString()));




                        quantity = quantity + 1;



                        holder.counter.setText(String.valueOf(quantity));
                        total = Integer.parseInt(map.getMapList().get("price") )* quantity;

                        subTotal.put(key, total);

                        Log.d("total", "onBindViewHolder: " + total);



                        int a = Integer.parseInt(String.valueOf(holder.counter.getText().toString()));
                        int b = Integer.parseInt(String.valueOf(map.getMapList().get("price")));

                        String cost = String.valueOf(a * b);
                        Log.d("total", cost);

                        map.setId(map.getMapList().get("id"));
                        map.setQuantity(quantity);
                        map.setTotalCost(cost);
                        ((DataTransferListener)context).getProductData(key,map.getQuantity(),map.getTotalCost());

                        //   subtotal.setText(getString(R.string.rupee)+" "+""+total);


                        printMap();

                        /*int quantity = (Integer.parseInt(holder.counter.getText().toString()));
                        quantity = quantity + 1;
                        holder.counter.setText(String.valueOf(quantity));

                        int a = Integer.parseInt(String.valueOf(holder.counter.getText().toString()));
                        int b = Integer.parseInt(String.valueOf(map.getMapList().get("price")));

                        String cost = String.valueOf(a * b);
                        Log.d("total", cost);

                        map.setId(map.getMapList().get("id"));
                        map.setQuantity(quantity);
                        map.setTotalCost(cost);

                        ((DataTransferListener)context).getProductData(key,map.getQuantity(),map.getTotalCost());*/
                    }
                });
/*

                if (quantity==0){
                    cart_bottom_sheet_ll.setVisibility(View.GONE);
                }
*/

                holder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(quantity < 1) {

                            //cart_bottom_sheet_ll.setVisibility(View.GONE);
                            price_count_tv.setText(getString(R.string.rupee)+"0");
                            //holder.counter.setVisibility(View.GONE);
                            Toast.makeText(ProductsActivity.this, "0", Toast.LENGTH_SHORT).show();

                        }else {

                            quantity = quantity - 1;
                            total = Integer.parseInt(map.getMapList().get("price")) * quantity;

                            holder.counter.setText(String.valueOf(quantity));
                        }
                        subTotal.put(key, total);
                        printMap();

                        int a = Integer.parseInt(String.valueOf(holder.counter.getText().toString()));
                        int b = Integer.parseInt(String.valueOf(map.getMapList().get("price")));

                        String cost = String.valueOf(b / a);

                        map.setId(map.getMapList().get("id"));
                        map.setQuantity(quantity);
                        map.setTotalCost(cost);

                        ((DataTransferListener)context).getProductData(key,map.getQuantity(),map.getTotalCost());
                       /* if (Integer.parseInt(holder.counter.getText().toString()) > 1) {
                            int quantity = (Integer.parseInt(holder.counter.getText().toString()));
                            quantity = quantity - 1;
                            holder.counter.setText(String.valueOf(quantity));

                            int a = Integer.parseInt(String.valueOf(holder.counter.getText().toString()));
                            int b = Integer.parseInt(String.valueOf(map.getMapList().get("price")));

                            String cost = String.valueOf(b / a);

                            map.setId(map.getMapList().get("id"));
                            map.setQuantity(quantity);
                            map.setTotalCost(cost);

                            ((DataTransferListener)context).getProductData(key,map.getQuantity(),map.getTotalCost());
                        } else {

                        }*/
                    }

                });
            } else {
                return;
            }
        }

        @Override
        public int getItemCount() {
            return storelist.size();
        }
    }

    public void printMap() {
        int t = 0;
        for (int value : subTotal.values()) {
            t = t + value;
        }
        //   Toast.makeText(this, "total is: "+t, Toast.LENGTH_SHORT).show();
        // subtotal.setText(getString(R.string.rupee)+""+t);
        price_count_tv.setText(getString(R.string.rupee)+String.valueOf(t));

        Log.d("totttttt", String.valueOf(t));
        Toast.makeText(ProductsActivity.this, "" + t, Toast.LENGTH_SHORT).show();
        SharedHelper.putKey(ProductsActivity.this, "totalitem", "" + t);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name, price, counter;
        ImageView product_image, plus, minus;
        Button add;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            product_image = itemView.findViewById(R.id.productImageId);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            counter = itemView.findViewById(R.id.quantity);
            add = itemView.findViewById(R.id.add);

        }
    }
}
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sky21.liquor_app.Home.ProductsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    String token;
    TextView addressTxt;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
    Button place_order;
    String cart_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        token= SharedHelper.getKey(CartActivity.this,"token");
        addressTxt=findViewById(R.id.addressId);

        recyclerView=findViewById(R.id.recyclerview);
        progressBar=findViewById(R.id.progressbar);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

place_order=findViewById(R.id.place_order);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        getCartdata();



        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),OrderplacedActivity.class);
                intent.putExtra("TOTAL",cart_total);
                startActivity(intent);
            }
        });

    }



    private void getCartdata() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="https://missionlockdown.com/BoozeApp/api/cart";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cart",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true"))
                    {

                        cart_total=jsonObject.getString("total");
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int j=0; j<jsonArray.length(); j++)
                        {

                            JSONObject object=jsonArray.getJSONObject(j);
                            HashMap<String,String> params=new HashMap<>();
                            params.put("id",object.getString("id"));
                            params.put("user_id",object.getString("user_id"));
                            params.put("product_id",object.getString("product_id"));
                            params.put("cost",object.getString("cost"));
                            params.put("quantity",object.getString("quantity"));

                            JSONObject object1=object.getJSONObject("product");
                            params.put("id",object1.getString("id"));
                            params.put("state_id",object1.getString("state_id"));
                            params.put("name",object1.getString("name"));
                            params.put("price",object1.getString("price"));
                            params.put("image",object1.getString("image"));


                            storeList.add(params);

                        }

                        CartAdapter cartadapter=new CartAdapter(getApplicationContext(),storeList);
                        recyclerView.setAdapter(cartadapter);
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
    }


    private class CartAdapter extends RecyclerView.Adapter<CartHolder> {
        Context context;
        ArrayList<HashMap<String, String>> storelist = new ArrayList<>();

        public CartAdapter(Context context, ArrayList<HashMap<String, String>> storeList) {
            this.context=context;
            this.storelist=storeList ;
        }

        @NonNull
        @Override
        public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.cart_list,null);
            return new CartHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartHolder holder, int position) {
            final HashMap<String,String> map=storelist.get(position);

            holder.name.setText(map.get("name"));
            holder.price.setText("Per item price :"+" "+getString(R.string.rupee)+map.get("price"));
            holder.quantity.setText("Quantity is :"+" "+map.get("quantity"));
            holder.cost.setText("Total cost:"+" "+getString(R.string.rupee)+map.get("cost"));
        }



        @Override
        public int getItemCount() {
            return storeList.size();
        }
    }

    private class CartHolder extends RecyclerView.ViewHolder {
        TextView name, price, cost, quantity;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            cost=itemView.findViewById(R.id.cost);
            quantity=itemView.findViewById(R.id.quantity);
        }
    }
}

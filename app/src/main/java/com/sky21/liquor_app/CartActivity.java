package com.sky21.liquor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
    TextView addressTxt,delete_all;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
    Button place_order;
    String cart_total;
    ImageView empty_cart;
    String total_cost;
     String time ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        token = SharedHelper.getKey(CartActivity.this, "token");
        addressTxt = findViewById(R.id.addressId);
        delete_all = findViewById(R.id.delete_all);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        empty_cart = findViewById(R.id.empty_cart);

        place_order = findViewById(R.id.place_order);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        getCartdata();


        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSubmitDialog();

              /*  Intent intent=new Intent(getApplicationContext(),OrderplacedActivity.class);
        intent.putExtra("TOTAL",cart_total);
        startActivity(intent);*/
            }
        });

    }

    private void showSubmitDialog() {
        final Dialog dialog=new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.time_picker);
        dialog.setCancelable(false);
        dialog.show();

        final TextView time_text;


        TimePicker simpleTimePicker;

        simpleTimePicker = (TimePicker) dialog.findViewById(R.id.simpleTimePicker);
        time_text=dialog.findViewById(R.id.time_txt);
        simpleTimePicker.setIs24HourView(false);


        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();
            time_text.setText(hourOfDay + " : " + minute);// set the current time in text view

                time= time_text.getText().toString();


            }
        });

        Button ok= dialog.findViewById(R.id.Submit);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


                orderplace();
                /*Intent intent=new Intent(CartActivity.this,LoginActivity.class);
                startActivity(intent);*/
            }
        });

    }

    private void orderplace() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        String url="https://missionlockdown.com/BoozeApp/api/place-order";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("vvvvvvvvvvvvvvvv",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("success").equalsIgnoreCase("true"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            HashMap<String,String>map=new HashMap<>();
                            map.put("order_id", object.getString("order_id"));
                            map.put("total_cost",object.getString("total_cost"));
                            total_cost=map.get("total_cost");

                        }

                        Intent intent=new Intent(getApplicationContext(),OrderplacedActivity.class);
                        intent.putExtra("COST",total_cost);
                        startActivity(intent);
                    }
                    else
                    {
                       empty_cart.setVisibility(View.VISIBLE);
                       recyclerView.setVisibility(View.GONE);
                       place_order.setVisibility(View.GONE);
                       delete_all.setVisibility(View.GONE);

                    }
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>map=new HashMap<>();
                map.put("pick_up_time", "10.30");
                map.put("store_id",SharedHelper.getKey(CartActivity.this,"store_id"));
                
                Log.d("time", String.valueOf(map));
                return map;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); 
    }

    private void delete() {

        progressBar.setVisibility(View.VISIBLE);
        String url ="https://missionlockdown.com/BoozeApp/api/empty-cart";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    if (json.getString("success").equalsIgnoreCase("true"))
                    {
                        Toast.makeText(CartActivity.this, ""+ "Successfully removed all the items from the cart!", Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.GONE);
                        place_order.setVisibility(View.GONE);
                        delete_all.setVisibility(View.GONE);
                        empty_cart.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(CartActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "no internet access", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

                        delete_all.setVisibility(View.VISIBLE);
                        CartAdapter cartadapter=new CartAdapter(getApplicationContext(),storeList);
                        recyclerView.setAdapter(cartadapter);
                    }

                    else
                    {
                        delete_all.setVisibility(View.GONE)                      ;
                        recyclerView.setVisibility(View.GONE);
                        empty_cart.setVisibility(View.VISIBLE);
                        place_order.setVisibility(View.GONE);

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
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {
            final HashMap<String,String> map=storelist.get(position);

            holder.name.setText(map.get("name"));
            holder.price.setText(getString(R.string.rupee)+map.get("price"));
            holder.quantity.setText(map.get("quantity"));
//            holder.cost.setText("Total cost:"+" "+getString(R.string.rupee)+map.get("cost"));
            holder.delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteCart(map.get("id"));
                    storeList.remove(position);

                    notifyDataSetChanged();
                }
            });

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int quantity = (Integer.parseInt(holder.quantity.getText().toString()));
                    quantity = quantity + 1;
                    holder.quantity.setText(String.valueOf(quantity));

                    int a= Integer.parseInt(String.valueOf(holder.quantity.getText().toString()));
                    int b=Integer.parseInt(String.valueOf(map.get("price")));

                    String cost= String.valueOf(a*b);
                    Log.d("total",cost);

                    add_to_cart(map.get("id"), a,cost);


                }
            });


            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Integer.parseInt(holder.quantity.getText().toString())>1)
                    {
                        int quantity = (Integer.parseInt(holder.quantity.getText().toString()));
                        quantity = quantity - 1;
                        holder.quantity.setText(String.valueOf(quantity));

                    }else
                    {

                    }

                    int a= Integer.parseInt(String.valueOf(holder.quantity.getText().toString()));
                    int b=Integer.parseInt(String.valueOf(map.get("price")));

                    String cost= String.valueOf(a*b);
                    Log.d("total",cost);

                    add_to_cart(map.get("id"), a,cost);

                }


            });
        }



        @Override
        public int getItemCount() {
            return storeList.size();
        }
    }



    private void deleteCart(final String id) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        String url = "https://boozeapp.co/Booze-App-Api/api/remove-from-cart";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    Log.d("deleteresponse", response);

                    if (json.getString("success").equalsIgnoreCase("true")) {
                        /*Intent intent=new Intent( CartActivity.this,CartActivity.class);
                        startActivity(intent);*/
                        Toast.makeText(CartActivity.this, "Item removed from cart!", Toast.LENGTH_SHORT).show();
                        getCartdata();               
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
               progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CartActivity.this, "no internet access", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", id);
              
                return map;
            }

        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private class CartHolder extends RecyclerView.ViewHolder {
        TextView name, price, cost, quantity;
        ImageView delete_item,plus,minus;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            delete_item=itemView.findViewById(R.id.delete_item);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
        }
    }

    private void add_to_cart(final String s, final int id, final String toString) {

        progressBar.setVisibility(View.VISIBLE);
        String url="https://boozeapp.co/Booze-App-Api/api/add-to-cart";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject json=new JSONObject(response);
                    if (json.getString("success").equalsIgnoreCase("true"))
                    {

                       /* Toast.makeText(CartActivity.this, "Added to cart !", Toast.LENGTH_SHORT).show();*/
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
}

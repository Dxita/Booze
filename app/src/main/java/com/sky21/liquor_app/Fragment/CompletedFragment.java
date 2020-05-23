package com.sky21.liquor_app.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends Fragment {
RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManagers;
    String id,total_cost,token;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
    HashMap<String,String>data=new HashMap<>();
    TextView textView;
    public CompletedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_completed, container, false);
   token = SharedHelper.getKey(getActivity(), "token");

   progressBar=v.findViewById(R.id.progressbar);
   textView=v.findViewById(R.id.text);
        id= SharedHelper.getKey(getActivity(),"strid");
        recyclerView=v.findViewById(R.id.recyclerview);
        layoutManagers = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagers);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        api();
        return v;
    }

    private void api() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String url="https://boozeapp.co/Booze-App-Api/api/orders-list?"+"store_id=1"+"&status=3";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("success").equalsIgnoreCase("true"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            HashMap<String,String>map=new HashMap<>();
                            data.put("order_id", object.getString("order_id"));
                            total_cost=map.get("total");

                            JSONArray array=object.getJSONArray("object_product");
                            for (int j=0; j<array.length(); j++)
                            {
                                JSONObject obj=array.getJSONObject(j);
                                HashMap<String,String>param=new HashMap<>();
                                param.put("id",obj.getString("id"));
                                param.put("order_id",obj.getString("id"));
                                param.put("product_id",obj.getString("product_id"));
                                param.put("quantity",obj.getString("quantity"));
                                param.put("cost",obj.getString("cost"));

                                JSONObject o=obj.getJSONObject("product");
                                param.put("id",obj.getString("id"));
                                param.put("name",obj.getString("name"));
                                param.put("price",obj.getString("price"));
                                param.put("size",obj.getString("size"));
                                param.put("image",obj.getString("image"));


                                JSONObject jbject1=o.getJSONObject("store");
                                {

                                    param.put("id",jbject1.getString("id"));
                                    param.put("name",jbject1.getString("name"));
                                    param.put("address",jbject1.getString("address"));
                                    param.put("license_number",jbject1.getString("license_number"));
                                    param.put("license_valid_till",jbject1.getString("license_valid_till"));
                                    param.put("lat",jbject1.getString("lat"));
                                    param.put("long",jbject1.getString("long"));



                                }


                                storeList.add(param);

                            }
                            MyAdapter myadapter=new MyAdapter(getActivity(),storeList);
                            recyclerView.setAdapter(myadapter);

                        }

                    }

                    else if (jsonObject.getString("success").equalsIgnoreCase("false"))
                    {
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    else
                    {


                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private class MyAdapter extends RecyclerView.Adapter<Holder> {

        Context context;
        ArrayList<HashMap<String, String>> storelist = new ArrayList<>();

        public MyAdapter(Context context, ArrayList<HashMap<String, String>> storeList) {

            this.context=context;
            this.storelist=storeList ;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.completed,null);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView name, price, quantity, cost, ordered_on, orderid;
        public Holder(View view) {
            super(view);


        }
    }
}

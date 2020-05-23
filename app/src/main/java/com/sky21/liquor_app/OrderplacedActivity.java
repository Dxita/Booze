package com.sky21.liquor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sky21.liquor_app.UserAccount.HistoryActivity;

public class OrderplacedActivity extends AppCompatActivity {
    TextView addressTxt;
    TextView item_total;
    TextView total;
    TextView time_txt;
    TextView store_name;
    TextView address;
    TextView order_id;
    String cart_cost,time,add,sn,oi,store_id;
    Button continue_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderplaced);
        getSupportActionBar().hide();
        addressTxt=findViewById(R.id.addressId);

        item_total=findViewById(R.id.item_total);
        total=findViewById(R.id.total);
        time_txt=findViewById(R.id.time);
        store_name=findViewById(R.id.storename);
        order_id=findViewById(R.id.orderid);
        address=findViewById(R.id.storeaddress);

        continue_button=findViewById(R.id.continue_button);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent=getIntent();
        cart_cost=intent.getStringExtra("COST");
        time=intent.getStringExtra("time");
        add=intent.getStringExtra("add");
        sn=intent.getStringExtra("name");
        oi=intent.getStringExtra("orderid");
        store_id=intent.getStringExtra("STOREID");



        time_txt.setText("Pickup time is:"+" "+time);
        item_total.setText(getString(R.string.rupee)+cart_cost);
        total.setText(getString(R.string.rupee)+cart_cost);
        order_id.setText("Order ID"+":"+" "+oi);
        store_name.setText(sn);
        address.setText(add);




        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(), HistoryActivity.class);
                intent.putExtra("STRID",store_id);
                startActivity(intent1);
            }
        });
    }


}

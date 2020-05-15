package com.sky21.liquor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrderplacedActivity extends AppCompatActivity {
    TextView addressTxt,item_total, total;
    String cart_cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderplaced);
        getSupportActionBar().hide();
        addressTxt=findViewById(R.id.addressId);

        item_total=findViewById(R.id.item_total);
        total=findViewById(R.id.total);

        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent=getIntent();
        cart_cost=intent.getStringExtra("TOTAL");
        item_total.setText(getString(R.string.rupee)+cart_cost);
        total.setText(getString(R.string.rupee)+cart_cost);



    }


}

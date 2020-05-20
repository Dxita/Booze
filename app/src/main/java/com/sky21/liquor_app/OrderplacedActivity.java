package com.sky21.liquor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sky21.liquor_app.UserAccount.HistoryActivity;

public class OrderplacedActivity extends AppCompatActivity {
    TextView addressTxt,item_total, total;
    String cart_cost;
    Button continue_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderplaced);
        getSupportActionBar().hide();
        addressTxt=findViewById(R.id.addressId);

        item_total=findViewById(R.id.item_total);
        total=findViewById(R.id.total);

        continue_button=findViewById(R.id.continue_button);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent=getIntent();
        cart_cost=intent.getStringExtra("COST");
        item_total.setText(getString(R.string.rupee)+cart_cost);
        total.setText(getString(R.string.rupee)+cart_cost);



        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent1);
            }
        });
    }


}

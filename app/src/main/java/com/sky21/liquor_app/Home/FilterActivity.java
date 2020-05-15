package com.sky21.liquor_app.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sky21.liquor_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    TextView backspace,unselect_all_tv;
    TextView category_tv,brand_tv,price_tv;
    RecyclerView category_rv;
    FilterAdapter adapter;
    List<String> list;

    String[] category_list = {"Vodka","Gin","Tequila","Rum","Whiskey","Beer","Brandy"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().hide();

        category_tv=findViewById(R.id.category_tv);
        brand_tv=findViewById(R.id.brand_tv);
        price_tv=findViewById(R.id.price_tv);
       // unselect_all_tv=findViewById(R.id.unselect_all_tv);

        category_tv.setBackgroundColor(Color.WHITE);
        brand_tv.setBackgroundColor(Color.GRAY);
        price_tv.setBackgroundColor(Color.GRAY);

        category_rv=findViewById(R.id.category_rv);
        list = new ArrayList<String>();

        backspace=findViewById(R.id.addressId);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_tv.setBackgroundColor(Color.WHITE);
                brand_tv.setBackgroundColor(Color.GRAY);
                price_tv.setBackgroundColor(Color.GRAY);
                setCategoryRecyclerView();
            }
        });

        brand_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_tv.setBackgroundColor(Color.GRAY);
                brand_tv.setBackgroundColor(Color.WHITE);
                price_tv.setBackgroundColor(Color.GRAY);
                setCategoryRecyclerView();
            }
        });

        price_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_tv.setBackgroundColor(Color.GRAY);
                brand_tv.setBackgroundColor(Color.GRAY);
                price_tv.setBackgroundColor(Color.WHITE);
                setCategoryRecyclerView();
            }
        });

      /*  unselect_all_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryRecyclerView();
            }
        });*/

        setCategoryRecyclerView();
    }

    private void setCategoryRecyclerView(){
        list.addAll(Arrays.asList(category_list));
        category_rv.setHasFixedSize(true);
        category_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FilterAdapter(this,list);
        category_rv.setAdapter(adapter);
    }

    public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder>{

        Context context;
        List<String> list;

        public FilterAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.filter_item_list,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String item = list.get(position);
            holder.checkbox.setText(item);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkbox;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkbox = itemView.findViewById(R.id.checkbox);
            }
        }
    }
}

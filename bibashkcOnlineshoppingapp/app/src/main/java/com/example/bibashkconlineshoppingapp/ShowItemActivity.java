package com.example.bibashkconlineshoppingapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowItemActivity extends AppCompatActivity {
    private ImageView imagee;
    private TextView namee, pricee, description;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Online_Clothing_Shop");

        imagee = findViewById(R.id.iv_photo);
        namee = findViewById(R.id.tv_name);
        pricee = findViewById(R.id.tv_price);
        description = findViewById(R.id.tv_description);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String image = bundle.getString("image");
            Picasso.with(this).load("http://10.0.2.2:3000/uploads/" + image).into(imagee);
            namee.setText(bundle.getString("name"));
            pricee.setText(bundle.getString("price"));
            description.setText(bundle.getString("description"));

        }
    }
}

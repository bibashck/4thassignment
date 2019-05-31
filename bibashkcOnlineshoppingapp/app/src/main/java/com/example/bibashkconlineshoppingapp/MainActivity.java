package com.example.bibashkconlineshoppingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bibashkconlineshoppingapp.Adapter.ItemAdapter;
import com.example.bibashkconlineshoppingapp.Interface.ItemInterface;
import com.example.bibashkconlineshoppingapp.models.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ActionBar actionBar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getAllItems();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Online Clothing");

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        textView.setText("Welcome,   " + preferences.getString("username", ""));
    }
    private void getAllItems() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItemInterface itemApi = retrofit.create(ItemInterface.class);

        Call<List<Item>> listCall = itemApi.getAllItems();

        listCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Item> itemModelList = response.body();

                recyclerView.setAdapter(new ItemAdapter(itemModelList, MainActivity.this));
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menubar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Add_item:

                Intent addItem = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(addItem);
                break;
            case R.id.Logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you really want to exit?")
                        // .setTitle("Title")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.remove("user");
                                editor.putBoolean("LoginStatus", false).commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.show();




                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.bibashkconlineshoppingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bibashkconlineshoppingapp.R;
import com.example.bibashkconlineshoppingapp.ShowItemActivity;
import com.example.bibashkconlineshoppingapp.models.Item;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    List<Item> itemList;
    Context context;
    Bitmap bitmap;
    public static final String BASE_URL = "http://10.0.2.2:3000/";



    public ItemAdapter(List<Item> itemModelList, Context context) {
        this.itemList = itemModelList;
        this.context = context;

    }

    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sample_row,
                viewGroup, false);

        return new ItemViewHolder(itemView);
    }
    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder itemViewHolder, int i) {
        final Item item = itemList.get(i);


        itemViewHolder.name.setText(item.getItemName());
        itemViewHolder.price.setText(item.getItemPrice());

        StrictMode();

        final String path = BASE_URL+"uploads/"+itemList.get(i).getItemImageName();
        System.out.println("Path: " +path);

        try {
            URL uri = new URL(path);
            bitmap = BitmapFactory.decodeStream((InputStream)uri.getContent());
            itemViewHolder.image.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShowItemActivity.class);
                intent.putExtra("image", item.getItemImageName());
                intent.putExtra("name", item.getItemName());
                intent.putExtra("price", item.getItemPrice());
                intent.putExtra("description", item.getItemDescription());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, price;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.tv_itemName);
            price = itemView.findViewById(R.id.tv_price);


        }


    }
}

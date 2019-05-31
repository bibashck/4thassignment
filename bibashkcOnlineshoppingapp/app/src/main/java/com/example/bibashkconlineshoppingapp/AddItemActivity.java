package com.example.bibashkconlineshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bibashkconlineshoppingapp.Interface.ItemInterface;
import com.example.bibashkconlineshoppingapp.models.Image;
import com.example.bibashkconlineshoppingapp.models.Item;
import com.example.bibashkconlineshoppingapp.Interface.ItemInterface;
import com.example.bibashkconlineshoppingapp.models.Image;
import com.example.bibashkconlineshoppingapp.models.Item;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class AddItemActivity extends AppCompatActivity implements View.OnClickListener {
    private Button confirmImage, addItem;
    private EditText itemName, itemPrice, description;
    private TextView imageName;
    private ImageView itemImage;
    ActionBar actionBar;
    Uri imageUri;
    Bitmap bitmap;
    Retrofit retrofit;
    ItemInterface itemInterface;
    private static final int PICK_IMAGE = 1;
    public static final String BASE_URL = "http://10.0.2.2:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initializeItems();
    }



    private void initializeItems() {

        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.price);
        imageName = findViewById(R.id.imageName);
        description = findViewById(R.id.description);

        itemImage = findViewById(R.id.iv_image);

        confirmImage = findViewById(R.id.btn_confirmImage);
        addItem = findViewById(R.id.btn_addItem);

        confirmImage.setOnClickListener(this);
        addItem.setOnClickListener(this);
        itemImage.setOnClickListener(this);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Online_Clothing_Shop");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_image:
                openGallery();
                break;

            case R.id.btn_confirmImage:
                addImage(bitmap);
                addItem.setEnabled(true);
                break;

            case R.id.btn_addItem:
                if (Validate() == true) {
                    addItem();
                } else {
                    Toast.makeText(AddItemActivity.this, "Empty Fields", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
    private void openGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Choose Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap
                        (getContentResolver(), imageUri);

                itemImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void createInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemInterface = retrofit.create(ItemInterface.class);
    }
    private void addImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        try {
            File file = new File(this.getCacheDir(), "image.jpeg");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();

            RequestBody requestBody = RequestBody.
                    create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.
                    createFormData("imageFile", file.getName(), requestBody);


            createInstance();
            Call<Image> imgCall = itemInterface.uploadImage(body);
            imgCall.enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {
                    imageName.setText(response.body().getFilename());
                }

                @Override
                public void onFailure(Call<Image> call, Throwable t) {
                    Toast.makeText(AddItemActivity.this, "error is" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addItem() {
        createInstance();

        String itemNameFinal = itemName.getText().toString();
        String itemPriceFinal = itemPrice.getText().toString();
        String imageNameFinal = imageName.getText().toString();
        String itemDescriptionFinal = description.getText().toString();

        Item item = new Item(itemNameFinal, itemPriceFinal, imageNameFinal, itemDescriptionFinal);

        Call<Void> voidCall = itemInterface.addItem(item);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(AddItemActivity.this, "Item Added Successfully ", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddItemActivity.this, "Error :" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_menubar, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.back:

                Intent back = new Intent(AddItemActivity.this, com.example.bibashkconlineshoppingapp.MainActivity.class);
                startActivity(back);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean Validate() {


        if (TextUtils.isEmpty(itemName.getText().toString())) {
            itemName.setError("Enter Item Name");
            itemName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(itemPrice.getText().toString())) {
            itemPrice.setError("Enter Price");
            itemPrice.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Enter item description");
            description.requestFocus();
            return false;
        } else {
            return true;

        }
    }

}

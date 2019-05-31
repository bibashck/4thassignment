package com.example.bibashkconlineshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bibashkconlineshoppingapp.Interface.User_Interface;
import com.example.bibashkconlineshoppingapp.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText editTextid,editTextpw;
    Button buttonLogin ,regi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextid = findViewById(R.id.uid);
        editTextpw = findViewById(R.id.pwd);
        buttonLogin = findViewById(R.id.btnLogin);
        regi = findViewById(R.id.regi);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                User_Interface userInterface = retrofit.create(User_Interface.class);
                Call<ResponseBody> logincall = userInterface.userLogin
                        (new User(editTextid.getText().toString(),editTextpw.getText().toString()));
                logincall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            String session = response.headers().get("Set-Cookie");
                            Intent intent = new Intent(LoginActivity.this, com.example.bibashkconlineshoppingapp.MainActivity.class);
                            intent.putExtra("mysession",session);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}

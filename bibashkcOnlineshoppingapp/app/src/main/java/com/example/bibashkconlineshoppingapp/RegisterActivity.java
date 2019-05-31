package com.example.bibashkconlineshoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bibashkconlineshoppingapp.Interface.User_Interface;
import com.example.bibashkconlineshoppingapp.models.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText fname, lname, user, regPass;
    Button btnSignUP, regi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        user = findViewById(R.id.user);
        regPass = findViewById(R.id.regPass);
        btnSignUP = findViewById(R.id.btnSignUP);

        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register() {
        String fnam = fname.getText().toString();
        String lnam = lname.getText().toString();
        String usern = user.getText().toString();
        String pass = regPass.getText().toString();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        User_Interface userInterface = retrofit.create(User_Interface.class);
        Call<ResponseBody> reg = userInterface.userSignup(new User(fnam, lnam, usern, pass));
        reg.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Sign up success", Toast.LENGTH_SHORT).show();

                    fname.setText("");
                    lname.setText("");
                    user.setText("");
                    regPass.setText("");
                }

                else{
                    try {
                        Toast.makeText(RegisterActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}


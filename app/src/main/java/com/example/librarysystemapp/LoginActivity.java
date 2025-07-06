package com.example.librarysystemapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarysystemapp.api.ApiClient;
import com.example.librarysystemapp.api.ApiService;
import com.example.librarysystemapp.models.LoginRequest;
import com.example.librarysystemapp.models.LoginResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "يرجى إدخال البريد الإلكتروني وكلمة المرور", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "يرجى إدخال بريد إلكتروني صحيح", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false); // تعطيل الزر أثناء الطلب

            LoginRequest loginRequest = new LoginRequest(email, password);

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<LoginResponse> call = apiService.loginUser(loginRequest);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    btnLogin.setEnabled(true); // تفعيل الزر بعد الاستجابة
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();
                        Toast.makeText(LoginActivity.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();

                        getSharedPreferences("prefs", MODE_PRIVATE)
                                .edit()
                                .putString("TOKEN", token)
                                .apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = "فشل في تسجيل الدخول. تحقق من بياناتك";
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            try {
                                errorMsg = errorBody.string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    btnLogin.setEnabled(true); // تفعيل الزر عند الفشل
                    Toast.makeText(LoginActivity.this, "خطأ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

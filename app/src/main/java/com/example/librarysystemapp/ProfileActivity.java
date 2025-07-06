package com.example.librarysystemapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarysystemapp.api.ApiClient;
import com.example.librarysystemapp.api.ApiService;
import com.example.librarysystemapp.models.LogoutResponse;
import com.example.librarysystemapp.models.User;
import com.example.librarysystemapp.models.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);

        loadUserProfile();

        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserProfile() {
        String token = getSharedPreferences("prefs", MODE_PRIVATE).getString("TOKEN", null);
        if (token == null) {
            // لا يوجد توكن، الانتقال لتسجيل الدخول
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserProfile("Bearer " + token);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().getUser();
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                } else if (response.code() == 401) {
                    // التوكن غير صالح أو منتهي
                    Toast.makeText(ProfileActivity.this, "يجب تسجيل الدخول مجددًا", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("prefs", MODE_PRIVATE).edit().remove("TOKEN").apply();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "فشل تحميل بيانات المستخدم", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "خطأ في الاتصال: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        String token = getSharedPreferences("prefs", MODE_PRIVATE).getString("TOKEN", null);
        if (token == null) {
            // لا يوجد توكن، الانتقال مباشرة لتسجيل الدخول
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LogoutResponse> call = apiService.logout("Bearer " + token);

        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    getSharedPreferences("prefs", MODE_PRIVATE).edit().remove("TOKEN").apply();

                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "فشل في تسجيل الخروج", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "خطأ في الاتصال: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

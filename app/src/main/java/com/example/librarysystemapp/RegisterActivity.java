package com.example.librarysystemapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarysystemapp.api.ApiClient;
import com.example.librarysystemapp.api.ApiService;
import com.example.librarysystemapp.models.RegisterRequest;
import com.example.librarysystemapp.models.RegisterResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("الاسم مطلوب");
                return;
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("يرجى إدخال بريد إلكتروني صحيح");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("كلمة المرور مطلوبة");
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("كلمتا المرور غير متطابقتين");
                return;
            }

            btnRegister.setEnabled(false); // تعطيل الزر أثناء الطلب

            RegisterRequest request = new RegisterRequest(name, email, password, confirmPassword);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<RegisterResponse> call = apiService.registerUser(request);

            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    btnRegister.setEnabled(true); // تفعيل الزر بعد الاستجابة

                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        String errorMsg = "فشل في التسجيل. تأكد من صحة البيانات";
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            try {
                                String error = errorBody.string();
                                JSONObject jsonObject = new JSONObject(error);
                                if (jsonObject.has("message")) {
                                    errorMsg = jsonObject.getString("message");
                                } else if (jsonObject.has("errors")) {
                                    JSONObject errors = jsonObject.getJSONObject("errors");
                                    Iterator<String> keys = errors.keys();
                                    if (keys.hasNext()) {
                                        String key = keys.next();
                                        errorMsg = errors.getJSONArray(key).getString(0);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    btnRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "فشل الاتصال بالخادم: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

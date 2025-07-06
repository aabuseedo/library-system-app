package com.example.librarysystemapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarysystemapp.adapters.BookAdapter;
import com.example.librarysystemapp.adapters.CategoryAdapter;
import com.example.librarysystemapp.api.ApiClient;
import com.example.librarysystemapp.api.ApiResponse;
import com.example.librarysystemapp.api.ApiService;
import com.example.librarysystemapp.models.Book;
import com.example.librarysystemapp.models.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView rvBooks, rvCategories;
    private BookAdapter bookAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Book> allBooks = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private ApiService apiService;
    ImageView imgTop ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiService.class);

        // ربط RecyclerViews
        rvBooks = findViewById(R.id.rvBooks);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, allBooks);
        rvBooks.setAdapter(bookAdapter);

        rvCategories = findViewById(R.id.rvCategories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // المهم
        categoryAdapter = new CategoryAdapter(this, categoryList, this);
        rvCategories.setAdapter(categoryAdapter);

        loadCategories();
        loadBooks(); // تحميل جميع الكتب في البداية

        imgTop = findViewById(R.id.imgTop);
        imgTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

    }

    private void loadCategories() {
        Call<ApiResponse<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(Call<ApiResponse<Category>> call, Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    categoryList.clear();
                    categoryList.add(new Category(0, "الكل")); // زر الكل
                    categoryList.addAll(response.body().getData());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "فشل تحميل التصنيفات", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "خطأ في الاتصال: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Categories Error: " + t.getMessage());
            }
        });
    }

    private void loadBooks() {
        Call<ApiResponse<Book>> call = apiService.getBooks();
        call.enqueue(new Callback<ApiResponse<Book>>() {
            @Override
            public void onResponse(Call<ApiResponse<Book>> call, Response<ApiResponse<Book>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    allBooks = response.body().getData();
                    bookAdapter.updateBooks(allBooks);
                } else {
                    Toast.makeText(MainActivity.this, "فشل تحميل الكتب", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Book>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "خطأ في الاتصال: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Books Error: " + t.getMessage());
            }
        });
    }

    private void loadBooksByCategory(int categoryId) {
        Call<ApiResponse<Book>> call = apiService.getBooksByCategory(categoryId);
        call.enqueue(new Callback<ApiResponse<Book>>() {
            @Override
            public void onResponse(Call<ApiResponse<Book>> call, Response<ApiResponse<Book>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    bookAdapter.updateBooks(response.body().getData());
                } else {
                    Toast.makeText(MainActivity.this, "فشل تحميل الكتب حسب التصنيف", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Book>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
                Log.e("API", "BooksByCategory Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        if (category.getId() == 0) {
            loadBooks();
        } else {
            loadBooksByCategory(category.getId());
        }
    }
}

package com.example.librarysystemapp.api;

import com.example.librarysystemapp.models.Book;
import com.example.librarysystemapp.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiService {
    @GET("categories")   // فقط categories بدون api/ لأنها موجودة في BASE_URL
    Call<ApiResponse<Category>> getCategories();

    @GET("books")        // نفس الشيء هنا
    Call<ApiResponse<Book>> getBooks();

    @GET("books")
    Call<ApiResponse<Book>> getBooksByCategory(@Query("category_id") int categoryId);

    @GET("books/{id}")
    Call<ApiResponse<Book>> getBookDetails(@Path("id") int id);
}

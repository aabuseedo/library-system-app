package com.example.librarysystemapp.api;

import com.example.librarysystemapp.models.Book;
import com.example.librarysystemapp.models.Category;
import com.example.librarysystemapp.models.LogoutResponse;
import com.example.librarysystemapp.models.RegisterRequest;
import com.example.librarysystemapp.models.RegisterResponse;
import com.example.librarysystemapp.models.LoginRequest;
import com.example.librarysystemapp.models.LoginResponse;
import com.example.librarysystemapp.models.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiService {



    @GET("categories")   // فقط categories بدون api/ لأنها موجودة في BASE_URL
    Call<ApiResponse<Category>> getCategories();

    @GET("books")
    Call<ApiResponse<Book>> getBooks();

    @GET("books")
    Call<ApiResponse<Book>> getBooksByCategory(@Query("category_id") int categoryId);

    @GET("books/{id}")
    Call<ApiResponse<Book>> getBookDetails(@Path("id") int id);

    @POST("register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("user")
    Call<UserResponse> getUserProfile(@Header("Authorization") String token);

    @POST("logout")
    Call<LogoutResponse> logout(@Header("Authorization") String token);

}

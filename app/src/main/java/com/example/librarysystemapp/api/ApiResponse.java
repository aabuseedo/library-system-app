package com.example.librarysystemapp.api;

import java.util.List;

public class ApiResponse<T> {
    private boolean success;
    private List<T> data;

    public boolean isSuccess() {
        return success;
    }

    public List<T> getData() {
        return data;
    }
}

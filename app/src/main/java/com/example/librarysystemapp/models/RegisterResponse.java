package com.example.librarysystemapp.models;

public class RegisterResponse {
    private boolean success;
    private String message;
    private User data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }

    public static class User {
        private int id;
        private String name;
        private String email;

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}

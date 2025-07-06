package com.example.librarysystemapp.models;

public class Book {
    private int id;
    private String title;
    private Author author;
    private String description;
    private String cover_image;
    private Category category;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; } // ترجع Author كامل
    public String getDescription() { return description; }
    public String getCoverImage() { return cover_image; }

    public Category getCategory() {
        return category;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "غير معروف";
    }

    public String getAuthorName() {
        return author != null ? author.getName() : "غير معروف";
    }
}

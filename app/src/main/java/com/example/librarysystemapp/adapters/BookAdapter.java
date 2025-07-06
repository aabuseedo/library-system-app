package com.example.librarysystemapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.librarysystemapp.BookDetailsActivity;
import com.example.librarysystemapp.R;
import com.example.librarysystemapp.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> books;

    public BookAdapter(Context ctx, List<Book> bookList) {
        this.context = ctx;
        this.books = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("بواسطة " + book.getAuthorName());

        Glide.with(context)
                .load(book.getCoverImage())
                .placeholder(R.drawable.book)
                .into(holder.imgCover);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailsActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthorName());
            intent.putExtra("coverUrl", book.getCoverImage());
            intent.putExtra("category", book.getCategoryName());
            intent.putExtra("description", book.getDescription());
            context.startActivity(intent);
        });
    }

    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor;
        ImageView imgCover;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            imgCover = itemView.findViewById(R.id.imgCover);
        }
    }
}

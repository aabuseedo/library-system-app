package com.example.librarysystemapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class BookDetailsActivity extends AppCompatActivity {

    private ImageView imgBookCover;
    private TextView tvBookTitle, tvAuthor, tvCategory, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);

        // التعامل مع النوافذ للـ EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ربط عناصر الواجهة
        imgBookCover = findViewById(R.id.imgBookCover);
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategory = findViewById(R.id.tvCategory);
        tvDescription = findViewById(R.id.tvDescription);

        // استلام بيانات الكتاب المرسلة من MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String author = extras.getString("author");
            String category = extras.getString("category");
            String coverUrl = extras.getString("coverUrl");
            String description = extras.getString("description");

            // تعبئة البيانات في الواجهة
            tvBookTitle.setText(title);
            tvAuthor.setText("بواسطة " + author);
            tvCategory.setText(category);
            tvDescription.setText(description);

            // تحميل صورة الغلاف باستخدام Glide
            Glide.with(this)
                    .load(coverUrl)
                    .placeholder(R.drawable.book)
                    .into(imgBookCover);
        }
    }
}

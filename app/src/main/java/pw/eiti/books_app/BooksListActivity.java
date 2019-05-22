package pw.eiti.books_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pw.eiti.books_app.adapters.BookDtoListAdapter;
import pw.eiti.books_app.client.RestUtils;
import pw.eiti.books_app.domain.BookDTO;
import pw.eiti.books_app.domain.BookDetailsDTO;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pw.eiti.books_app.client.RestUtils.SERVICE_URL;

public class BooksListActivity extends AppCompatActivity {

    private List<BookDTO> userBooks = Collections.synchronizedList(new ArrayList<>());
    BookDtoListAdapter adapter = new BookDtoListAdapter(userBooks);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_books_list);

        BookDtoListAdapter adapter = new BookDtoListAdapter(userBooks);
        RecyclerView recyclerView = findViewById(R.id.book_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.addNewBookButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(BooksListActivity.this, BookISBNCaptureActivity.class);
            startActivity(intent);
        });

        new Thread() {
            public void run() {
                RestTemplate restTemplate = RestUtils.getRestTemplate();
                ResponseEntity<List<BookDTO>> booksResponse = restTemplate.exchange(
                        SERVICE_URL.concat("/books"),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BookDTO>>() {
                        });
                userBooks.addAll(booksResponse.getBody());
                runOnUiThread(adapter::notifyDataSetChanged);
            }
        }.start();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

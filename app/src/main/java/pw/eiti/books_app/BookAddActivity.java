package pw.eiti.books_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.squareup.picasso.Picasso;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pw.eiti.books_app.client.RestUtils;
import pw.eiti.books_app.domain.BookDTO;

import java.util.Objects;

import static pw.eiti.books_app.client.RestUtils.SERVICE_URL;

public class BookAddActivity extends AppCompatActivity {

    ImageView bookCover;
    private TextView title;
    private TextView authors;
    private TextView numberOfPages;
    private TextView publishers;
    private TextView isbn;
    private TextView publishDate;

    private Button addNewBook;

    private BookDTO bookDTO;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BookAdd";
    private static final String BOOK = "book";
    private static final String NEW = "new";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        bookDTO = (BookDTO) Objects.requireNonNull(getIntent().getExtras()).get(BOOK);


        bookCover = findViewById(R.id.imageView);
        title = findViewById(R.id.title_field);
        authors = findViewById(R.id.author_name);
        publishDate = findViewById(R.id.publish_date);
        numberOfPages = findViewById(R.id.number_of_pages);
        publishers = findViewById(R.id.publishers);
        isbn = findViewById(R.id.isbn_number);
        addNewBook = findViewById(R.id.add_book);

        title.setText(bookDTO.getTitle());
        authors.setText(bookDTO.getAuthors());
        publishDate.setText(bookDTO.getPublishDate());
        numberOfPages.setText(bookDTO.getNumberOfPages() != null ? bookDTO.getNumberOfPages().toString() : "");
        publishers.setText(bookDTO.getPublishers());
        isbn.setText(bookDTO.getIsbn());

        Picasso.with(getApplicationContext())
                .load(bookDTO.getCoverUrl())
//                .centerCrop()
                .fit()
                .into(bookCover);

        if(getIntent().getBooleanExtra(NEW, false)) {
            addNewBook.setVisibility(View.VISIBLE);
            addNewBook.setOnClickListener((event) -> {
                new Thread(new TaskRunner2()).start();

            });
        }
        else {
            addNewBook.setVisibility(View.GONE);
        }
    }

    public class TaskRunner2 implements Runnable {

        public TaskRunner2() {
        }

        public void run() {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BookDTO> entity = new HttpEntity<>(bookDTO, requestHeaders);

            RestTemplate restTemplate = RestUtils.getRestTemplate();
            ResponseEntity<Integer> booksResponse = restTemplate.exchange(SERVICE_URL.concat("/book"), HttpMethod.POST, entity, Integer.class);
            if(booksResponse.getStatusCode() == HttpStatus.OK) {
                runOnUiThread(() -> {Snackbar.make(findViewById(android.R.id.content), "Book has been added!", Snackbar.LENGTH_INDEFINITE).show(); onBackPressed();});

            }
            else {
                runOnUiThread(() -> Snackbar.make(findViewById(android.R.id.content), "Unable to add book!", Snackbar.LENGTH_INDEFINITE).show());
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    /**
//     * Called when a view has been clicked.
//     *
//     * @param v The view that was clicked.
//     */
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.read_barcode) {
//            // launch barcode activity.
//
//        }
//
//    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BookISBNCaptureActivity.BarcodeObject);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                        CommonStatusCodes.getStatusCodeString(resultCode);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

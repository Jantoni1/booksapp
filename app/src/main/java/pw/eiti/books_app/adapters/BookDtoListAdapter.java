package pw.eiti.books_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import pw.eiti.books_app.BookAddActivity;
import pw.eiti.books_app.R;
import pw.eiti.books_app.domain.BookDTO;

import java.util.List;

public class BookDtoListAdapter extends RecyclerView.Adapter<BookDtoListAdapter.BookViewHolder> {

    private List<BookDTO> bookDTOList;

    private final String DEFAULT_COVER_URL = "http://bentwindow.com/wp-content/uploads/2018/05/Book-Placeholder.jpg";

    public BookDtoListAdapter(List<BookDTO> bookDTOList) {
        this.bookDTOList = bookDTOList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.only_book_string, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        bookViewHolder.bookDTO = bookDTOList.get(i);
        bookViewHolder.isbn.setText(bookDTOList.get(i).getTitle());
        String bookCoverUrl = bookDTOList.get(i).getCoverUrl();
        String url = !"".equals(bookCoverUrl) ? bookCoverUrl : DEFAULT_COVER_URL;
        Picasso.with(bookViewHolder.itemView.getContext())
                        .load(url)
                        .centerCrop()
                        .fit()
                        .into(bookViewHolder.bookCover);
    }

    @Override
    public int getItemCount() {
        return bookDTOList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder
    {
        BookDTO bookDTO;
        ImageView bookCover;
        TextView isbn;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.book_cover);
            isbn = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener((view) -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookAddActivity.class);
                intent.putExtra("book", bookDTO);
                context.startActivity(intent);
            });

        }
    }
}

package pw.eiti.books_app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDetailsDTO implements Serializable {

    protected String isbn;

    protected String title;

    protected String coverUrl;

    protected Integer numberOfPages;

    protected String authors;

    protected String publishDate;

    protected String publishers;

    protected String description;

    protected Date obtainDate;

    public BookDetailsDTO(BookDetailsDTO other) {
        this.isbn = other.isbn;
        this.title = other.title;
        this.coverUrl = other.coverUrl;
        this.numberOfPages = other.numberOfPages;
        this.authors = other.authors;
        this.publishDate = other.publishDate;
        this.publishers = other.publishers;
        this.description = other.description;
        this.obtainDate = other.obtainDate;
    }

}

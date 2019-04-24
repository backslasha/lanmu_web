package lanmu.entity.card;

import java.time.LocalDateTime;

import lanmu.entity.db.Book;
import lanmu.entity.db.User;

public class BookPostCard {
    private Book book;
    private User creator;
    private LocalDateTime createDate;
    private String content;
    private String images;

    public Book getBook() {
        return book;
    }

    public BookPostCard(Book book, User creator, LocalDateTime createDate, String content, String images) {
        this.book = book;
        this.creator = creator;
        this.createDate = createDate;
        this.content = content;
        this.images = images;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


}

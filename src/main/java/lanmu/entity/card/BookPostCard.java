package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

import lanmu.entity.db.BookPost;
import lanmu.entity.db.User;

public class BookPostCard {

    @Expose
    private BookCard book;
    @Expose
    private User creator;
    @Expose
    private LocalDateTime createDate;
    @Expose
    private String content;
    @Expose
    private String images;
    @Expose
    private long id;
    @Expose
    private int commentCount = 999;

    public BookPostCard(BookPost bookPost) {
        this.book = new BookCard(bookPost.getBook());
        this.creator = bookPost.getCreator();
        this.createDate = bookPost.getCreateDate();
        this.content = bookPost.getContent();
        this.images = bookPost.getImages();
        this.id = bookPost.getId();
    }

    public BookPostCard(BookCard book, User creator, LocalDateTime createDate, String content, String images) {
        this.book = book;
        this.creator = creator;
        this.createDate = createDate;
        this.content = content;
        this.images = images;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BookCard getBook() {
        return book;
    }

    public void setBook(BookCard book) {
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

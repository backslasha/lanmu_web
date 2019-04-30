package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

import lanmu.entity.db.BookPost;

public class BookPostCard {

    @Expose
    private BookCard book;

    @Expose
    private UserCard creator;

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
        this.creator = new UserCard(bookPost.getCreator());
        this.createDate = bookPost.getCreateDate();
        this.content = bookPost.getContent();
        this.images = bookPost.getImages();
        this.id = bookPost.getId();
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

    public UserCard getCreator() {
        return creator;
    }

    public void setCreator(UserCard creator) {
        this.creator = creator;
    }
}

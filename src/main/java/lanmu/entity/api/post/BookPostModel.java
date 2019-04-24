package lanmu.entity.api.post;

import lanmu.entity.db.Book;

public class BookPostModel {
    private Book book;
    private long createId;
    private String content;
    private String images;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
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

    public BookPostModel(Book book, long createId, String content, String images) {
        this.book = book;
        this.createId = createId;
        this.content = content;
        this.images = images;
    }
}

package lanmu.entity.db;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.ws.rs.ext.ParamConverter;

@Entity
@Table(name = "tb_book_post")
public class BookPost {

    public BookPost(Book book, User creator, String content, String images) {
        this.book = book;
        this.creator = creator;
        this.content = content;
        this.images = images;
    }

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, insertable = false, nullable = false)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bookId")
    @Cascade(CascadeType.ALL)
    private Book book;

    @Column(updatable = false, insertable = false, nullable = false)
    private long bookId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creatorId")
    private User creator;

    @Column(updatable = false, insertable = false, nullable = false)
    private long creatorId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate = LocalDateTime.now();

    @Column
    private String content;

    @Column(length = 2550)
    private String images;

    public BookPost() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }


    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}

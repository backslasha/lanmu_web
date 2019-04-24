package lanmu.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tb_book")
public class Book {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, nullable = false, insertable = false)
    private long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false, length = 128)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private java.sql.Timestamp publishDate;

    @Column(length = 128)
    private String version;

    @Column(length = 128)
    private String languish;

    @Column
    private String coverUrl;

    @Column
    private String introduction;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    public java.sql.Timestamp getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(java.sql.Timestamp publishDate) {
        this.publishDate = publishDate;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getLanguish() {
        return languish;
    }

    public void setLanguish(String languish) {
        this.languish = languish;
    }


    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }


    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

}

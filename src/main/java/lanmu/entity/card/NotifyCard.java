package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;

import lanmu.entity.db.BookPost;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;

public class NotifyCard {

    public static final int TYPE_NEW_COMMENT = 1;
    public static final int TYPE_NEW_REPLY = 2;

    @Expose
    private int type;

    @Expose
    private long postId;

    @Expose
    private long id;

    @Expose
    private UserCard from;
    @Expose
    private LocalDateTime time;
    @Expose
    private String content1;
    @Expose
    private String content2;
    @Expose
    private String cover;
    @Expose
    private BookCard book;

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public NotifyCard(Comment comment) {
        this.type = TYPE_NEW_COMMENT;
        this.postId = comment.getPostId();
        this.id = comment.getId();
        this.from = new UserCard(comment.getFrom());
        this.content1 = comment.getContent();
        BookPost bookPost = comment.getBookPost();
        String[] split = bookPost.getImages().split(";");
        this.content2 = bookPost.getContent();
        this.cover = split.length > 0 ? split[0] : "";
        this.time = comment.getTime();
        this.book = new BookCard(bookPost.getBook());
    }

    public NotifyCard(CommentReply reply) {
        this.type = TYPE_NEW_REPLY;
        this.postId = reply.getComment().getPostId();
        this.id = reply.getId();
        this.from = new UserCard(reply.getFrom());
        this.content1 = reply.getContent();
        BookPost bookPost = reply.getComment().getBookPost();
        this.content2 = bookPost.getContent();
        String[] split = bookPost.getImages().split(";");
        this.cover = split.length > 0 ? split[0] : "";
        this.time = reply.getTime();
        this.book = new BookCard(bookPost.getBook());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserCard getFrom() {
        return from;
    }

    public void setFrom(UserCard from) {
        this.from = from;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public BookCard getBook() {
        return book;
    }

    public void setBook(BookCard book) {
        this.book = book;
    }


}

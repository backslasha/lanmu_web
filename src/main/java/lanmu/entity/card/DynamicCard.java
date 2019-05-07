package lanmu.entity.card;

import com.google.gson.annotations.Expose;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

import lanmu.entity.db.BookPost;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.entity.db.ThumbsUp;


public class DynamicCard implements Serializable {

    public static final int TYPE_CREATE_POST = 1; // 创建帖子
    public static final int TYPE_COMMENT = 2; // 回复帖子
    public static final int TYPE_COMMENT_REPLY = 3; // 回复评论（回复评论+回复评论下他人的评论）
    public static final int TYPE_THUMB_UP = 4; // 点赞评论
    @Expose
    @Nullable
    private LocalDateTime time;
    @Expose
    private int type;
    @Expose
    private long postId;
    @Expose
    private long id;
    @Expose
    private UserCard to;
    @Expose
    private String content1;
    @Expose
    private String content2;
    @Expose
    private String cover;
    @Expose
    private BookCard book;

    public DynamicCard(BookPost bookPost) {
        this.type = TYPE_CREATE_POST;
        this.postId = bookPost.getId();
        this.id = bookPost.getId();
        this.to = new UserCard(bookPost.getCreator());
        this.content1 = bookPost.getContent();
        String[] split = bookPost.getImages().split(";");
        this.content2 = null;
        this.cover = split.length > 0 ? split[0] : "";
        this.time = bookPost.getCreateDate();
        this.book = new BookCard(bookPost.getBook());
    }

    public DynamicCard(ThumbsUp thumbsUp) {
        this.type = TYPE_THUMB_UP;
        Comment comment = thumbsUp.getComment();
        this.postId = comment.getPostId();
        this.id = thumbsUp.getId();
        this.to = new UserCard(comment.getFrom());
        this.content1 = "点赞了你的评论";
        BookPost bookPost = comment.getBookPost();
        String[] split = bookPost.getImages().split(";");
        this.content2 = comment.getContent();
        this.cover = split.length > 0 ? split[0] : "";
        this.time = thumbsUp.getTime();
        this.book = new BookCard(bookPost.getBook());
    }

    public DynamicCard(Comment comment) {
        this.type = TYPE_COMMENT;
        this.postId = comment.getPostId();
        this.id = comment.getId();
        this.content1 = comment.getContent();
        BookPost bookPost = comment.getBookPost();
        this.to = new UserCard(bookPost.getCreator());
        String[] split = bookPost.getImages().split(";");
        this.content2 = bookPost.getContent();
        this.cover = split.length > 0 ? split[0] : "";
        this.time = comment.getTime();
        this.book = new BookCard(bookPost.getBook());
    }

    public DynamicCard(CommentReply reply) {
        this.type = TYPE_COMMENT_REPLY;
        this.postId = reply.getComment().getPostId();
        this.id = reply.getId();
        if (reply.getTo() == null) {
            this.to = new UserCard(reply.getComment().getFrom());
        } else {
            this.to = new UserCard(reply.getTo());
        }
        this.content1 = reply.getContent();
        BookPost bookPost = reply.getComment().getBookPost();
        this.content2 = reply.getComment().getContent();
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

    public UserCard getTo() {
        return to;
    }

    public void setTo(UserCard to) {
        this.to = to;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":")
                .append(type);
        sb.append(",\"postId\":")
                .append(postId);
        sb.append(",\"id\":")
                .append(id);
        sb.append(",\"from\":")
                .append(to);
        sb.append(",\"time\":\"")
                .append(time).append('\"');
        sb.append(",\"content1\":\"")
                .append(content1).append('\"');
        sb.append(",\"content2\":\"")
                .append(content2).append('\"');
        sb.append(",\"cover\":\"")
                .append(cover).append('\"');
        sb.append(",\"book\":")
                .append(book);
        sb.append('}');
        return sb.toString();
    }

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

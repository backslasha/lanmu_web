package lanmu.entity.card;


import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;

import lanmu.entity.db.Comment;


public class CommentCard {
    @Expose
    private long id;

    @Expose
    private long postId;

    @Expose
    private UserCard from;

    @Expose
    private long fromId;

    @Expose
    private String content;

    @Expose
    private LocalDateTime time;

    public CommentCard(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPostId();
        this.from = new UserCard(comment.getFrom());
        this.fromId = comment.getFromId();
        this.content = comment.getContent();
        this.time = comment.getTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }


    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}

package lanmu.entity.card;


import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;


public class CommentReplyCard {

    @Expose
    private long id;

    @Expose
    private UserCard from;

    @Expose
    private long fromId;

    @Expose
    private CommentCard comment;

    @Expose
    private long commentId;

    @Expose
    private UserCard to;

    @Expose
    private long toId;

    @Expose
    private String content;

    @Expose
    private LocalDateTime time;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }


    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }


    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
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

    public CommentCard getComment() {
        return comment;
    }

    public void setComment(CommentCard comment) {
        this.comment = comment;
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
}

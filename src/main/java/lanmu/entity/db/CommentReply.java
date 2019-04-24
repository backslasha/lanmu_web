package lanmu.entity.db;


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

@Entity
@Table(name = "tb_comment_reply")
public class CommentReply {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, insertable = false, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fromId")
    private User from;

    @Column(updatable = false, insertable = false, nullable = false)
    private long fromId;

    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;

    @Column(updatable = false, insertable = false, nullable = false)
    private long commentId;


    @ManyToOne
    @JoinColumn(name = "toId")
    private User to;

    @Column(updatable = false, insertable = false, nullable = false)
    private long toId;

    @Column
    private String content;

    @CreationTimestamp
    @Column(nullable = false)
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


    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

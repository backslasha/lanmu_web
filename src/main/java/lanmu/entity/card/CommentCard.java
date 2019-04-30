package lanmu.entity.card;


import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;


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

    public List<CommentReplyCard> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentReplyCard> replies) {
        this.replies = replies;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Expose
    private List<CommentReplyCard> replies;

    @Expose
    private int replyCount;

    public CommentCard(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPostId();
        this.from = new UserCard(comment.getFrom());
        this.fromId = comment.getFromId();
        this.content = comment.getContent();
        this.time = comment.getTime();
        List<CommentReplyCard> replyCards = new ArrayList<>();
        List<CommentReply> replies = comment.getReplies();
        if (replies==null) replies = new ArrayList<>();
        for (int i = 0; i < Math.min(2, replies.size()); i++) {
            replyCards.add(new CommentReplyCard(replies.get(i)));
        }
        this.replies = replyCards;
        this.replyCount = replies.size();
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

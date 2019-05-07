package lanmu.entity.card;


import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;


public class CommentCard {

    public static final int ORDER_TIME_CLOSEST_FIRST = 0;
    public static final int ORDER_TIME_REMOTEST_FIRST = 1;
    public static final int ORDER_COMMENT_THUMBS_UP_FIRST = 2;
    public static final int ORDER_DEFAULT = ORDER_TIME_CLOSEST_FIRST;
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
    @Expose
    private int thumbsUpCount;
    @Expose
    private boolean thumbsUp;

    public boolean getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(boolean thumbsUp) {
        this.thumbsUp = thumbsUp;
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

    public int getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

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

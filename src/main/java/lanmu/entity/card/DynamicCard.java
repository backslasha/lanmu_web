package lanmu.entity.card;

import com.google.gson.annotations.Expose;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;


public class DynamicCard implements Serializable {

    public static final int TYPE_CREATE_POST = 1; // 创建帖子
    public static final int TYPE_COMMENT = 2; // 回复帖子
    public static final int TYPE_COMMENT_REPLY = 3; // 回复评论（回复评论+回复评论下他人的评论）
    public static final int TYPE_THUMB_UP = 4; // 点赞评论


    @Expose
    private int type;

    @Expose
    private BookPostCard bookPostCard;

    @Expose
    @Nullable
    private CommentCard commentCard;

    @Expose
    @Nullable
    private CommentReplyCard commentReplyCard;

    public static DynamicCard wrap(BookPostCard postCard) {
        DynamicCard dynamicCard = new DynamicCard();
        dynamicCard.setBookPostCard(postCard);
        dynamicCard.setType(TYPE_CREATE_POST);
        return dynamicCard;
    }

    public static DynamicCard wrap(CommentCard card) {
        DynamicCard dynamicCard = new DynamicCard();
        dynamicCard.setCommentCard(card);
        dynamicCard.setType(TYPE_COMMENT);
        return dynamicCard;
    }

    public static DynamicCard wrap(CommentReplyCard card) {
        DynamicCard dynamicCard = new DynamicCard();
        dynamicCard.setCommentReplyCard(card);
        dynamicCard.setType(TYPE_COMMENT_REPLY);
        return dynamicCard;
    }

    public BookPostCard getBookPostCard() {
        return bookPostCard;
    }

    public void setBookPostCard(BookPostCard bookPostCard) {
        this.bookPostCard = bookPostCard;
    }

    @Nullable
    public CommentCard getCommentCard() {
        return commentCard;
    }

    public void setCommentCard(@Nullable CommentCard commentCard) {
        this.commentCard = commentCard;
    }

    @Nullable
    public CommentReplyCard getCommentReplyCard() {
        return commentReplyCard;
    }

    public void setCommentReplyCard(@Nullable CommentReplyCard commentReplyCard) {
        this.commentReplyCard = commentReplyCard;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        switch (type) {
            case TYPE_CREATE_POST:
                return bookPostCard.getCreateDate();
            case TYPE_COMMENT:
                return commentCard.getTime();
            case TYPE_COMMENT_REPLY:
                return commentReplyCard.getTime();
            case TYPE_THUMB_UP:
                return commentCard.getTime();
        }
        return LocalDateTime.now();
    }
}

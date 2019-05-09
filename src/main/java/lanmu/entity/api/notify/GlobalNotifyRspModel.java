package lanmu.entity.api.notify;

import com.google.gson.annotations.Expose;

public class GlobalNotifyRspModel {
    @Expose
    private int applyCount;
    @Expose
    private int messageCount;
    @Expose
    private int commentCount;
    @Expose
    private int thumbsUpCount;

    public GlobalNotifyRspModel(int applyCount, int messageCount, int commentCount, int thumbsUpCount) {
        this.applyCount = applyCount;
        this.messageCount = messageCount;
        this.commentCount = commentCount;
        this.thumbsUpCount = thumbsUpCount;
    }

    @Override
    public String toString() {
        return "{" + "\"applyCount\":" +
                applyCount +
                ",\"messageCount\":" +
                messageCount +
                ",\"commentCount\":" +
                commentCount +
                ",\"thumbsUpCount\":" +
                thumbsUpCount +
                '}';
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }
}

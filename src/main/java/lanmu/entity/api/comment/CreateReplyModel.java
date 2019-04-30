package lanmu.entity.api.comment;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class CreateReplyModel {
    @Expose
    private long commentId;
    @Expose
    private long toId;
    @Expose
    private long fromId;
    @Expose
    private String content;

    public static boolean check(CreateReplyModel model) {
        return !Strings.isNullOrEmpty(model.content);
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
}

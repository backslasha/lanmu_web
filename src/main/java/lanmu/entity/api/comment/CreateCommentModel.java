package lanmu.entity.api.comment;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class CreateCommentModel {
    @Expose
    private long postId;
    @Expose
    private long fromId;
    @Expose
    private String content;

    public static boolean check(CreateCommentModel model) {
        return !Strings.isNullOrEmpty(model.content);
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
}

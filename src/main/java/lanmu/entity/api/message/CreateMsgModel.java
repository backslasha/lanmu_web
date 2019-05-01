package lanmu.entity.api.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class CreateMsgModel {

    @Expose
    private int type;
    @Expose
    private String content;
    @Expose
    private long fromId;
    @Expose
    private long toId;

    public static boolean check(CreateMsgModel model) {
        return !(model == null || Strings.isNullOrEmpty(model.content));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}

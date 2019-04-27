package lanmu.entity.api.post;

import com.google.gson.annotations.Expose;

/**
 * 搜索书帖时需要的信息
 */
public class SearchPostModel {

    public static final int TYPE_KEYWORD = 0;
    public static final int TYPE_POST_ID = 1;
    public static final int TYPE_CREATOR_ID = 2;

    @Expose
    private String value;
    @Expose
    private int type;

    public SearchPostModel(String value, int type) {
        this.value = value;
        this.type = type;
    }

    public SearchPostModel() {

    }

    public static int getTypeKeyword() {
        return TYPE_KEYWORD;
    }

    public static int getTypePostId() {
        return TYPE_POST_ID;
    }

    public static int getTypeCreatorId() {
        return TYPE_CREATOR_ID;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

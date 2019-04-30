package lanmu.entity.api.base;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime;

import lanmu.entity.card.BookPostCard;
import lanmu.entity.card.UserCard;
import lanmu.entity.db.BookPost;

public class ResponseModel<M> implements Serializable {
    // 成功
    public static final int SUCCEED = 1;
    // 未知错误
    public static final int ERROR_UNKNOWN = 0;

    // 没有找到用户信息
    public static final int ERROR_NOT_FOUND_USER = 4041;
    // 没有找到帖子
    public static final int ERROR_NOT_FOUND_POST = 4042;
    // 没有评论
    public static final int ERROR_NOT_FOUND_COMMENT = 4043;

    // 创建书帖
    public static final int ERROR_CREATE_POST = 3001;
    // 创建评论失败
    public static final int ERROR_CREATE_COMMENT = 3002;
    // 创建回复失败
    public static final int ERROR_CREATE_REPLY = 3003;

    public static final int ERROR_UPDATE_USER_INFO= 3004;

    // 请求参数错误
    public static final int ERROR_PARAMETERS = 4001;
    // 请求参数错误-已存在账户
    public static final int ERROR_PARAMETERS_EXIST_ACCOUNT = 4002;
    // 请求参数错误-已存在名称
    public static final int ERROR_PARAMETERS_EXIST_NAME = 4003;

    // 服务器错误
    public static final int ERROR_SERVICE = 5001;

    // 账户Token错误，需要重新登录
    public static final int ERROR_ACCOUNT_TOKEN = 2001;
    // 账户登录失败
    public static final int ERROR_ACCOUNT_LOGIN = 2002;
    // 账户注册失败
    public static final int ERROR_ACCOUNT_REGISTER = 2003;
    // 没有权限操作
    public static final int ERROR_ACCOUNT_NO_PERMISSION = 2010;

    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private LocalDateTime time = LocalDateTime.now();
    @Expose
    private M result;

    public ResponseModel() {
        code = 1;
        message = "ok";
    }

    public ResponseModel(M result) {
        this();
        this.result = result;
    }

    public ResponseModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseModel(int code, String message, M result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }


    public static <M> ResponseModel<M> buildOk() {
        return new ResponseModel<M>();
    }

    public static <M> ResponseModel<M> buildOk(M result) {
        return new ResponseModel<M>(result);
    }

    public static <M> ResponseModel<M> buildParameterError() {
        return new ResponseModel<M>(ERROR_PARAMETERS, "Parameters Error.");
    }

    public static <M> ResponseModel<M> buildHaveAccountError() {
        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_ACCOUNT, "Already have this account.");
    }

    public static <M> ResponseModel<M> buildHaveNameError() {
        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_NAME, "Already have this name.");
    }

    public static <M> ResponseModel<M> buildServiceError() {
        return new ResponseModel<M>(ERROR_SERVICE, "Service Error.");
    }

    public static <M> ResponseModel<M> buildNotFoundUserError(String str) {
        return new ResponseModel<M>(ERROR_NOT_FOUND_USER, str != null ? str : "Not Found User.");
    }

    public static <M> ResponseModel<M> buildNotFoundPostError() {
        return new ResponseModel<M>(ERROR_NOT_FOUND_POST, "Not Found Post.");
    }
    public static <M> ResponseModel<M> buildNotFoundCommentError() {
        return new ResponseModel<M>(ERROR_NOT_FOUND_COMMENT, "Not Found Comment.");
    }

    public static <M> ResponseModel<M> buildAccountError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_TOKEN, "Account Error; you need login.");
    }

    public static <M> ResponseModel<M> buildLoginError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_LOGIN, "Account or password error.");
    }

    public static <M> ResponseModel<M> buildRegisterError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_REGISTER, "Have this account.");
    }

    public static <M> ResponseModel<M> buildNoPermissionError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_NO_PERMISSION, "You do not have permission to operate.");
    }

    public static <M> ResponseModel<M> buildUpdateError(int type) {
        return new ResponseModel<M>(type, "Update failed.");
    }

    public static <M> ResponseModel<M> buildCreateError(int type) {
        return new ResponseModel<M>(type, "Create failed.");
    }

    public static <M> ResponseModel<M> buildCreateError(int type, M result) {
        return new ResponseModel<M>(type, "Create failed, the unique target already exits.", result);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSucceed() {
        return code == SUCCEED;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public M getResult() {
        return result;
    }

    public void setResult(M result) {
        this.result = result;
    }

}
package lanmu.entity.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class LoginModel {
    @Expose
    private String account;
    @Expose
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean check(LoginModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.account)
                && !Strings.isNullOrEmpty(model.password);
    }
}

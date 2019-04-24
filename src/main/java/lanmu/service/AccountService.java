package lanmu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lanmu.entity.api.account.AccountRspModel;
import lanmu.entity.api.account.LoginModel;
import lanmu.entity.api.account.RegisterModel;
import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.db.User;
import lanmu.factory.UserFactory;

// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService extends BaseService {


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel loginModel) {
        if (!LoginModel.check(loginModel)) {
            return ResponseModel.buildParameterError(); // 参数异常
        }

        User user = UserFactory.login(
                loginModel.getAccount(),
                loginModel.getPassword()
        );

        if (user != null) {
            return ResponseModel.buildOk(new AccountRspModel(user));
        } else {
            return ResponseModel.buildLoginError();
        }

    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError(); // 参数异常
        }

        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            return ResponseModel.buildHaveAccountError(); // 已有账户
        }

        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            return ResponseModel.buildHaveNameError(); // 已有用户名
        }

        // 注册逻辑开始
        user = UserFactory.register(
                model.getAccount(),
                model.getPassword(),
                model.getName()
        );

        if (user != null) {
            AccountRspModel rspModel = new AccountRspModel(user); // 返回当前的账户
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildRegisterError(); // 注册异常
        }
    }


}
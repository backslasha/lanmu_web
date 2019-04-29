package lanmu.service;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lanmu.entity.api.account.AccountRspModel;
import lanmu.entity.api.account.LoginModel;
import lanmu.entity.api.account.RegisterModel;
import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.card.BookPostCard;
import lanmu.entity.card.CommentCard;
import lanmu.entity.card.CommentReplyCard;
import lanmu.entity.card.DynamicCard;
import lanmu.entity.card.UserCard;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.entity.db.User;
import lanmu.factory.BookPostFactory;
import lanmu.factory.UserFactory;
import lanmu.utils.Hib;

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

    @POST
    @Path("/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> updateUserInfo(UserCard card) {
        if (!(getSelf().getId() == card.getId())) {
            return ResponseModel.buildNoPermissionError();
        }
        User userInDb = UserFactory.findById(card.getId());
        if (userInDb == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        return Hib.query(session -> {
            String name = card.getName();
            String avatarUrl = card.getAvatarUrl();
            String introduction = card.getIntroduction();
            String gender = card.getGender();
            String phone = card.getPhone();
            if (!Strings.isNullOrEmpty(name)) userInDb.setName(name);
            if (!Strings.isNullOrEmpty(avatarUrl)) userInDb.setAvatarUrl(avatarUrl);
            if (!Strings.isNullOrEmpty(introduction)) userInDb.setIntroduction(introduction);
            if (!Strings.isNullOrEmpty(gender)) userInDb.setGender(gender);
            if (!Strings.isNullOrEmpty(phone)) userInDb.setPhone(phone);
            try {
                session.saveOrUpdate(userInDb);
                return ResponseModel.buildOk(new UserCard(userInDb));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseModel.buildUpdateError(ResponseModel.ERROR_UPDATE_USER_INFO);
        });
    }


    @GET
    @Path("/dynamic/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<DynamicCard>> searchDynamics(@PathParam("id") long id) {
        if (null == UserFactory.findById(id)) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        // 创建过的帖子
        List<BookPostCard> bookPostCards = BookPostFactory.queryBookPostsByCreatorId(id);
        // 回复帖子
        List<CommentCard> commentCards = new ArrayList<>();
        // 回复评论（回复评论+回复评论下他人的评论）
        List<CommentReplyCard> commentReplyCards = new ArrayList<>();
        // 点赞评论
        List<DynamicCard> dynamics = new ArrayList<>();
        for (BookPostCard card : bookPostCards) {
            dynamics.add(DynamicCard.wrap(card));
        }
        for (CommentCard card : commentCards) {
            dynamics.add(DynamicCard.wrap(card));
        }
        for (CommentReplyCard card : commentReplyCards) {
            dynamics.add(DynamicCard.wrap(card));
        }
        dynamics.sort((o1, o2) -> {
            if (o1.getDate().isBefore(o2.getDate()))
                return 1;
            if (o1.getDate().isAfter(o2.getDate()))
                return -1;
            return 0;
        });
        return ResponseModel.buildOk(dynamics);
    }

}
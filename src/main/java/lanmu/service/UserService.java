package lanmu.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.card.ApplyCard;
import lanmu.entity.card.UserCard;
import lanmu.entity.db.Apply;
import lanmu.entity.db.User;
import lanmu.entity.db.UserFollow;
import lanmu.factory.ApplyFactory;
import lanmu.factory.UserFactory;
import lanmu.utils.Hib;

@Path("user/")
public class UserService extends BaseService {

    @GET
    @Path("/{userId}/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> searchUserInfo(@PathParam("userId") long userId) {
        User userInDb = UserFactory.findById(userId);
        if (userInDb == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        final boolean isFriend
                = UserFactory.isFriend(userId, getSelf().getId());
        return ResponseModel.buildOk(new UserCard(userInDb, isFriend));
    }


    // 拉取联系人
    @GET
    @Path("/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact(@QueryParam("userId")
                                                         long userId) {
        User user = UserFactory.findById(userId);
        if (null == user) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        List<User> users = UserFactory.contacts(userId);
        if (users != null) {
            return ResponseModel.buildOk(users.stream()
                    .map(UserCard::new)
                    .collect(Collectors.toList()));
        }
        return ResponseModel.buildNotFoundContactError();
    }

    @GET
    @Path("/add_friend/{toId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> createUserFollow(@QueryParam("fromId") long fromId,
                                                    @PathParam("toId") long toId) {
        User from = UserFactory.findById(fromId);
        User to = UserFactory.findById(toId);
        if (from == null || to == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        UserFollow userFollow = UserFactory.addFriend(from, to);
        if (null != userFollow) {
            Hib.queryOnly(session -> {
                session.createQuery("update Apply set handle=1 where fromId=:toId and toId=:fromId")
                        .setParameter("fromId", fromId)
                        .setParameter("toId", toId)
                        .executeUpdate();
            });
            return ResponseModel.buildOk(new UserCard(to, true));
        }
        return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_FOLLOW);
    }


    @GET
    @Path("/apply/{toId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel sendApply(@QueryParam("fromId") long fromId,
                                   @PathParam("toId") long toId) {
        User from = UserFactory.findById(fromId);
        User to = UserFactory.findById(toId);
        if (from == null || to == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        Apply apply = UserFactory.createApply(from, to);
        if (null != apply) {
            return ResponseModel.buildOk();
        }
        return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_APPLY);
    }


    @GET
    @Path("/applies")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<ApplyCard>> pullApplies(@QueryParam("userId") long userId) {
        User user = UserFactory.findById(userId);
        if (user == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        List<Apply> applies = ApplyFactory.searchApplies(userId);
        if (applies != null) {
            Collections.reverse(applies);
            return ResponseModel.buildOk(applies.stream().map(ApplyCard::new)
                    .collect(Collectors.toList()));
        }
        return ResponseModel.buildNotFoundApplyError();
    }

    @POST
    @Path("/apply/reject/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<ApplyCard> rejectApply(long applyId) {
        Apply apply = ApplyFactory.findById(applyId);
        if (apply == null) {
            return ResponseModel.buildNotFoundApplyError();
        }
        Apply committed = Hib.query(session -> {
            apply.setHandle(-1);
            session.saveOrUpdate(apply);
            return apply;
        });

        if (committed != null) {
            return ResponseModel.buildOk(new ApplyCard(committed));
        }
        return ResponseModel.buildUpdateError(ResponseModel.ERROR_UPDATE_APPLY);
    }

}

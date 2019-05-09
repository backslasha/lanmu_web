package lanmu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import lanmu.entity.api.notify.GlobalNotifyRspModel;
import lanmu.entity.card.ApplyCard;
import lanmu.entity.card.DynamicCard;
import lanmu.entity.card.UserCard;
import lanmu.entity.db.Apply;
import lanmu.entity.db.BookPost;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.entity.db.ThumbsUp;
import lanmu.entity.db.User;
import lanmu.entity.db.UserFollow;
import lanmu.factory.ApplyFactory;
import lanmu.factory.BookPostFactory;
import lanmu.factory.CommentFactory;
import lanmu.factory.MessageFactory;
import lanmu.factory.ThumbsUpFactory;
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
        List<Apply> applies = ApplyFactory.pullApplies(userId);
        ApplyFactory.markAppliesReceived(userId);
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


    @GET
    @Path("/dynamic/posts/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<DynamicCard>> pullPostsDynamics(@PathParam("userId") long userId) {
        if (null == UserFactory.findById(userId)) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        // 创建过的帖子
        List<DynamicCard> dynamics = new ArrayList<>();
        List<BookPost> bookPosts = BookPostFactory.queryUserPostsByMonth(userId, 0);
        for (BookPost card : bookPosts) {
            dynamics.add(new DynamicCard(card));
        }
        dynamics.sort(tComparator);
        return ResponseModel.buildOk(dynamics);
    }

    @GET
    @Path("/dynamic/comments/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<DynamicCard>> pullCommentsDynamics(@PathParam("userId") long userId) {
        if (null == UserFactory.findById(userId)) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        // 创建过的帖子
        List<DynamicCard> dynamics = new ArrayList<>();
        // 回复帖子
        List<Comment> comments = CommentFactory.queryUserCommentsByMonth(userId, 0);
        for (Comment card : comments) {
            dynamics.add(new DynamicCard(card));
        }
        dynamics.sort(tComparator);
        return ResponseModel.buildOk(dynamics);

    }

    @GET
    @Path("/dynamic/replies/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<DynamicCard>> pullRepliesDynamics(@PathParam("userId") long userId) {
        if (null == UserFactory.findById(userId)) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        // 创建过的帖子
        List<DynamicCard> dynamics = new ArrayList<>();
        // 回复评论（回复评论+回复评论下他人的评论）
        List<CommentReply> replies = CommentFactory.queryUserRepliesByMonth(userId, 0);
        for (CommentReply card : replies) {
            dynamics.add(new DynamicCard(card));
        }
        dynamics.sort(tComparator);
        return ResponseModel.buildOk(dynamics);
    }

    @GET
    @Path("/dynamic/thumbsup/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<DynamicCard>> pullThumbsUpDynamics(@PathParam("userId") long userId) {
        if (null == UserFactory.findById(userId)) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        // 创建过的帖子
        List<DynamicCard> dynamics = new ArrayList<>();
        // 点赞
        List<ThumbsUp> thumbsUps = ThumbsUpFactory.queryUserThumbsUpByMonth(userId, 0);
        for (ThumbsUp thumbsUp : thumbsUps) {
            dynamics.add(new DynamicCard(thumbsUp));
        }
        dynamics.sort(tComparator);
        return ResponseModel.buildOk(dynamics);
    }

    private Comparator<DynamicCard> tComparator = (o1, o2) -> {
        if (o1.getTime().isBefore(o2.getTime()))
            return 1;
        if (o1.getTime().isAfter(o2.getTime()))
            return -1;
        return 0;
    };

    @GET
    @Path("/notify_count/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GlobalNotifyRspModel> pullGlobalNotifyCount(@PathParam("userId") long userId) {
        if (null == UserFactory.findById(userId)) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        GlobalNotifyRspModel committed = Hib.query(session -> {
            final int applyCount = ApplyFactory.countUnReceivedApplyOf(userId);
            final int messageCount = MessageFactory.countUnreadMessageOf(userId);
            final int commentCount =
                    CommentFactory.countUnreadCommentOf(userId) + CommentFactory.countUnreadReplyOf(userId);
            final int thumbsUpCount = ThumbsUpFactory.countUnreadThumbsUpOf(userId);
            return new GlobalNotifyRspModel(applyCount, messageCount, commentCount, thumbsUpCount);
        });

        if (committed != null)
            return ResponseModel.buildOk(committed);

        return ResponseModel.buildNotFoundGlobalNotifyError();
    }

}

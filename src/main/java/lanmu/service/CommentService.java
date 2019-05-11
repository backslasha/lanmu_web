package lanmu.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import lanmu.entity.api.base.PageModel;
import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.api.comment.CreateCommentModel;
import lanmu.entity.api.comment.CreateReplyModel;
import lanmu.entity.api.comment.NotifyRspModel;
import lanmu.entity.card.CommentCard;
import lanmu.entity.card.CommentReplyCard;
import lanmu.entity.card.NotifyCard;
import lanmu.entity.db.BookPost;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.entity.db.ThumbsUp;
import lanmu.entity.db.User;
import lanmu.factory.BookPostFactory;
import lanmu.factory.CommentFactory;
import lanmu.factory.ThumbsUpFactory;
import lanmu.factory.UserFactory;
import lanmu.utils.Hib;

@Path("comment/")
public class CommentService extends BaseService {

    @POST
    @Path("create/")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseModel<CommentCard> createComment(CreateCommentModel model) {
        if (!CreateCommentModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User from = UserFactory.findById(model.getFromId());
        if (from == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        BookPost bookPost = BookPostFactory.findById(model.getPostId());
        if (bookPost == null) {
            return ResponseModel.buildNotFoundPostError();
        }

        Comment committed = Hib.query(session -> {
            Comment comment = new Comment();
            comment.setContent(model.getContent());
            comment.setBookPost(bookPost);
            comment.setPostId(model.getPostId());
            comment.setFromId(model.getFromId());
            comment.setFrom(from);
            session.saveOrUpdate(comment);
            return comment;
        });

        if (committed != null) {
            return ResponseModel.buildOk(new CommentCard(committed));
        }

        return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_COMMENT);
    }


    @GET
    @Path("{postId}/")
    @Produces("application/json")
    public ResponseModel<PageModel<CommentCard>> pullComments(@PathParam("postId") long postId,
                                                              @QueryParam("order") int order,
                                                              @QueryParam("page") int page) {
        BookPost bookPost = BookPostFactory.findById(postId);
        if (bookPost == null) {
            return ResponseModel.buildNotFoundPostError();
        }
        List<CommentCard> cards
                = CommentFactory.queryPostComments(postId, getSelf().getId(), order, page);
        if (cards == null) {
            return ResponseModel.buildNotFoundCommentError();
        } else {
            return ResponseModel.buildOk(new PageModel<>(
                    cards, page, CommentFactory.queryPostCommentCount(postId),
                    cards.size() < CommentFactory.ITEM_COUNT_PER_PAGE));
        }
    }


    @GET
    @Path("resemble/{userId}/")
    @Produces("application/json")
    public ResponseModel<NotifyRspModel> pullNotifies(@PathParam("userId") long userId) {
        User user = UserFactory.findById(userId);
        if (user == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        List<Comment> comments
                = CommentFactory.queryUserReceivedComments(userId);
        List<CommentReply> replies
                = CommentFactory.queryUserReceivedReplies(userId);
        List<ThumbsUp> thumbsUps
                = ThumbsUpFactory.queryUserReceivedThumbsUp(userId);

        if (thumbsUps == null || replies == null || comments == null) {
            return ResponseModel.buildNotFoundCommentError();
        }

        int unread = CommentFactory.markCommentsReceived(userId);
        unread += CommentFactory.markRepliesReceived(userId);
        unread += ThumbsUpFactory.markThumbsUpsReceived(userId);

        List<NotifyCard> notifyCards = comments.stream()
                .map(NotifyCard::new)
                .collect(Collectors.toList());
        notifyCards.addAll(replies.stream()
                .map(NotifyCard::new)
                .collect(Collectors.toList())
        );
        notifyCards.addAll(thumbsUps.stream()
                .map(NotifyCard::new)
                .collect(Collectors.toList())
        );
        notifyCards.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));

        return ResponseModel.buildOk(new NotifyRspModel(unread, notifyCards));
    }


    @POST
    @Path("reply/create/")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseModel<CommentReplyCard> createReply(CreateReplyModel model) {
        if (!CreateReplyModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User from = UserFactory.findById(model.getFromId());
        User to = UserFactory.findById(model.getToId());
        if (from == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        Comment comment = CommentFactory.findById(model.getCommentId());
        if (comment == null) {
            return ResponseModel.buildNotFoundCommentError();
        }
        CommentReply committed = Hib.query(session -> {
            CommentReply reply = new CommentReply();
            reply.setContent(model.getContent());
            reply.setFromId(model.getFromId());
            reply.setFrom(from);
            reply.setToId(model.getToId());
            reply.setTo(to);
            reply.setCommentId(model.getCommentId());
            reply.setComment(comment);
            session.saveOrUpdate(reply);
            return reply;
        });

        if (committed != null) {
            return ResponseModel.buildOk(new CommentReplyCard(committed));
        }
        return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_REPLY);
    }

    @GET
    @Path("reply/{commentId}/")
    @Produces("application/json")
    public ResponseModel<PageModel<CommentReplyCard>> pullReplies(@PathParam("commentId") long commentId,
                                                                  @QueryParam("page") int page) {
        Comment comment = CommentFactory.findById(commentId);
        if (comment == null) {
            return ResponseModel.buildNotFoundCommentError();
        }
        List<CommentReplyCard> cards
                = CommentFactory.queryRepliesByCommentId(commentId, page);
        if (cards == null) {
            return ResponseModel.buildNotFoundCommentError();
        } else {
            return ResponseModel.buildOk(new PageModel<>(
                    cards, page, CommentFactory.queryReplyCountByCommentID(commentId),
                    cards.size() < CommentFactory.ITEM_COUNT_PER_PAGE));
        }
    }


    @GET
    @Path("thumbsup/")
    @Produces("application/json")
    public ResponseModel createThumbsUp(@QueryParam("commentId") long commentId,
                                        @QueryParam("fromId") long fromId) {

        if (fromId != getSelf().getId()) {
            return ResponseModel.buildNoPermissionError();
        }

        Comment comment = CommentFactory.findById(commentId);
        if (comment == null) {
            return ResponseModel.buildNotFoundCommentError();
        }

        User from = UserFactory.findById(fromId);
        if (from == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        ThumbsUp thumbsUp = ThumbsUpFactory.find(fromId, commentId);
        if (thumbsUp == null) {
            thumbsUp = ThumbsUpFactory.createThumbsUp(comment, from);
            if (thumbsUp != null) {
                return ResponseModel.buildOk();
            } else {
                return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_THUMBS_UP);
            }
        } else {
            thumbsUp = ThumbsUpFactory.deleteThumbsUp(thumbsUp);
            if (thumbsUp != null) {
                return ResponseModel.buildOk();
            }
            return ResponseModel.buildDeleteError(ResponseModel.ERROR_DELETE_THUMBS_UP);
        }
    }

}

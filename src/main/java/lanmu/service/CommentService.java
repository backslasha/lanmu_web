package lanmu.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.api.comment.CreateCommentModel;
import lanmu.entity.api.comment.CreateReplyModel;
import lanmu.entity.card.CommentCard;
import lanmu.entity.card.CommentReplyCard;
import lanmu.entity.db.BookPost;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.entity.db.User;
import lanmu.factory.BookPostFactory;
import lanmu.factory.CommentFactory;
import lanmu.factory.UserFactory;
import lanmu.utils.Hib;

@Path("comment/")
public class CommentService {

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
    public ResponseModel<List<CommentCard>> pullComments(@PathParam("postId") long postId) {
        BookPost bookPost = BookPostFactory.findById(postId);
        if (bookPost == null) {
            return ResponseModel.buildNotFoundPostError();
        }
        List<Comment> comments = CommentFactory.queryPostComments(postId);
        if (comments == null) {
            return ResponseModel.buildNotFoundCommentError();
        } else {
            return ResponseModel.buildOk(
                    comments.stream()
                            .map(CommentCard::new)
                            .collect(Collectors.toList()));
        }
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
        if (from == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        User to = UserFactory.findById(model.getToId());
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

}

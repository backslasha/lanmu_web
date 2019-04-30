package lanmu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.ws.Response;

import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.api.comment.CreateCommentModel;
import lanmu.entity.card.CommentCard;
import lanmu.entity.db.BookPost;
import lanmu.entity.db.Comment;
import lanmu.entity.db.User;
import lanmu.factory.BookPostFactory;
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

}

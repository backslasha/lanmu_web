package lanmu.factory;

import java.util.List;

import lanmu.entity.db.Comment;
import lanmu.utils.Hib;

public class CommentFactory {

    public static Comment findById(long id) {
        return Hib.query(session -> session.get(Comment.class, id));
    }

    public static List<Comment> queryPostComments(long postId) {
        return Hib.query(session ->
                session.createQuery("from Comment where postId=:postId", Comment.class)
                        .setParameter("postId", postId)
                        .getResultList());
    }
}

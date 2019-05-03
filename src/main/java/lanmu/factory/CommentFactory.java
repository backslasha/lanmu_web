package lanmu.factory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
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

    public static int updateCommentsReceived(long userId) {
        return Hib.query(session ->
                session.createQuery("update Comment set received=1 " +
                        "where postId in (from BookPost where creatorId = :userId) " +
                        "and fromId!=:userId and received=0")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }

    public static List<Comment> queryUserReceivedComments(long userId) {
        return Hib.query(session -> {
            List<Comment> comments = session.createQuery("from Comment where postId" +
                    " in (from BookPost where creatorId = :userId) " +
                    "and fromId!=:userId", Comment.class)
                    .setParameter("userId", userId)
                    .setMaxResults(20)
                    .getResultList();
            for (Comment comment : comments) {
                comment.getBookPost().getBook();
            }
            return comments;
        });
    }

    public static List<CommentReply> queryUserReceivedReplies(long userId) {
        return Hib.query(session -> {
            List<CommentReply> replies = session.createQuery("from CommentReply " +
                    "where " +
                    "commentId in (from Comment where fromId = :userId) " +
                    "and fromId!= :userId", CommentReply.class)
                    .setParameter("userId", userId)
                    .setMaxResults(20)
                    .getResultList();
            for (CommentReply reply : replies) {
                reply.getComment().getBookPost().getBook();
            }
            return replies;
        });
    }

    public static int updateRepliesReceived(long userId) {
        return Hib.query(session ->
                session.createQuery("update CommentReply set received=1 " +
                        "where " +
                        "commentId in (from Comment where fromId = :userId)" +
                        " and" +
                        " fromId!= :userId and received=0")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }

}

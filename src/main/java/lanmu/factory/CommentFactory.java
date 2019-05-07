package lanmu.factory;

import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.utils.Hib;

public class CommentFactory {

    public static Comment findById(long id) {
        return Hib.query(session -> session.get(Comment.class, id));
    }

    public static List<Comment> queryPostComments(long postId) {
        return Hib.query(session -> {
            List<Comment> comments
                    = session.createQuery("from Comment where postId=:postId", Comment.class)
                    .setParameter("postId", postId)
                    .getResultList();
            for (Comment comment : comments) {
                Hibernate.initialize(comment.getReplies());
            }
            return comments;
        });
    }

    public static int queryPostCommentCount(long postId) {
        AtomicReference<Long> aLong = new AtomicReference<>();
        Hib.queryOnly(session -> {
            Long count = session.createQuery("select count(*) from Comment where postId=:postId", Long.class)
                    .setParameter("postId", postId)
                    .uniqueResult();
            aLong.set(count);
        });
        if (aLong.get() != null) {
            return Math.toIntExact(aLong.get());
        }
        return 0;
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

    public static int updateCommentsReceived(long userId) {
        return Hib.query(session ->
                session.createQuery("update Comment set received=1 " +
                        "where postId in (from BookPost where creatorId = :userId) " +
                        "and fromId!=:userId and received=0")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }

    public static List<Comment> queryUserCommentsByMonth(long userId, int delta) {
        return Hib.query(session -> {
            LocalDateTime toDate = LocalDateTime.now().minus(30 * delta, ChronoUnit.DAYS);
            LocalDateTime fromDate = toDate.minus(30, ChronoUnit.DAYS);
            List<Comment> resultList = session.createQuery("from Comment " +
                    "where fromId=:userId and time > :date1 and time < :date2", Comment.class)
                    .setParameter("userId", userId)
                    .setParameter("date1", fromDate)
                    .setParameter("date2", toDate)
                    .getResultList();
            resultList.forEach(comment -> comment.getBookPost().getBook());
            return resultList;
        });
    }

    public static List<CommentReply> queryUserRepliesByMonth(long userId, int delta) {
        return Hib.query(session -> {
            LocalDateTime toDate = LocalDateTime.now().minus(30 * delta, ChronoUnit.DAYS);
            LocalDateTime fromDate = toDate.minus(30, ChronoUnit.DAYS);
            List<CommentReply> resultList = session.createQuery("from CommentReply " +
                    "where fromId=:userId and time > :date1 and time < :date2", CommentReply.class)
                    .setParameter("userId", userId)
                    .setParameter("date1", fromDate)
                    .setParameter("date2", toDate)
                    .getResultList();
            resultList.forEach(reply -> reply.getComment().getBookPost().getBookId());
            return resultList;
        });
    }
}

package lanmu.factory;

import org.hibernate.Hibernate;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lanmu.entity.card.CommentCard;
import lanmu.entity.db.Comment;
import lanmu.entity.db.CommentReply;
import lanmu.utils.Hib;

import static lanmu.entity.card.CommentCard.ORDER_COMMENT_THUMBS_UP_FIRST;

public class CommentFactory {

    public static final int ITEM_COUNT_PER_PAGE = 5;

    private static final String HQL_COMMENTS_TIME_ORDER =
            "select cm," +
                    "(select count (tp.commentId) from ThumbsUp tp where tp.commentId=cm.id) as cnt, " +
                    "(select distinct (1) from ThumbsUp tp where tp.commentId=cm.id and fromId=:fromId) " +
                    "from Comment cm " +
                    "where cm.postId=:postId " +
                    "order by cm.time ";

    private static final String HQL_COMMENTS_TIME_ORDER_DESC =
            HQL_COMMENTS_TIME_ORDER + "desc ";

    private static final String HQL_COMMENTS_TB_ORDER =
            "select cm," +
                    "(select COUNT(tp.commentId) from ThumbsUp tp where tp.commentId=cm.id) as cnt, " +
                    "(select distinct (1) from ThumbsUp tp where tp.commentId=cm.id and fromId=:fromId) " +
                    "from Comment cm " +
                    "where cm.postId=:postId " +
                    "order by cnt desc ";

    public static Comment findById(long id) {
        return Hib.query(session -> session.get(Comment.class, id));
    }

    /**
     * 返回评论列表
     */
    public static List<CommentCard> queryPostComments(long postId, long userId, int order, int page) {
        return Hib.query(session -> {
            String hql;
            switch (order) {
                case CommentCard.ORDER_DEFAULT:
                    hql = HQL_COMMENTS_TIME_ORDER_DESC;
                    break;
                case CommentCard.ORDER_TIME_REMOTEST_FIRST:
                    hql = HQL_COMMENTS_TIME_ORDER;
                    break;
                case ORDER_COMMENT_THUMBS_UP_FIRST:
                    hql = HQL_COMMENTS_TB_ORDER;
                    break;
                default:
                    hql = HQL_COMMENTS_TIME_ORDER_DESC;
            }
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> objects = query
                    .setParameter("postId", postId)
                    .setParameter("fromId", userId)
                    .setFirstResult(Math.max(page - 1, 0) * ITEM_COUNT_PER_PAGE)
                    .setMaxResults(ITEM_COUNT_PER_PAGE)
                    .getResultList();
            return objects2Cards(objects);
        });
    }


    private static List<CommentCard> objects2Cards(List<Object[]> objects) {
        return objects.stream()
                .map(object -> {
                    Comment comment = (Comment) object[0];
                    int cnt = Math.toIntExact((long) object[1]);
                    Hibernate.initialize(comment.getReplies());
                    CommentCard card = new CommentCard(comment);
                    card.setThumbsUpCount(cnt);
                    card.setThumbsUp(object[2] != null);
                    return card;
                })
                .collect(Collectors.toList());
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

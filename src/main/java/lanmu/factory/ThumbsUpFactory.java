package lanmu.factory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lanmu.entity.db.Comment;
import lanmu.entity.db.ThumbsUp;
import lanmu.entity.db.User;
import lanmu.utils.Hib;

public class ThumbsUpFactory {

    public static ThumbsUp createThumbsUp(Comment comment, User from) {
        return Hib.query(session -> {
                    ThumbsUp thumbsUp = new ThumbsUp();
                    thumbsUp.setComment(comment);
                    thumbsUp.setFrom(from);
                    thumbsUp.setReceived(0);
                    session.saveOrUpdate(thumbsUp);
                    return thumbsUp;
                }
        );
    }

    public static ThumbsUp deleteThumbsUp(ThumbsUp thumbsUp) {
        return Hib.query(session -> {
                    session.delete(thumbsUp);
                    return thumbsUp;
                }
        );
    }

    public static int countThumbsUp(long commentId) {
        AtomicReference<Long> count = new AtomicReference<>();
        Hib.queryOnly(session -> count.set(
                session.createQuery("select count(*) from ThumbsUp " +
                        "where commentId=:commentId", Long.class)
                        .setParameter("commentId", commentId)
                        .uniqueResult()
        ));
        if (count.get() == null) {
            return 0;
        }
        return Math.toIntExact(count.get());
    }

    public static ThumbsUp find(long userId, long commentId) {
        return Hib.query(session ->
                session.createQuery("from ThumbsUp " +
                        "where commentId=:commentId and fromId=:userId", ThumbsUp.class)
                        .setParameter("commentId", commentId)
                        .setParameter("userId", userId)
                        .uniqueResult());
    }


    public static List<ThumbsUp> queryUserReceivedThumbsUp(long userId) {
        return Hib.query(session -> {
            List<ThumbsUp> thumbsUps = session.createQuery(
                    "from ThumbsUp " +
                    "where " +
                    "commentId in (from Comment where fromId = :userId) " +
                    "and fromId!= :userId", ThumbsUp.class)
                    .setParameter("userId", userId)
                    .setMaxResults(20)
                    .getResultList();
            for (ThumbsUp thumbsUp : thumbsUps) {
                thumbsUp.getComment().getBookPost().getBook();
            }
            return thumbsUps;
        });
    }


    public static int updateThumbsUpsReceived(long userId) {
        return Hib.query(session ->
                session.createQuery("update ThumbsUp set received=1 " +
                        "where " +
                        "commentId in (from Comment where fromId = :userId)" +
                        " and" +
                        " fromId!= :userId and received=0")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }

    public static List<ThumbsUp> queryUserThumbsUpByMonth(long userId, int delta) {
            return Hib.query(session -> {
                LocalDateTime toDate = LocalDateTime.now().minus(30 * delta, ChronoUnit.DAYS);
                LocalDateTime fromDate = toDate.minus(30, ChronoUnit.DAYS);
                List<ThumbsUp> resultList = session.createQuery("from ThumbsUp " +
                        "where fromId=:userId and time > :date1 and time < :date2", ThumbsUp.class)
                        .setParameter("userId", userId)
                        .setParameter("date1", fromDate)
                        .setParameter("date2", toDate)
                        .getResultList();
                resultList.forEach(thumbsUp -> thumbsUp.getComment().getBookPost().getBookId());
                return resultList;
            });
    }
}

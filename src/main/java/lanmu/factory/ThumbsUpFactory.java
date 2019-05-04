package lanmu.factory;

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

}

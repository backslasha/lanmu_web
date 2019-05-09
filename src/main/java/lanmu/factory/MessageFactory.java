package lanmu.factory;

import java.util.Collections;
import java.util.List;

import lanmu.entity.db.Message;
import lanmu.utils.Hib;

public class MessageFactory {

    public static List<Message> pullMessages(long fromId, long toId, int maxCount) {
        return pullMessages(fromId, toId, maxCount, 0);
    }

    public static List<Message> pullMessages(long fromId, long toId, int maxCount, int fromIndex) {
        return Hib.query(session -> {
            List<Message> resultList =
                    session.createQuery("from Message msg where (fromId=:fromId and toId=:toId) or (toId=:fromId and fromId=:toId) order by msg.time desc", Message.class)
                            .setParameter("fromId", fromId)
                            .setParameter("toId", toId)
                            .setMaxResults(maxCount)
                            .setFirstResult(fromIndex)
                            .getResultList();
            Collections.reverse(resultList);
            return resultList;
        });
    }

    public static int countUnreadMessageOf(long userId) {
        Long count = Hib.query(session ->
                session.createQuery("select count(*) from Message where toId=:toId and received=0", Long.class)
                        .setParameter("toId", userId)
                        .uniqueResult());
        return Math.toIntExact(count);
    }

    public static int markMessagesReceived(long userId) {
        return Hib.query(session ->
                session.createQuery("update Message set received=1 " +
                        "where toId =:userId and received=0")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }
}


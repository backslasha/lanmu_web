package lanmu.factory;

import java.util.Collections;
import java.util.List;

import lanmu.entity.db.Message;
import lanmu.utils.Hib;

public class MessageFactory {

    public static List<Message> queryMsg(long fromId, long toId, int maxCount) {
        return queryMsg(fromId, toId, maxCount, 0);
    }

    public static List<Message> queryMsg(long fromId, long toId, int maxCount, int fromIndex) {
        return Hib.query(session -> {
            List<Message> resultList = session.createQuery("from Message msg where (fromId=:fromId and toId=:toId) or (toId=:fromId and fromId=:toId) order by msg.time desc", Message.class)
                    .setParameter("fromId", fromId)
                    .setParameter("toId", toId)
                    .setMaxResults(maxCount)
                    .setFirstResult(fromIndex)
                    .getResultList();
            Collections.reverse(resultList);
            return resultList;
        });
    }
}


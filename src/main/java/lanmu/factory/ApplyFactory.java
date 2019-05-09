package lanmu.factory;

import java.util.List;

import lanmu.entity.db.Apply;
import lanmu.utils.Hib;

public class ApplyFactory {
    public static List<Apply> pullApplies(long userId) {
        return Hib.query(session ->
                session.createQuery("from Apply where toId=:toId", Apply.class)
                        .setParameter("toId", userId)
                        .getResultList());
    }

    public static Apply findById(long applyId) {
        return Hib.query(session -> session.get(Apply.class, applyId));
    }

    public static int countUnhandledApplyOf(long userId) {
        Long count = Hib.query(session ->
                session.createQuery("select count(*) from Apply where toId=:toId and handle=0", Long.class)
                        .setParameter("toId", userId)
                        .uniqueResult());
        return Math.toIntExact(count);
    }

    public static int countUnReceivedApplyOf(long userId) {
        Long count = Hib.query(session ->
                session.createQuery("select count(*) from Apply where toId=:toId and received=0", Long.class)
                        .setParameter("toId", userId)
                        .uniqueResult());
        return Math.toIntExact(count);
    }

    public static int markAppliesReceived(long userId) {
        return Hib.query(session ->
                session.createQuery("update Apply set received=1 " +
                        "where toId =:userId and received=0")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }
}

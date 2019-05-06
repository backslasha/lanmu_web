package lanmu.factory;

import java.util.List;

import lanmu.entity.db.Apply;
import lanmu.utils.Hib;

public class ApplyFactory {
    public static List<Apply> searchApplies(long userId){
        return Hib.query(session ->
                session.createQuery("from Apply where toId=:toId", Apply.class)
                 .setParameter("toId", userId)
                 .getResultList());
    }

    public static Apply findById(long applyId) {
        return Hib.query(session -> session.get(Apply.class, applyId));
    }
}

import java.io.Serializable;

import lanmu.entity.db.A;
import lanmu.entity.db.B;
import lanmu.utils.Hib;

public class Test {
    public static void main(String[] args) {
        B b = new B("bname", new A("cname"));
        Serializable query = Hib.query(session -> session.save(b));
        System.out.println(query);
    }
}

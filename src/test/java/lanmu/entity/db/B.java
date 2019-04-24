package lanmu.entity.db;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "B")
public class B {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, insertable = false)
    private long id;
    @Column
    private String bname;
    @ManyToOne
    @Cascade(CascadeType.ALL)
    private A a;

    public B() {
    }

    public B(String bname, A a) {
        this.bname = bname;
        this.a = a;
    }

    @Override
    public String toString() {
        return "lanmu.entity.db.B{" +
                "id=" + id +
                ", bname='" + bname + '\'' +
                ", a=" + a +
                '}';
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String cname) {
        this.bname = cname;
    }
}

package lanmu.entity.db;

import java.util.List;

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
@Table(name = "A")
public class A {


    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, insertable = false)
    private long id;
    @Column
    private String cname;

    @OneToMany
    private List<B> b;

    public A() {
    }

    public A(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "lanmu.entity.db.A{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}

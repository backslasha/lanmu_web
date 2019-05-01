package lanmu.entity.db;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tb_message")
public class Message {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false, nullable = false, insertable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fromId")
    private User from;

    @Column(updatable = false, insertable = false, nullable = false)
    private long fromId;

    @ManyToOne
    @JoinColumn(name = "toId")
    private User to;

    @Column(updatable = false, insertable = false, nullable = false)
    private long toId;

    @Column
    private String content;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(nullable = false)
    private int type;
    @Column(nullable = false)
    private int received = 0;

    public int getType() {
        return type;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}

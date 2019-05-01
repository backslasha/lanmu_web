package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;

import lanmu.entity.db.Message;

public class MessageCard {

    public static final int TYPE_TEXT = 0;

    @Expose
    private long id;
    @Expose
    private int type;
    @Expose
    private LocalDateTime time;
    @Expose
    private String content;
    @Expose
    private UserCard from;
    @Expose
    private UserCard to;
    @Expose
    private int received;

    public MessageCard(Message msg) {
        id = msg.getId();
        type = msg.getType();
        time = msg.getTime();
        content = msg.getContent();
        from = new UserCard(msg.getFrom());
        to = new UserCard(msg.getTo());
        received = msg.getReceived();
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserCard getFrom() {
        return from;
    }

    public void setFrom(UserCard from) {
        this.from = from;
    }

    public UserCard getTo() {
        return to;
    }

    public void setTo(UserCard to) {
        this.to = to;
    }
}

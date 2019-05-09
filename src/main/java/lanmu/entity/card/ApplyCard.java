package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

import lanmu.entity.db.Apply;


public class ApplyCard {
    @Expose
    private long id;
    @Expose
    private UserCard from;
    @Expose
    private long fromId;
    @Expose
    private LocalDateTime time;
    @Expose
    private UserCard to;
    @Expose
    private long toId;
    @Expose
    private int handle = 0;
    @Expose
    private int received = 0;

    public ApplyCard(Apply apply) {
        this.id = apply.getId();
        this.from = new UserCard(apply.getFrom());
        this.fromId = apply.getFromId();
        this.time = apply.getTime();
        this.to = new UserCard(apply.getTo());
        this.toId = apply.getToId();
        this.handle = apply.getHandle();
        this.received = apply.getReceived();
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public UserCard getFrom() {
        return from;
    }

    public void setFrom(UserCard from) {
        this.from = from;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public UserCard getTo() {
        return to;
    }

    public void setTo(UserCard to) {
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


    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }
}

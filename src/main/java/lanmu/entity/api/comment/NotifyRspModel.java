package lanmu.entity.api.comment;

import com.google.gson.annotations.Expose;

import java.util.List;

import lanmu.entity.card.NotifyCard;

public class NotifyRspModel {

    @Expose
    private List<NotifyCard> notifyCards;
    @Expose
    private int unread;

    public NotifyRspModel(int unread, List<NotifyCard> notifyCards) {
        this.unread = unread;
        this.notifyCards = notifyCards;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public List<NotifyCard> getNotifyCards() {
        return notifyCards;
    }

    public void setNotifyCards(List<NotifyCard> notifyCards) {
        this.notifyCards = notifyCards;
    }
}

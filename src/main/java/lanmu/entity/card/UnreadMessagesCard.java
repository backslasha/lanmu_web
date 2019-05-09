package lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.stream.Collectors;

import lanmu.entity.db.Message;
import lanmu.entity.db.User;

public class UnreadMessagesCard {
    @Expose
    private UserCard talkTo;
    @Expose
    private List<MessageCard> unreadMsgCards;

    public UnreadMessagesCard(User from, List<Message> messages) {
        this.talkTo = new UserCard(from);
        this.unreadMsgCards = messages.stream().map(MessageCard::new).collect(Collectors.toList());
    }

    public UserCard getTalkTo() {
        return talkTo;
    }

    public void setTalkTo(UserCard talkTo) {
        this.talkTo = talkTo;
    }

    public List<MessageCard> getUnreadMsgCards() {
        return unreadMsgCards;
    }

    public void setUnreadMsgCards(List<MessageCard> unreadMsgCards) {
        this.unreadMsgCards = unreadMsgCards;
    }
}

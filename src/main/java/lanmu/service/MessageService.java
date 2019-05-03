package lanmu.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.api.message.CreateMsgModel;
import lanmu.entity.api.message.PullMsgModel;
import lanmu.entity.card.MessageCard;
import lanmu.entity.db.Message;
import lanmu.entity.db.User;
import lanmu.factory.MessageFactory;
import lanmu.factory.UserFactory;
import lanmu.utils.Hib;

import static lanmu.factory.UserFactory.exists;

@Path("msg/")
public class MessageService extends BaseService {

    @POST
    @Path("create/")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseModel<MessageCard> createMsg(CreateMsgModel msgModel) {
        if (!CreateMsgModel.check(msgModel)) {
            return ResponseModel.buildParameterError();
        }
        User from = UserFactory.findById(msgModel.getFromId());
        User to = UserFactory.findById(msgModel.getToId());
        if (from == null || to == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        Message committed = Hib.query(session -> {
            Message message = new Message();
//        message.setFromId(msgModel.getFromId());
//        message.setToId(msgModel.getToId());
            message.setContent(msgModel.getContent());
            message.setFrom(from);
            message.setTo(to);
            message.setReceived(0);
            message.setType(msgModel.getType());
            session.saveOrUpdate(message);
            return message;
        });

        if (committed != null) {
            return ResponseModel.buildOk(new MessageCard(committed));
        }
        return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
    }


    @POST
    @Path("record/")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseModel<List<MessageCard>> pullMsgRecord(PullMsgModel model) {
        if (!PullMsgModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        if (!exists(model.getFromId()) || !exists(model.getToId())) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        List<Message> committed = MessageFactory
                .queryMsg(model.getFromId(), model.getToId(), model.getPullCount());
        if (committed != null) {
            return ResponseModel.buildOk(
                    committed.stream()
                            .map(MessageCard::new)
                            .collect(Collectors.toList())
            );
        }
        return ResponseModel.buildNotFoundMessageError();
    }

    // 按 toId 分组，收集每组 time 最大的 Message，即每个 toId 最后收到的一条消息
    private static final String toIdGroupMaxTimeMessages = "from Message m where m.time=(select max(time) from Message mm where mm.toId=m.toId)";

    // 按 fromId 分组，收集每组 time 最大的 Message，即每个 fromId 最后发送的一条消息
    private static final String fromIdGroupMaxTimeMessages = "from Message m where m.time=(select max(time) from Message mm where mm.fromId=m.fromId)";

    // 对于每个 toId、fromId，收集 userId 最近发送的、接受的 Message，
    private static final String lastContractsHql =
            "from Message msg where " +
                    "(msg in (" + toIdGroupMaxTimeMessages + ") " +
                    "or " + "msg in (" + fromIdGroupMaxTimeMessages + "))" +
                    "and " + "(toId=:userId or fromId=:userId )" +
                    "order by time desc";


//    private static final String lastContractsHql
//            = "from Message where id in " +
//            "(select id from Message where time in " +
//            "(select max(time) from Message where fromId=:userId or toId=:userId))" +
//            "order by time desc";

    @POST
    @Path("conversations/")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseModel<List<MessageCard>> pullConversations(long userId) {
        User self = getSelf();
        if (userId != self.getId()) {
            return ResponseModel.buildNoPermissionError();
        }

        List<Message> committed = Hib.query(session ->
                session.createQuery(lastContractsHql, Message.class)
                        .setParameter("userId", userId)
                        .setMaxResults(20)
                        .getResultList());

        if (committed != null) {
            return ResponseModel.buildOk(
                    committed.stream()
                            .map(Wrapper::new)
                            .distinct()
                            .map(Wrapper::getMessage)
                            .map(MessageCard::new)
                            .collect(Collectors.toList()));
        }

        return ResponseModel.buildNotFoundMessageError();
    }

    /**
     * Message 去重使用
     */
    private static class Wrapper {
        private long fromId, toId;
        private Message message;

        Wrapper(Message message) {
            this.message = message;
            this.fromId = message.getFromId();
            this.toId = message.getToId();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Wrapper wrapper = (Wrapper) o;
            return (fromId == wrapper.fromId && toId == wrapper.toId)
                    || (fromId == wrapper.toId && toId == wrapper.fromId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromId, toId) + Objects.hash(toId,fromId);
        }

        Message getMessage() {
            return message;
        }
    }

}

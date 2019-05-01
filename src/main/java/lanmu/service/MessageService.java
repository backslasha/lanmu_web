package lanmu.service;

import java.util.List;
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
public class MessageService {

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

}

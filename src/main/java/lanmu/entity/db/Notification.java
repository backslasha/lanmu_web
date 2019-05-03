package lanmu.entity.db;

public class Notification {

    private static final int TYPE_NEW_COMMENT = 1;
    private static final int TYPE_NEW_REPLY = 2;

    private int type;

    private long commentId;
    private Comment comment;

    private long replyId;
    private CommentReply reply;

    private static String string = "from Comment where postId in (from BookPost where creatorId = 6) and fromId!= 6";
    private static String string2 = "from CommentReply where commentId in (from Comment where fromId = 6) and fromId!=6";

}

package lanmu.service;

import com.google.common.base.Strings;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.api.post.CreatePostModel;
import lanmu.entity.api.post.SearchPostModel;
import lanmu.entity.card.BookCard;
import lanmu.entity.card.BookPostCard;
import lanmu.entity.db.Book;
import lanmu.entity.db.BookPost;
import lanmu.entity.db.User;
import lanmu.factory.BookPostFactory;
import lanmu.factory.CommentFactory;
import lanmu.utils.Hib;

@Path("/posts")
public class BookPostService extends BaseService {

    @Path("/create")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseModel<BookPostCard> create(CreatePostModel model) {

        if (!CreatePostModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        // 创建书帖成功时，返回该 BookPostCard
        return Hib.query(session -> {

            // 如果待新建的书帖和已经存在书帖信息冲突，创建失败，返回该冲突书帖信息
            BookPost bookPost = BookPostFactory.exists(model);

            if (bookPost != null) {
                return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_POST,
                        new BookPostCard(bookPost));
            }

            // 从 session 加载出完整的 User 对象
            User user = new User();
            session.load(user, model.getCreateId());

            // 客户端传来的待新建的 Book 对象
            BookCard b = model.getBook();
            Book book = new Book(
                    b.getName(),
                    b.getAuthor(),
                    b.getPublisher(),
                    b.getPublishDate(),
                    b.getVersion(),
                    b.getLanguish(),
                    b.getCoverUrl(),
                    b.getIntroduction()
            );

            bookPost = new BookPost(
                    book,
                    user,
                    model.getContent(),
                    model.getImages()
            );

            try {
                session.save(bookPost); // todo 此处不合理，当 transaction commit 失败时，将仍然返回 ok 的响应
                return ResponseModel.buildOk(new BookPostCard(bookPost));
            } catch (Exception e) {
                return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_POST);
            }
        });
    }


    @Path("/search")
    @GET
    @Produces("application/json")
    public ResponseModel<List<BookPostCard>> search(@QueryParam("type") int type,
                                                    @QueryParam("value") String value) {
        if (Strings.isNullOrEmpty(value)) {
            return ResponseModel.buildParameterError();
        }
        try {
            switch (type) {
                case SearchPostModel.TYPE_KEYWORD:
                    return ResponseModel.buildOk(
                            BookPostFactory.queryBookPostsByKeyWord(value));
                case SearchPostModel.TYPE_CREATOR_ID:
                    return ResponseModel.buildOk(
                            BookPostFactory.queryBookPostsByCreatorId(Long.parseLong(value)));
                case SearchPostModel.TYPE_POST_ID:
                    return ResponseModel.buildOk(BookPostFactory.queryBookPostsByPostId(Long.parseLong(value)));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ResponseModel.buildParameterError();
    }

    @Path("/hot")
    @GET
    @Produces("application/json")
    public ResponseModel<List<BookPostCard>> hot() {
        List<BookPost> bookPosts = Hib.query(session ->
                session.createQuery("from BookPost where id in(select postId from Comment group by postId order by count(postId))", BookPost.class)
                        .setMaxResults(20)
                        .getResultList());
        if (bookPosts != null) {
            List<BookPostCard> result = covert(bookPosts);
            Collections.reverse(result);
            return ResponseModel.buildOk(result);
        }
        return ResponseModel.buildNotFoundPostError();
    }


    @Path("/latest")
    @GET
    @Produces("application/json")
    public ResponseModel<List<BookPostCard>> latest() {
        List<BookPost> bookPosts = Hib.query(session ->
                session.createQuery("from BookPost order by createDate desc ", BookPost.class)
                        .setMaxResults(20)
                        .getResultList());
        if (bookPosts != null) {
            return ResponseModel.buildOk(covert(bookPosts));
        }
        return ResponseModel.buildNotFoundPostError();
    }

    private List<BookPostCard> covert(List<BookPost> bookPosts) {
        return bookPosts.stream()
                .map(BookPostCard::new)
                .peek(card ->
                        card.setCommentCount(CommentFactory.queryPostCommentCount(card.getId())))
                .collect(Collectors.toList());
    }

}

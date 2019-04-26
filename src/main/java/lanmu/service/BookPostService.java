package lanmu.service;

import java.io.Serializable;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lanmu.entity.api.base.ResponseModel;
import lanmu.entity.api.post.BookPostRspModel;
import lanmu.entity.api.post.CreatePostModel;
import lanmu.entity.card.BookCard;
import lanmu.entity.card.BookPostCard;
import lanmu.entity.db.Book;
import lanmu.entity.db.BookPost;
import lanmu.entity.db.User;
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
            BookPost bookPost = search(model);

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
                session.save(bookPost);
                return ResponseModel.buildOk(new BookPostCard(bookPost));
            } catch (Exception e) {
                return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_POST);
            }
        });
    }

    private BookPost search(CreatePostModel model) {
        return Hib.query(session -> {
            BookCard book = model.getBook();
            Book dbBook = (Book) session.createQuery("from Book " +
                    "where name = :name " +
                    "and author=:author " +
                    "and version=:version " +
                    "and languish=:languish")
                    .setParameter("name", book.getName())
                    .setParameter("author", book.getAuthor())
                    .setParameter("version", book.getVersion())
                    .setParameter("languish", book.getLanguish())
                    .uniqueResult();

            if (dbBook == null) {
                return null;
            } else {
                return (BookPost) session.createQuery(
                        "from BookPost as post where bookId =:bookId")
                        .setParameter("bookId", dbBook.getId())
                        .uniqueResult();
            }
        });
    }

}

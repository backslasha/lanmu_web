package lanmu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lanmu.entity.api.post.BookPostModel;
import lanmu.entity.db.BookPost;
import lanmu.entity.card.BookPostCard;
import lanmu.utils.Hib;

@Path("/posts")
public class BookPostService extends BaseService {

    @Path("/publish")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public BookPostCard publish(BookPostModel bookPostModel) {
        BookPost bookPost = new BookPost(
                bookPostModel.getBook(),
                bookPostModel.getCreateId(),
                bookPostModel.getContent(),
                bookPostModel.getImages()
        );
        return Hib.query(session -> {
            BookPost save = (BookPost) Hib.session().save(bookPost);
            return new BookPostCard(
                    save.getBook(),
                    save.getCreator(),
                    save.getCreateDate(),
                    save.getContent(),
                    save.getImages()
            );
        });
    }
}

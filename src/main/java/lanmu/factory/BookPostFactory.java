package lanmu.factory;

import com.google.common.base.Function;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lanmu.entity.api.post.CreatePostModel;
import lanmu.entity.card.BookCard;
import lanmu.entity.card.BookPostCard;
import lanmu.entity.db.Book;
import lanmu.entity.db.BookPost;
import lanmu.utils.Hib;

public class BookPostFactory {
    /**
     * 从数据库中查找是否存在 和 该 model 对应的 BookPost，存在则返回这个 BookPost
     */
    public static BookPost exists(CreatePostModel model) {
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

    public static List<BookPostCard> queryBookPostsByPostId(long postId) {
        return Hib.query(session -> {
            List<BookPost> bookPosts
                    = session.createQuery("from BookPost where id=:postId",
                    BookPost.class)
                    .setParameter("postId", postId)
                    .getResultList();
            return bookPosts.stream()
                    .map((Function<BookPost, BookPostCard>) BookPostCard::new)
                    .peek(card ->
                            card.setCommentCount(CommentFactory.queryPostCommentCount(card.getId())))
                    .collect(Collectors.toList());
        });

    }

    public static List<BookPostCard> queryBookPostsByCreatorId(long creatorId) {
        return Hib.query(session -> {
            List<BookPost> bookPosts
                    = session.createQuery("from BookPost where creatorId=:creatorId",
                    BookPost.class)
                    .setParameter("creatorId", creatorId)
                    .getResultList();
            return bookPosts.stream()
                    .map((Function<BookPost, BookPostCard>) BookPostCard::new)
                    .peek(card ->
                            card.setCommentCount(CommentFactory.queryPostCommentCount(card.getId())))
                    .collect(Collectors.toList());
        });
    }

    public static List<BookPost> queryUserPostsByMonth(long creatorId, int delta) {
        return Hib.query(session -> {
            LocalDateTime toDate = LocalDateTime.now().minus(30 * delta, ChronoUnit.DAYS);
            LocalDateTime fromDate = toDate.minus(30, ChronoUnit.DAYS);
            return session.createQuery("from BookPost where creatorId=:creatorId" +
                    " and createDate > :date1 and createDate < :date2", BookPost.class)
                    .setParameter("creatorId", creatorId)
                    .setParameter("date1", fromDate)
                    .setParameter("date2", toDate)
                    .getResultList();
        });
    }

    public static List<BookPostCard> queryBookPostsByKeyWord(String keyword) {
        return Hib.query(session -> {
            List<BookPost> bookPosts
                    = session.createQuery("from BookPost where bookId in " +
                            "(select book.id from Book as book where book.name like :value)",
                    BookPost.class)
                    .setParameter("value", "%" + keyword + "%")
                    .getResultList();
            return bookPosts.stream()
                    .map((Function<BookPost, BookPostCard>) BookPostCard::new)
                    .peek(card ->
                            card.setCommentCount(CommentFactory.queryPostCommentCount(card.getId())))
                    .collect(Collectors.toList());
        });
    }

    public static BookPost findById(long postId) {
        return Hib.query(session -> session.find(BookPost.class, postId));
    }
}

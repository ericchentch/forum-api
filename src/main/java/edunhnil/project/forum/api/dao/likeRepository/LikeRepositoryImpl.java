package edunhnil.project.forum.api.dao.likeRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractRepository;

@Repository
public class LikeRepositoryImpl extends AbstractRepository implements LikeRepository {

    @Override
    public int getTotalPostLike(int postId) {
        String sql = "SELECT COUNT(*) FROM forum.plikes p where p.post_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }

    @Override
    public int getTotalCommentLike(int commentId) {
        String sql = "SELECT COUNT(*) FROM forum.clikes c where c.comment_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, commentId);

    }

    @Override
    public boolean checkLikeComment(int commentId, String ownerId) {
        String sql = "SELECT * FROM forum.clikes c WHERE c.comment_id = ? AND owner_id = ?";
        try {
            CommentLike commentLike = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CommentLike.class),
                    commentId, ownerId);
            return commentLike != null;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean checkLikePost(int postId, String ownerId) {
        String sql = "SELECT * FROM forum.plikes p WHERE p.post_id = ? AND owner_id = ?";
        try {
            PostLike postLike = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PostLike.class), postId,
                    ownerId);
            return postLike != null;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void addNewCommentLike(String ownerId, int commentId) {
        String sql = "INSERT INTO forum.clikes (owner_id, comment_id) VALUES (?,?)";
        jdbcTemplate.update(sql, ownerId, commentId);
    }

    @Override
    public void addNewPostId(String ownerId, int postId) {
        String sql = "INSERT INTO forum.plikes (owner_id, post_id) VALUES (?,?)";
        jdbcTemplate.update(sql, ownerId, postId);
    }

    @Override
    public void hideLikePost(String ownerId, int postId) {
        String sql = "DELETE FROM forum.plikes WHERE owner_id = ? AND post_id = ?";
        jdbcTemplate.update(sql, ownerId, postId);
    }

    @Override
    public void hideCommentLike(String ownerId, int commentId) {
        String sql = "DELETE FROM forum.clikes WHERE owner_id=? AND comment_id=?";
        jdbcTemplate.update(sql, ownerId, commentId);
    }

    @Override
    public void resetIdComment() {
        int max = getMax("SELECT MAX(id) FROM forum.clikes") + 1;
        String sql = "ALTER SEQUENCE forum.clikes_id_seq RESTART WITH "
                + max;
        jdbcTemplate.execute(sql);
    }

    @Override
    public void resetIdPost() {
        int max = getMax("SELECT MAX(id) FROM forum.plikes") + 1;
        String sql = "ALTER SEQUENCE forum.plikes_id_seq RESTART WITH "
                + max;
        jdbcTemplate.execute(sql);
    }

}

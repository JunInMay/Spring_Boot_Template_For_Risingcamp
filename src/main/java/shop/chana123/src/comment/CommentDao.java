package shop.chana123.src.comment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.chana123.config.BaseException;
import shop.chana123.src.comment.model.*;
import shop.chana123.src.user.model.GetUserRes;
import shop.chana123.src.video.model.PostVideosEvaRes;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static shop.chana123.config.BaseResponseStatus.DATABASE_QUERY_ERROR;

@Repository
public class CommentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 대댓글 리스트 가져오기
    public List<GetCommentsRecommentlistRes> retrieveRecommentList(long comment_id) {
        String retrieveRecommentListQuery = ""
                + "SELECT user.profile_image AS profile_image, "
                + "       user.name          AS user_name, "
                + "       comment.created_at AS created_at, "
                + "       comment.content    AS content, "
                + "       comment.likes      AS likes "
                + "FROM   comment "
                + "       JOIN user "
                + "         ON user.id = comment.id "
                + "WHERE  is_recomment = 1 "
                + "       AND original_comment_id = ?";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return this.jdbcTemplate.query(retrieveRecommentListQuery,
                (rs, rowNum) -> new GetCommentsRecommentlistRes(
                        rs.getString("profile_image"),
                        rs.getString("user_name"),
                        dateFormat.format(rs.getTimestamp("created_at")),
                        rs.getString("content"),
                        rs.getLong("likes")
                ), comment_id);

    }

    // 코멘트 리스트 가져오기
    public List<GetCommentsCommentlistRes> retrieveCommentList(long video_id){
        String retreiveCommentListQuery = ""
                + "SELECT user.profile_image AS profile_image, "
                + "       user.name          AS user_name, "
                + "       main.created_at    AS created_at, "
                + "       main.content       AS content, "
                + "       main.likes         AS likes, "
                + "       CASE "
                + "         WHEN forcount.count IS NULL THEN 0 "
                + "         ELSE forcount.count "
                + "       end                AS recomment_count "
                + "FROM   comment main "
                + "       JOIN user "
                + "         ON main.user_id = user.id "
                + "       LEFT JOIN (SELECT original_comment_id, "
                + "                         Count(*) AS count "
                + "                  FROM   comment "
                + "                  GROUP  BY original_comment_id) forcount "
                + "              ON main.id = forcount.original_comment_id "
                + "WHERE  main.video_id = ? "
                + "       AND main.is_recomment = 0";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return this.jdbcTemplate.query(retreiveCommentListQuery,
                (rs,rowNum) -> new GetCommentsCommentlistRes(
                        rs.getString("profile_image"),
                        rs.getString("user_name"),
                        dateFormat.format(rs.getTimestamp("created_at")),
                        rs.getString("content"),
                        rs.getLong("likes"),
                        rs.getInt("recomment_count")),
                video_id);
    }
    // 코멘트 만들기
    public PostCommentsRes createComment(PostCommentsReq postCommentsReq) throws BaseException {

        String createCommentQuery = "" +
                "insert into comment " +
                "(user_id, content, video_id, is_recomment, original_comment_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        Object[] createCommentParams = new Object[]{
                postCommentsReq.getUser_id(),
                postCommentsReq.getContent(),
                postCommentsReq.getVideo_id(),
                postCommentsReq.getIs_recomment(),
                postCommentsReq.getOriginal_comment_id()
        };

        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String retrieveCommentQuery = "" +
                "select user_id, content, video_id, is_recomment, original_comment_id, created_at " +
                "from comment " +
                "where id = ?";
        return this.jdbcTemplate.queryForObject(retrieveCommentQuery,
                (rs, rowNum) -> new PostCommentsRes(
                        rs.getLong("user_id"),
                        rs.getString("content"),
                        rs.getLong("video_id"),
                        rs.getInt("is_recomment"),
                        rs.getInt("original_comment_id"),
                        dateFormat.format(rs.getTimestamp("created_at"))),
                recentId);
    }

    public PostCommentsEvaRes createEvaluationComment(PostCommentsEvaReq postCommentsEvaReq) {

        String createEvaluationCommentQuery = "" +
                "insert into like_hate_comment " +
                "(user_id, comment_id, evaluation) " +
                "VALUES (?, ?, ?)";
        long user_id = postCommentsEvaReq.getUser_id();
        long comment_id = postCommentsEvaReq.getComment_id();
        Object[] createEvaluationCommentParams = new Object[]{
                user_id,
                comment_id,
                postCommentsEvaReq.getEvaluation()
        };

        this.jdbcTemplate.update(createEvaluationCommentQuery, createEvaluationCommentParams);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String retrieveEvaluationCommentQuery = "" +
                "select user_id, comment_id, evaluation " +
                "from like_hate_comment " +
                "where user_id = ? and comment_id = ?";
        return this.jdbcTemplate.queryForObject(retrieveEvaluationCommentQuery,
                (rs, rowNum) -> new PostCommentsEvaRes(
                        rs.getLong("user_id"),
                        rs.getLong("comment_id"),
                        rs.getInt("evaluation")
                ),
                new Object[]{user_id, comment_id});
    }
}
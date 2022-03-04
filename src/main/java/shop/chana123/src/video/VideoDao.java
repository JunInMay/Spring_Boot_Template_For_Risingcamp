package shop.chana123.src.video;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.chana123.src.video.model.*;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class VideoDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 연결 체크
    public int connectionCheck(){
        return this.jdbcTemplate.queryForObject("select 1", int.class);
    }

    // 추천 비디오 리스트 가져오기
    public List<GetVideosListRes> retrieveVideoList(){
        String retrieveVideoListQuery = ""
                + "SELECT video.thumbnail    AS thumbnail_address, "
                + "       video.title        AS title, "
                + "       user.profile_image AS profile_image_address, "
                + "       user.name          AS uploader, "
                + "       video.views        AS views, "
                + "       video.created_at   AS created_at "
                + "FROM   video "
                + "       JOIN user "
                + "         ON video.uploader_id = user.id "
                + "LIMIT  5;";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return this.jdbcTemplate.query(retrieveVideoListQuery,
                (rs,rowNum) -> new GetVideosListRes(
                        rs.getString("thumbnail_address"),
                        rs.getString("title"),
                        rs.getString("profile_image_address"),
                        rs.getString("uploader"),
                        rs.getLong("views"),
                        dateFormat.format(rs.getTimestamp("created_at"))
                        )
                );
    }
    
    // 비디오 상세 정보 가져오기
    public GetVideosDetailRes retrieveVideoDetail(long video_id) {
        String retrieveVideoDetailQuery = ""
                + "SELECT video.location      AS location, "
                + "       video.title         AS title, "
                + "       video.views         AS views, "
                + "       video.created_at    AS created_at, "
                + "       video.likes         AS likes, "
                + "       user.profile_image  AS profile_image, "
                + "       user.name           AS uploader_name, "
                + "       channel.subscribers AS subscribers, "
                + "       video.description   AS description "
                + "FROM   video "
                + "       JOIN channel "
                + "         ON video.uploader_id = channel.owner "
                + "       JOIN user "
                + "         ON video.uploader_id = user.id "
                + "WHERE  video.id = ? ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return this.jdbcTemplate.queryForObject(retrieveVideoDetailQuery,
                (rs,rowNum) -> new GetVideosDetailRes(
                        rs.getString("location"),
                        rs.getString("title"),
                        rs.getLong("views"),
                        dateFormat.format(rs.getTimestamp("created_at")),
                        rs.getLong("likes"),
                        rs.getString("profile_image"),
                        rs.getString("uploader_name"),
                        rs.getLong("subscribers"),
                        rs.getString("description")),
                        video_id);
    }

    // 동영상 좋아요/싫어요 평가 생성
    public PostVideosEvaRes createEvaluationVideo(PostVideosEvaReq postVideosEvaReq) {
        String createDiscriminateVideoQuery = "" +
                "insert into like_hate_video " +
                "(user_id, video_id, evaluation) " +
                "VALUES (?, ?, ?)";
        long user_id = postVideosEvaReq.getUser_id();
        long video_id = postVideosEvaReq.getVideo_id();
        Object[] createDiscriminateParams = new Object[]{
                user_id,
                video_id,
                postVideosEvaReq.getEvaluation()
        };
        this.jdbcTemplate.update(createDiscriminateVideoQuery, createDiscriminateParams);

        String retrieveEvaluationVideoQuery = "" +
                "select user_id, video_id, evaluation " +
                "from like_hate_video " +
                "where user_id = ? and video_id = ?";
        return this.jdbcTemplate.queryForObject(retrieveEvaluationVideoQuery,
                (rs, rowNum) -> new PostVideosEvaRes(
                        rs.getLong("user_id"),
                        rs.getLong("video_id"),
                        rs.getInt("evaluation")),
                new Object[]{user_id, video_id});
    }
    
    // 좋아요 / 싫어요 / 취소 평가 업데이트
    public PatchVideosEvaRes updateEvaluationVideo(PatchVideosEvaReq patchVideosEvaReq) {
        String updateEvaluationVideoQuery = ""
                + "UPDATE like_hate_video "
                + "SET    evaluation = ? "
                + "WHERE  user_id = ? "
                + "       AND video_id = ?";
        long user_id = patchVideosEvaReq.getUser_id();
        long video_id = patchVideosEvaReq.getVideo_id();
        Object[] updateDiscriminateParams = new Object[]{
                patchVideosEvaReq.getEvaluation(),
                user_id,
                video_id
        };
        this.jdbcTemplate.update(updateEvaluationVideoQuery, updateDiscriminateParams);

        String retrieveEvaluationVideoQuery = "" +
                "select user_id, video_id, evaluation " +
                "from like_hate_video " +
                "where user_id = ? and video_id = ?";
        return this.jdbcTemplate.queryForObject(retrieveEvaluationVideoQuery,
                (rs, rowNum) -> new PatchVideosEvaRes(
                        rs.getLong("user_id"),
                        rs.getLong("video_id"),
                        rs.getInt("evaluation")),
                new Object[]{user_id, video_id});
    }

    // 비디오 업로드
    public PostVideoRes createVideo(PostVideoReq postVideoReq) {
        String createVideoQuery = ""
                + "INSERT INTO video "
                + "            (title, "
                + "             uploader_id, "
                + "             thumbnail, "
                + "             location, "
                + "             description) "
                + "VALUES      (?, "
                + "             ?, "
                + "             ?, "
                + "             ?, "
                + "             ?);";
        Object[] createVideoParams = new Object[]{
                postVideoReq.getTitle(),
                postVideoReq.getUploader_id(),
                postVideoReq.getThumbnail(),
                postVideoReq.getLocation(),
                postVideoReq.getDescription()
        };
        this.jdbcTemplate.update(createVideoQuery, createVideoParams);

        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        String retrieveVideoQuery = "" +
                "select id, title, uploader_id, thumbnail " +
                "from video " +
                "where id = ?";
        return this.jdbcTemplate.queryForObject(retrieveVideoQuery,
                (rs, rowNum) -> new PostVideoRes(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getLong("uploader_id"),
                        rs.getString("thumbnail")),
                recentId);
    }
}
package shop.chana123.src.playlist;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.chana123.src.playlist.model.*;
import shop.chana123.src.playlist.model.PostPlaylistReq;
import shop.chana123.src.playlist.model.PostPlaylistRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;



@Repository
public class PlaylistDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 재생목록 상세정보 조회하기
    public GetPlaylistDetailRes retrievePlaylistDetail(long playlist_id) {
        String retrievePlaylistElemQuery = ""
                + "SELECT @rownum := @rownum + 1 AS row_num, "
                + "       result.* "
                + "FROM   (SELECT video.thumbnail AS thumbnail, "
                + "               video.title     AS title, "
                + "               user.name       AS user_name "
                + "        FROM   playlist_video "
                + "               JOIN video "
                + "                 ON video.id = playlist_video.video_id "
                + "               JOIN user "
                + "                 ON user.id = video.uploader_id "
                + "        WHERE  playlist_video.playlist_id = ?) result "
                + "WHERE  ( @rownum := 0 ) = 0;";
        playlist_id = 1;
        List<GetPlaylistElem> playlistElems = this.jdbcTemplate.query(retrievePlaylistElemQuery,
                (rs, rowNum) -> new GetPlaylistElem(
                        rs.getInt("row_num"),
                        rs.getString("thumbnail"),
                        rs.getString("title"),
                        rs.getString("user_name")),
                playlist_id);
        String retrievePlaylistDetailQuery = ""
                + "SELECT video.thumbnail      AS playlist_thumbnail, "
                + "       playlist.name        AS playlist_name, "
                + "       forcount.count       AS count_videos, "
                + "       playlist.updated_at  AS updated_at, "
                + "       playlist.description AS description, "
                + "       user.profile_image   AS profile_image, "
                + "       user.name            AS user_name "
                + "FROM   playlist "
                + "JOIN "
                + "       ( "
                + "              SELECT id "
                + "              FROM   playlist "
                + "              WHERE  id = ?) target "
                + "JOIN "
                + "       ( "
                + "                SELECT   * "
                + "                FROM     playlist_video "
                + "                ORDER BY playlist_video.updated_at "
                + "                LIMIT    1) forthumbnail "
                + "ON     forthumbnail.playlist_id = playlist.id "
                + "JOIN   video "
                + "ON     forthumbnail.video_id = video.id "
                + "JOIN "
                + "       ( "
                + "                SELECT   playlist_id, "
                + "                         Count(*) AS count "
                + "                FROM     playlist_video "
                + "                GROUP BY playlist_id) forcount "
                + "ON     forcount.playlist_id = target.id "
                + "JOIN   user "
                + "ON     user.id = playlist.user_id";
        return this.jdbcTemplate.queryForObject(retrievePlaylistDetailQuery,
                (rs, rowNum) -> new GetPlaylistDetailRes(
                        rs.getString("playlist_thumbnail"),
                        rs.getString("playlist_name"),
                        rs.getInt("count_videos"),
                        rs.getString("updated_at"),
                        rs.getString("description"),
                        rs.getString("profile_image"),
                        rs.getString("user_name"),
                        playlistElems),
                playlist_id);

    }

    // 재생목록 만들기
    public PostPlaylistRes createPlaylist(PostPlaylistReq postPlaylistReq){
        String createPlaylistQuery = "insert into playlist (user_id, name, type) VALUES (?,?,?)";
        Object[] createPlaylistParams = new Object[]{postPlaylistReq.getUser_id(), postPlaylistReq.getPl_name(), postPlaylistReq.getType()};
        this.jdbcTemplate.update(createPlaylistQuery, createPlaylistParams);

        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        String getPlaylistQuery =
                "select * " +
                "from playlist " +
                "where id = ? ";

        return this.jdbcTemplate.queryForObject(getPlaylistQuery,
                (rs, rowNum) -> new PostPlaylistRes(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("type")),
                recentId);
    }

    // 재생목록 보관하기(담기)
    public PostPlaylistHaveRes createPlaylistHave(PostPlaylistHaveReq postPlaylistHaveReq){
        String createPlaylistHaveQuery = "insert into playlist_have (user_id, playlist_id) VALUES (?,?)";
        Object[] createPlaylistHaveParams = new Object[]{
                postPlaylistHaveReq.getUser_id(),
                postPlaylistHaveReq.getPlaylist_id()
        };
        this.jdbcTemplate.update(createPlaylistHaveQuery, createPlaylistHaveParams);

        String retrievePlaylistHaveQuery =
                "select user_id, playlist_id, created_at " +
                "from playlist_have " +
                "where user_id = ? and playlist_id = ? ";
        Object[] retrievePlaylistHaveParams = new Object[]{
                postPlaylistHaveReq.getUser_id(),
                postPlaylistHaveReq.getPlaylist_id()
        };
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return this.jdbcTemplate.queryForObject(retrievePlaylistHaveQuery,
                (rs, rowNum) -> new PostPlaylistHaveRes(
                        rs.getLong("user_id"),
                        rs.getLong("playlist_id"),
                        dateFormat.format(rs.getTimestamp("created_at"))),
                        retrievePlaylistHaveParams);
    }

    // 특정 재생목록에 동영상 담기
    public PostPlaylistVideoRes createPlaylistVideo(PostPlaylistVideoReq postPlaylistVideoReq){
        String createPlaylistVideoQuery = "insert into playlist_video (playlist_id, video_id) VALUES (?,?)";
        Object[] createPlaylistVideoParams = new Object[]{
                postPlaylistVideoReq.getPlaylist_id(),
                postPlaylistVideoReq.getVideo_id()
        };
        this.jdbcTemplate.update(createPlaylistVideoQuery, createPlaylistVideoParams);

        String retrievePlaylistVideoQuery =
                "select playlist_id, video_id " +
                        "from playlist_video " +
                        "where playlist_id = ? and video_id = ? ";
        Object[] retrievePlaylistVideoParams = new Object[]{
                postPlaylistVideoReq.getPlaylist_id(),
                postPlaylistVideoReq.getVideo_id()
        };
        return this.jdbcTemplate.queryForObject(retrievePlaylistVideoQuery,
                (rs, rowNum) -> new PostPlaylistVideoRes(
                        rs.getLong("playlist_id"),
                        rs.getLong("video_id")),
                retrievePlaylistVideoParams);
    }

    // 유저가 담은 재생목록 조회
    public List<GetPlaylistHaveRes> retrievePlaylistHave(long user_id) {
        String retrievePlaylistHaveQuery = ""
                + "SELECT playlist_have.user_id     AS user_id, "
                + "       playlist_have.playlist_id AS playlist_id, "
                + "       playlist.name             AS playlist_name, "
                + "       playlist.type             AS type "
                + "FROM   playlist_have "
                + "       JOIN playlist "
                + "         ON playlist_have.playlist_id = playlist.id "
                + "WHERE  playlist_have.user_id = ?";

        return this.jdbcTemplate.query(retrievePlaylistHaveQuery,
                (rs,rowNum) -> new GetPlaylistHaveRes(
                        rs.getLong("user_id"),
                        rs.getLong("playlist_id"),
                        rs.getString("playlist_name"),
                        rs.getInt("type")),
                        user_id);

    }
}
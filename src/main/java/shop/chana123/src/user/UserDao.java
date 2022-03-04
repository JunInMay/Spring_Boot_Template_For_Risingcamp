package shop.chana123.src.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.chana123.src.playlist.model.PostPlaylistReq;
import shop.chana123.src.user.model.*;
import shop.chana123.src.playlist.PlaylistDao;


import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 연결 체크
    public int connectionCheck(){
        return this.jdbcTemplate.queryForObject("select 1", int.class);
    }

    // 유저 존재 체크
    public int userExistCheck(Long user_id) {
        String userExistCheckByIdQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   user "
                + "               WHERE  id = ?) AS exist;";
        int result = this.jdbcTemplate.queryForObject(userExistCheckByIdQuery, int.class, user_id);
        return result;
    }
    public int userExistCheck(String login_id) {
        String userExistCheckByIdQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   user "
                + "               WHERE  login_id = ?) AS exist;";
        int result = this.jdbcTemplate.queryForObject(userExistCheckByIdQuery, int.class, login_id);
        return result;
    }

    // 모든 유저 리스트 가져오기
    public List<GetUserRes> getUsers(){
        String getUsersQuery = "" +
                "select * " +
                "from user";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getLong("id"),
                        rs.getString("login_id"),
                        rs.getString("name"),
                        rs.getDate("birthdate"),
                        rs.getString("profile_image"))
                );
    }

    // 특정 유저 가져오기
    public GetUserRes getUser(Long user_id){
        String getUserQuery = "" +
                "select * " +
                "from user " +
                "where id = ?";
        Long getUserParam = user_id;

        System.out.println("asdfkljadsfjkl");
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getLong("id"),
                        rs.getString("login_id"),
                        rs.getString("name"),
                        rs.getDate("birthdate"),
                        rs.getString("profile_image")),
                getUserParam);
    }

    // 회원 가입

    public PostSignupRes createUser(PostSignupReq postSignupReq) {

        String createUserQuery;
        Object[] createUserParams;
        if(postSignupReq.getProfile_image() == null) {
            createUserQuery = "insert into user (login_id, name, birthdate) VALUES (?,?,?)";
            createUserParams = new Object[]{postSignupReq.getLogin_id(), postSignupReq.getName(), postSignupReq.getBirthdate()};
        } else {
            createUserQuery = "insert into user (login_id, name, birthdate, profile_image) VALUES (?,?,?,?)";
            createUserParams = new Object[]{postSignupReq.getLogin_id(), postSignupReq.getName(), postSignupReq.getBirthdate(), postSignupReq.getProfile_image()};
        }
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        String getUserQuery = "" +
                "select id " +
                "from user " +
                "where id = ?";
        PostSignupRes postSignupRes = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new PostSignupRes(
                        rs.getLong("id")),
                recentId);

        return postSignupRes;
    }



}
package shop.chana123.src.subscribe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.chana123.src.subscribe.model.*;
import shop.chana123.src.video.model.GetVideosListRes;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class SubscribeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int alreadySubscribedCheck(Long user_id, Long channel_id) {
        String alreadySubscribedCheckQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   subscribe "
                + "               WHERE  user_id = ? and channel_id = ?) AS exist;";
        int result = this.jdbcTemplate.queryForObject(alreadySubscribedCheckQuery, int.class, new Object[]{user_id, channel_id});
        return result;
    }
    public int isSubscribedCheck(Long user_id, Long channel_id) {
        String isSubscribedCheckQuery = ""
                + "SELECT is_subscribed "
                + "FROM   subscribe "
                + "WHERE  user_id = ? and channel_id = ?;";
        int result = this.jdbcTemplate.queryForObject(isSubscribedCheckQuery, int.class, new Object[]{user_id, channel_id});
        return result;
    }

    // 구독하기
    public PostSubscribeRes createSubscription(PostSubscribeReq postSubscribeReq) {
        String createSubscriptionQuery = "" +
                "insert into subscribe " +
                "(user_id, channel_id) " +
                "VALUES (?, ?)";

        long user_id = postSubscribeReq.getUser_id();
        long channel_id = postSubscribeReq.getChannel_id();
        Object[] subscriptionParams = new Object[]{user_id, channel_id};
        this.jdbcTemplate.update(createSubscriptionQuery, subscriptionParams);

        String retrieveSubscriptionQuery = "" +
                "select user_id, channel_id, is_subscribed " +
                "from subscribe " +
                "where user_id = ? and channel_id = ?";
        return this.jdbcTemplate.queryForObject(retrieveSubscriptionQuery,
                (rs, rowNum) -> new PostSubscribeRes(
                        rs.getLong("user_id"),
                        rs.getLong("channel_id"),
                        rs.getInt("is_subscribed")),
                subscriptionParams);

    }

    // 구독 정보 변경
    public PatchSubscribeRes updateSubscription(PatchSubscribeReq patchSubscribeReq) {
        String updateSubscriptionQuery = "" +
                "update subscribe " +
                "set is_subscribed = ? " +
                "where user_id = ? and channel_id = ? ";

        long user_id = patchSubscribeReq.getUser_id();
        long channel_id = patchSubscribeReq.getChannel_id();
        int status = patchSubscribeReq.getStatus();
        Object[] subscriptionParams = new Object[]{status, user_id, channel_id};
        this.jdbcTemplate.update(updateSubscriptionQuery, subscriptionParams);

        String retrieveSubscriptionQuery = "" +
                "select user_id, channel_id, is_subscribed " +
                "from subscribe " +
                "where user_id = ? and channel_id = ?";
        return this.jdbcTemplate.queryForObject(retrieveSubscriptionQuery,
                (rs, rowNum) -> new PatchSubscribeRes(
                        rs.getLong("user_id"),
                        rs.getLong("channel_id"),
                        rs.getInt("is_subscribed")),
                new Object[]{user_id, channel_id});
    }

    // 구독한 채널리스트 뽑아오기
    public List<GetSubscribeListRes> retrieveSubscriptionList(long user_id) {
        String retrieveSubscriptionListQuery = ""
                + "SELECT user.profile_image AS profile_image, "
                + "       channel.name       AS channel_name "
                + "FROM   subscribe "
                + "       JOIN channel "
                + "         ON channel.owner = subscribe.channel_id "
                + "       JOIN user "
                + "         ON channel.owner = user.id "
                + "WHERE  subscribe.user_id = ? "
                + "       AND subscribe.is_subscribed = 1;";

        return this.jdbcTemplate.query(retrieveSubscriptionListQuery,
                (rs,rowNum) -> new GetSubscribeListRes(
                        rs.getString("profile_image"),
                        rs.getString("channel_name")),
                user_id);
    }
}
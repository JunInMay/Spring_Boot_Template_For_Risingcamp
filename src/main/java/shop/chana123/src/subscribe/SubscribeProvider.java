package shop.chana123.src.subscribe;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.subscribe.SubscribeDao;
import shop.chana123.src.subscribe.model.GetSubscribeListRes;
import shop.chana123.src.subscribe.model.PatchSubscribeRes;
import shop.chana123.src.user.UserDao;
import shop.chana123.utils.JwtService;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.DATABASE_ERROR;
import static shop.chana123.config.BaseResponseStatus.DATABASE_QUERY_ERROR;

@Service
public class SubscribeProvider {

    private final SubscribeDao subscribeDao;
    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SubscribeProvider(SubscribeDao subscribeDao, JwtService jwtService, UserDao userDao) {
        this.subscribeDao = subscribeDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }


    public List<GetSubscribeListRes> retrieveSubscriptionList(long user_id) throws BaseException {


        try{
            List<GetSubscribeListRes> getSubscribeListRes = subscribeDao.retrieveSubscriptionList(user_id);
            return getSubscribeListRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
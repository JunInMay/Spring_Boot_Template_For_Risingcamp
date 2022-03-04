package shop.chana123.src.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.subscribe.model.PatchSubscribeReq;
import shop.chana123.src.subscribe.model.PatchSubscribeRes;
import shop.chana123.src.subscribe.model.PostSubscribeReq;
import shop.chana123.src.subscribe.model.PostSubscribeRes;
import shop.chana123.src.user.UserDao;
import shop.chana123.utils.JwtService;

import static shop.chana123.config.BaseResponseStatus.*;
import static shop.chana123.config.BaseResponseStatus.VALIDATION_QUERY_ERROR;


@Service
public class SubscribeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final shop.chana123.src.subscribe.SubscribeDao subscribeDao;
    private final SubscribeProvider subscribeProvider;
    private final JwtService jwtService;
    private final UserDao userDao;

    @Autowired
    public SubscribeService(SubscribeDao subscribeDao, SubscribeProvider subscribeProvider, JwtService jwtService, UserDao userDao) {
        this.subscribeDao = subscribeDao;
        this.subscribeProvider = subscribeProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }
    // validation 처리를 위한 메소드
    public void checkUserChannel(Long user_id, Long channel_id) throws BaseException {
        // 존재하는 유저인지, 채널인지 확인
        try{
            int res = userDao.userExistCheck(user_id);
            if (res == 0) {
                throw new BaseException(NOT_EXISTS_USER);
            }
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(VALIDATION_QUERY_ERROR);
        }
        try{
            int res = userDao.userExistCheck(channel_id);
            if (res == 0) {
                throw new BaseException(NOT_EXISTS_CHANNEL);
            }
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(VALIDATION_QUERY_ERROR);
        }
    }

    public PostSubscribeRes createSubscription(PostSubscribeReq postSubscribeReq) throws BaseException {
        checkUserChannel(postSubscribeReq.getUser_id(), postSubscribeReq.getChannel_id());
        // 이미 구독한 채널인지 확인
        try{
            int res = subscribeDao.alreadySubscribedCheck(postSubscribeReq.getUser_id(), postSubscribeReq.getChannel_id());
            if (res == 1) {
                throw new BaseException(POST_SUBSCRIBE_ALREADY_SUBSCRIBED);
            }
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(VALIDATION_QUERY_ERROR);
        }
        // 메인 쿼리
        try{
            PostSubscribeRes postSubscribeRes = subscribeDao.createSubscription(postSubscribeReq);
            return postSubscribeRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchSubscribeRes updateSubscription(PatchSubscribeReq patchSubscribeReq) throws BaseException {
        checkUserChannel(patchSubscribeReq.getUser_id(), patchSubscribeReq.getChannel_id());
        // 구독한 상태인지 확인
        try{
            int res = subscribeDao.alreadySubscribedCheck(patchSubscribeReq.getUser_id(), patchSubscribeReq.getChannel_id());
            if (res == 0) {
                throw new BaseException(PATCH_SUBSCRIBE_NOT_SUBSCRIBED);
            }
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(VALIDATION_QUERY_ERROR);
        }

        try{
            int res = subscribeDao.isSubscribedCheck(patchSubscribeReq.getUser_id(), patchSubscribeReq.getChannel_id());
            if (res == patchSubscribeReq.getStatus()) {
                throw new BaseException(PATCH_SUBSCRIBE_STATUS_IS_SAME);
            }
            PatchSubscribeRes patchSubscribeRes = subscribeDao.updateSubscription(patchSubscribeReq);
            return patchSubscribeRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (BaseException baseException) {
            throw new BaseException(baseException.getStatus());
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
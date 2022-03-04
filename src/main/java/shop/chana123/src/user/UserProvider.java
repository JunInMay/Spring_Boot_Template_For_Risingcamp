package shop.chana123.src.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.user.model.*;
import shop.chana123.utils.JwtService;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.*;

@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    // 여러 유저 가져오기
    public List<GetUserRes> getUsers() throws BaseException {
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 특정 유저 가져오기
    public GetUserRes getUser(Long user_id) throws BaseException {
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
            GetUserRes getUserRes = userDao.getUser(user_id);
            System.out.println("asdf");
            return getUserRes;
        } catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            // 쿼리 실패 에러 날리기
            throw new BaseException(GET_FAIL_USER);
        } catch (Exception exception) {

            throw new BaseException(DATABASE_ERROR);
        }
    }


}
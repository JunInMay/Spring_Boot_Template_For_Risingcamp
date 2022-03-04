package shop.chana123.src.reference;



import shop.chana123.config.BaseException;
import shop.chana123.config.secret.Secret;
import shop.chana123.src.reference.model.PatchUserReq;
import shop.chana123.src.reference.model.PostUserReq;
import shop.chana123.src.reference.model.PostUserRes;
import shop.chana123.utils.AES128;
import shop.chana123.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static shop.chana123.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class RefService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RefDao refDao;
    private final RefProvider refProvider;
    private final JwtService jwtService;


    @Autowired
    public RefService(RefDao refDao, RefProvider refProvider, JwtService jwtService) {
        this.refDao = refDao;
        this.refProvider = refProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(refProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = refDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = refDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

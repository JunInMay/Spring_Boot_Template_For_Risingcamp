package shop.chana123.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.playlist.PlaylistDao;
import shop.chana123.src.playlist.model.PostPlaylistReq;
import shop.chana123.src.playlist.model.PostPlaylistRes;
import shop.chana123.src.user.model.PostSignupReq;
import shop.chana123.src.user.model.PostSignupRes;
import shop.chana123.utils.JwtService;


import static shop.chana123.config.BaseResponseStatus.*;


@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private final PlaylistDao playlistDao;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService, PlaylistDao playlistDao) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
        this.playlistDao = playlistDao;
    }


    // 회원가입
    public PostSignupRes createUser(PostSignupReq postSignupReq) throws BaseException{
        // 중복되는 이메일인지 확인
        try{
            int res = userDao.userExistCheck(postSignupReq.getLogin_id());
            if (res == 1) {
                throw new BaseException(POST_USERS_EXISTS_EMAIL);
            }
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(VALIDATION_QUERY_ERROR);
        }

        try{
            PostSignupRes postSignupRes = userDao.createUser(postSignupReq);
            PostPlaylistReq postPlaylistWatchLaterReq = new PostPlaylistReq(
                    postSignupRes.getUser_id(),
                    "WatchLater",
                    1
            );
            PostPlaylistReq postPlaylistVideoLikedReq = new PostPlaylistReq(
                    postSignupRes.getUser_id(),
                    "LikedVideo",
                    2
            );
            PostPlaylistRes postPlaylistWatchLaterRes = playlistDao.createPlaylist(postPlaylistWatchLaterReq);
            PostPlaylistRes postPlaylistVideoLikedRes = playlistDao.createPlaylist(postPlaylistVideoLikedReq);

            return postSignupRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(POST_FAIL_SIGNUP);
        }
    }
}
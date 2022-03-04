package shop.chana123.src.playlist;

import org.springframework.dao.InvalidDataAccessResourceUsageException;
import shop.chana123.config.BaseException;
import shop.chana123.src.playlist.model.*;
import shop.chana123.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static shop.chana123.config.BaseResponseStatus.*;


@Service
public class PlaylistService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlaylistDao playlistDao;
    private final PlaylistProvider playlistProvider;
    private final JwtService jwtService;

    @Autowired
    public PlaylistService(PlaylistDao playlistDao, PlaylistProvider playlistProvider, JwtService jwtService) {
        this.playlistDao = playlistDao;
        this.playlistProvider = playlistProvider;
        this.jwtService = jwtService;
    }

    //POST
    // 재생목록 만들기
    public PostPlaylistRes createPlaylist(PostPlaylistReq postPlaylistReq) throws BaseException {
        try{
            PostPlaylistRes postPlaylistRes = playlistDao.createPlaylist(postPlaylistReq);
            return postPlaylistRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 재생목록 보관하기
    public PostPlaylistHaveRes createPlaylistHave(PostPlaylistHaveReq postPlaylistHaveReq) throws BaseException {
        try{
            PostPlaylistHaveRes postPlaylistHaveRes = playlistDao.createPlaylistHave(postPlaylistHaveReq);
            return postPlaylistHaveRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 재생목록에 동영상 담기
    public PostPlaylistVideoRes createPlaylistVideo(PostPlaylistVideoReq postPlaylistVideoReq) throws BaseException {
        try{
            PostPlaylistVideoRes postPlaylistVideoRes = playlistDao.createPlaylistVideo(postPlaylistVideoReq);
            return postPlaylistVideoRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
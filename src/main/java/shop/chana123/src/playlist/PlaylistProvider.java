package shop.chana123.src.playlist;


import org.springframework.dao.InvalidDataAccessResourceUsageException;
import shop.chana123.config.BaseException;
import shop.chana123.src.playlist.model.GetPlaylistDetailRes;
import shop.chana123.src.playlist.model.GetPlaylistHaveRes;
import shop.chana123.src.video.model.GetVideosListRes;
import shop.chana123.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.DATABASE_ERROR;
import static shop.chana123.config.BaseResponseStatus.DATABASE_QUERY_ERROR;

@Service
public class PlaylistProvider {

    private final PlaylistDao playlistDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PlaylistProvider(PlaylistDao playlistDao, JwtService jwtService) {
        this.playlistDao = playlistDao;
        this.jwtService = jwtService;
    }

    // 유저가 보관한 재생목록 가져오기
    public List<GetPlaylistHaveRes> retrievePlaylistHave(long user_id) throws BaseException {
        try{
            List<GetPlaylistHaveRes> getPlaylistHaveRes = playlistDao.retrievePlaylistHave(user_id);
            return getPlaylistHaveRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPlaylistDetailRes retrievePlaylistDetail(long playlist_id) throws BaseException {
        try{
            GetPlaylistDetailRes getPlaylistDetailRes = playlistDao.retrievePlaylistDetail(playlist_id);
            return getPlaylistDetailRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
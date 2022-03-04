package shop.chana123.src.video;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.video.model.*;
import shop.chana123.utils.JwtService;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.DATABASE_ERROR;
import static shop.chana123.config.BaseResponseStatus.DATABASE_QUERY_ERROR;

@Service
public class VideoProvider {

    private final VideoDao videoDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public VideoProvider(VideoDao videoDao, JwtService jwtService) {
        this.videoDao = videoDao;
        this.jwtService = jwtService;
    }

    // 비디오 리스트 가져오기
    public List<GetVideosListRes> retrieveVideoList() throws BaseException {
        try{
            List<GetVideosListRes> getVideosListRes = videoDao.retrieveVideoList();
            return getVideosListRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 비디오 상세 정보 가져오기
    public GetVideosDetailRes retrieveVideoDetail(long video_id) throws BaseException {
        try{
            GetVideosDetailRes getVideosDetailRes = videoDao.retrieveVideoDetail(video_id);
            return getVideosDetailRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}













































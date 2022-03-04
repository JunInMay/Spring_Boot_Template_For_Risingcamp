package shop.chana123.src.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.video.model.*;
import shop.chana123.utils.JwtService;

import static shop.chana123.config.BaseResponseStatus.*;


@Service
public class VideoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VideoDao videoDao;
    private final VideoProvider videoProvider;
    private final JwtService jwtService;

    @Autowired
    public VideoService(VideoDao videoDao, VideoProvider videoProvider, JwtService jwtService) {
        this.videoDao = videoDao;
        this.videoProvider = videoProvider;
        this.jwtService = jwtService;
    }
    // 동영상 좋아요/싫어요 평가 생성
    public PostVideosEvaRes createEvaluationVideo(PostVideosEvaReq postVideosEvaReq) throws BaseException{
        try{
            PostVideosEvaRes postVideosEvaRes = videoDao.createEvaluationVideo(postVideosEvaReq);
            return postVideosEvaRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 동영상 좋아요/싫어요 평가 업데이트
    public PatchVideosEvaRes updateEvaluationVideo(PatchVideosEvaReq patchVideosEvaReq) throws BaseException{
        try{
            PatchVideosEvaRes patchVideosEvaRes = videoDao.updateEvaluationVideo(patchVideosEvaReq);
            return patchVideosEvaRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostVideoRes postUploadVideo(PostVideoReq postVideoReq) throws BaseException{
        try{
            PostVideoRes postVideoRes = videoDao.createVideo(postVideoReq);
            return postVideoRes;
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
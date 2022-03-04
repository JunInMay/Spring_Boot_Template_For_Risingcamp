package shop.chana123.src.comment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.comment.model.GetCommentsCommentlistRes;
import shop.chana123.src.comment.model.GetCommentsRecommentlistRes;
import shop.chana123.src.comment.model.PostCommentsRes;
import shop.chana123.utils.JwtService;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.DATABASE_ERROR;
import static shop.chana123.config.BaseResponseStatus.DATABASE_QUERY_ERROR;

@Service
public class CommentProvider {

    private final CommentDao commentDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommentProvider(CommentDao commentDao, JwtService jwtService) {
        this.commentDao = commentDao;
        this.jwtService = jwtService;
    }


    public List<GetCommentsCommentlistRes> retrieveCommentlist(long video_id) throws BaseException {
        try{
            List<GetCommentsCommentlistRes> getCommentsCommentlistRes = commentDao.retrieveCommentList(video_id);
            return getCommentsCommentlistRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetCommentsRecommentlistRes> retrieveRecommentlist(long comment_id) throws BaseException{
        try{
            List<GetCommentsRecommentlistRes> getRecommentsCommentlistRes = commentDao.retrieveRecommentList(comment_id);
            return getRecommentsCommentlistRes;
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
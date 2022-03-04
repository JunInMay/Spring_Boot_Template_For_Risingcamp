package shop.chana123.src.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import shop.chana123.config.BaseException;
import shop.chana123.src.comment.model.*;
import shop.chana123.src.video.model.PostVideoRes;
import shop.chana123.utils.JwtService;

import static shop.chana123.config.BaseResponseStatus.DATABASE_ERROR;
import static shop.chana123.config.BaseResponseStatus.DATABASE_QUERY_ERROR;


@Service
public class CommentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;
    private final JwtService jwtService;

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider, JwtService jwtService) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
        this.jwtService = jwtService;
    }

    // 댓글 만들기
   public PostCommentsRes createComment(PostCommentsReq postCommentsReq) throws BaseException {
        try{
            PostCommentsRes postCommentsRes = commentDao.createComment(postCommentsReq);
            return postCommentsRes;
        }
        catch (InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException) {
            throw new BaseException(DATABASE_QUERY_ERROR);
        }
        catch (Exception exception) {
            // DB 연결 실패 에러 날리기
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostCommentsEvaRes createEvaluationComment(PostCommentsEvaReq postCommentsEvaReq) throws BaseException {
        try{
            PostCommentsEvaRes postCommentsEvaRes = commentDao.createEvaluationComment(postCommentsEvaReq);
            return postCommentsEvaRes;
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
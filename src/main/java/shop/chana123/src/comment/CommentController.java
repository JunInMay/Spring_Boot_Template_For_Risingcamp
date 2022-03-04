package shop.chana123.src.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import shop.chana123.config.BaseException;
import shop.chana123.config.BaseResponse;
import shop.chana123.src.comment.model.*;
import shop.chana123.src.user.model.GetUserRes;
import shop.chana123.src.video.model.PostVideosEvaReq;
import shop.chana123.src.video.model.PostVideosEvaRes;
import shop.chana123.utils.JwtService;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.COMMENTS_AMBIGUOUS_RECOMMENT_OR_NOT;
import static shop.chana123.config.BaseResponseStatus.POST_USERS_EMPTY_EMAIL;


@RestController
@RequestMapping("/app/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService) {
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }
    // 코멘트 좋아요/싫어요 평가 생성
    @ResponseBody
    @PostMapping("/eva") // (POST) 127.0.0.1:9000/app/videos/dis
    public BaseResponse<PostCommentsEvaRes> postCommentsEva(@RequestBody PostCommentsEvaReq postCommentsEvaReq) {
        try{
            PostCommentsEvaRes postCommentsEvaRes = commentService.createEvaluationComment(postCommentsEvaReq);
            return new BaseResponse<>(postCommentsEvaRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 코멘트 리스트 가져오기
    @ResponseBody
    @GetMapping("/commentlist/{video_id}") // (GET) 127.0.0.1:9000/app/comments/commentlist/{video_id}
    public BaseResponse<List<GetCommentsCommentlistRes>> getCommentsCommentlist(@PathVariable("video_id") long video_id) {
        try{
            List<GetCommentsCommentlistRes> getCommentsCommentlistRes = commentProvider.retrieveCommentlist(video_id);
            return new BaseResponse<>(getCommentsCommentlistRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    // 대댓글 리스트 가져오기
    @ResponseBody
    @GetMapping("/recommentlist/{comment_id}") // (GET) 127.0.0.1:9000/app/comments/recommentlist/{comment_id}
    public BaseResponse<List<GetCommentsRecommentlistRes>> getCommentsRecommentlist(@PathVariable("comment_id") long comment_id) {
        try{
            List<GetCommentsRecommentlistRes> getCommentsRecommentlistRes = commentProvider.retrieveRecommentlist(comment_id);
            return new BaseResponse<>(getCommentsRecommentlistRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 댓글 생성
    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/app/comments
    public BaseResponse<PostCommentsRes> postComments(@RequestBody PostCommentsReq postCommentsReq) {
        if (postCommentsReq.getIs_recomment() == 0 && postCommentsReq.getOriginal_comment_id() != 0) {
            return new BaseResponse<>(COMMENTS_AMBIGUOUS_RECOMMENT_OR_NOT);
        }
        else if (postCommentsReq.getIs_recomment() != 0 && postCommentsReq.getOriginal_comment_id() == 0) {
            return new BaseResponse<>(COMMENTS_AMBIGUOUS_RECOMMENT_OR_NOT);
        }
        try{
            PostCommentsRes postCommentsRes = commentService.createComment(postCommentsReq);
            return new BaseResponse<>(postCommentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그 테스트 API
     * [GET] /test/log
     * @return String
     */
    @ResponseBody
    @GetMapping("/log")
    public String getAll() {
        System.out.println("테스트");
//        trace, debug 레벨은 Console X, 파일 로깅 X
//        logger.trace("TRACE Level 테스트");
//        logger.debug("DEBUG Level 테스트");

//        info 레벨은 Console 로깅 O, 파일 로깅 X
        logger.info("INFO Level 테스트");
//        warn 레벨은 Console 로깅 O, 파일 로깅 O
        logger.warn("Warn Level 테스트");
//        error 레벨은 Console 로깅 O, 파일 로깅 O (app.log 뿐만 아니라 error.log 에도 로깅 됨)
//        app.log 와 error.log 는 날짜가 바뀌면 자동으로 *.gz 으로 압축 백업됨
        logger.error("ERROR Level 테스트");

        return "Success Test";
    }
}

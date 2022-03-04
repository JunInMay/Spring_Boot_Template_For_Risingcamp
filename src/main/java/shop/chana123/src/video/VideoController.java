package shop.chana123.src.video;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.chana123.config.BaseException;
import shop.chana123.config.BaseResponse;
import shop.chana123.src.video.model.*;
import shop.chana123.utils.JwtService;

import java.util.List;


@RestController
@RequestMapping("/app/videos")
public class VideoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final VideoProvider videoProvider;
    @Autowired
    private final VideoService videoService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    public VideoController(VideoProvider videoProvider, VideoService videoService, JwtService jwtService) {
        this.videoProvider = videoProvider;
        this.videoService = videoService;
        this.jwtService = jwtService;
    }

    // 추천 비디오 리스트 가져오기
    @ResponseBody
    @GetMapping("/list") // (GET) 127.0.0.1:9000/app/video/list
    public BaseResponse<List<GetVideosListRes>> getVideosList() {
        try{
            List<GetVideosListRes> getVideosListRes = videoProvider.retrieveVideoList();
            return new BaseResponse<>(getVideosListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 비디오 디테일 정보 가져오기
    @ResponseBody
    @GetMapping("/{video_id}") // (GET) 127.0.0.1:9000/app/video/list
    public BaseResponse<GetVideosDetailRes> getVideos(@PathVariable("video_id") long video_id) {
        try{
            GetVideosDetailRes getVideosDetailRes = videoProvider.retrieveVideoDetail(video_id);
            return new BaseResponse<>(getVideosDetailRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 동영상 좋아요/싫어요 평가 생성
    @ResponseBody
    @PostMapping("/eva") // (POST) 127.0.0.1:9000/app/videos/dis
    public BaseResponse<PostVideosEvaRes> postVideosEva(@RequestBody PostVideosEvaReq postVideosEvaReq) {

        try{
            PostVideosEvaRes postVideosEvaRes = videoService.createEvaluationVideo(postVideosEvaReq);
            return new BaseResponse<>(postVideosEvaRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 동영상 좋아요/싫어요/취소 평가 업데이트
    @ResponseBody
    @PatchMapping("/eva") // (PATCH) 127.0.0.1:9000/app/videos/dis
    public BaseResponse<PatchVideosEvaRes> patchVideosEva(@RequestBody PatchVideosEvaReq patchVideosEvaReq) {

        try{
            PatchVideosEvaRes patchVideosEvaRes = videoService.updateEvaluationVideo(patchVideosEvaReq);
            return new BaseResponse<>(patchVideosEvaRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 동영상 업로드
    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/app/videos/upload
    public BaseResponse<PostVideoRes> postVideos(@RequestBody PostVideoReq postVideoReq) {

        try{
            PostVideoRes postVideoRes = videoService.postUploadVideo(postVideoReq);
            return new BaseResponse<>(postVideoRes);
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

package shop.chana123.src.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.chana123.config.BaseException;
import shop.chana123.config.BaseResponse;

import shop.chana123.src.subscribe.model.*;
import shop.chana123.utils.JwtService;

import java.util.List;
import static shop.chana123.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/subscribe")
public class SubscribeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.chana123.src.subscribe.SubscribeProvider subscribeProvider;
    @Autowired
    private final shop.chana123.src.subscribe.SubscribeService subscribeService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    public SubscribeController(SubscribeProvider subscribeProvider, SubscribeService subscribeService, JwtService jwtService) {
        this.subscribeProvider = subscribeProvider;
        this.subscribeService = subscribeService;
        this.jwtService = jwtService;
    }
    
    // 유저가 구독한 채널들 목록 불러오기
    @ResponseBody
    @GetMapping("/list/{user_id}") // (GET) 127.0.0.1:9000/app/subscribe/list/{user_id}
    public BaseResponse<List<GetSubscribeListRes>> getSubscribeList(@PathVariable("user_id") long user_id) {

        try{
            List<GetSubscribeListRes> getSubscribeListRes = subscribeProvider.retrieveSubscriptionList(user_id);
            return new BaseResponse<>(getSubscribeListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    // 구독 생성
    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/app/subscribe
    public BaseResponse<PostSubscribeRes> postSubscribe(@RequestBody PostSubscribeReq postSubscribeReq) {
        try {
            if (postSubscribeReq.getUser_id() < 1) {
                return new BaseResponse<>(USERS_INVALID_USER_ID);
            }
        }
        catch(NullPointerException nullPointerException)
        {
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }

        try {
            if (postSubscribeReq.getUser_id() < 1) {
                return new BaseResponse<>(CHANNEL_INVALID_CHANNEL_ID);
            }
        }
        catch(NullPointerException nullPointerException)
        {
            return new BaseResponse<>(CHANNEL_EMPTY_CHANNEL_ID);
        }


        try{
            PostSubscribeRes postSubscribeRes = subscribeService.createSubscription(postSubscribeReq);
            return new BaseResponse<>(postSubscribeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
    // 구독 취소(구독상태 변경)
    @ResponseBody
    @PatchMapping("") // (PATCH) 127.0.0.1:9000/app/subscribe
    public BaseResponse<PatchSubscribeRes> patchSubscribe(@RequestBody PatchSubscribeReq patchSubscribeReq) {

        try {
            if (patchSubscribeReq.getUser_id() < 1) {
                return new BaseResponse<>(USERS_INVALID_USER_ID);
            }
        }
        catch(NullPointerException nullPointerException)
        {
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        try {
            if (patchSubscribeReq.getUser_id() < 1) {
                return new BaseResponse<>(CHANNEL_INVALID_CHANNEL_ID);
            }
        }
        catch(NullPointerException nullPointerException)
        {
            return new BaseResponse<>(CHANNEL_EMPTY_CHANNEL_ID);
        }
        try {
            if (patchSubscribeReq.getStatus() > 1 || patchSubscribeReq.getStatus() < 0) {
                return new BaseResponse<>(PATCH_SUBSCRIBE_INVALID_STATUS);
            }
        }
        catch(NullPointerException nullPointerException)
        {
            return new BaseResponse<>(PATCH_SUBSCRIBE_EMPTY_STATUS);
        }
        try{
            PatchSubscribeRes patchSubscribeRes = subscribeService.updateSubscription(patchSubscribeReq);
            return new BaseResponse<>(patchSubscribeRes);
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

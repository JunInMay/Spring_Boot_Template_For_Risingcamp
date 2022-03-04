package shop.chana123.src.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.chana123.config.BaseException;
import shop.chana123.config.BaseResponse;
import shop.chana123.src.user.model.*;
import shop.chana123.utils.JwtService;
import shop.chana123.utils.ValidationRegex;

import java.util.List;

import static shop.chana123.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/user")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // 유저 리스트 다 가져오기
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers() {
        try{
            List<GetUserRes> getUsersRes = userProvider.getUsers();
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 유저 1명 데이터 가져오기
    @ResponseBody
    @GetMapping("/{user_id}") // (GET) 127.0.0.1:9000/app/users/{user_id}
    public BaseResponse<GetUserRes> getUser(@PathVariable("user_id") Long user_id) {
        if (user_id < 1) {
            return new BaseResponse<>(USERS_INVALID_USER_ID);
        }
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(user_id);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/app/users
    public BaseResponse<PostSignupRes> postUserSignup(@RequestBody PostSignupReq postSignupReq) {

        // email 입력되지 않을 경우
        if(postSignupReq.getLogin_id() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현 검사
        if(!ValidationRegex.isRegexEmail(postSignupReq.getLogin_id())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //이름 검사
        if (postSignupReq.getName() == null || postSignupReq.getName() == "") {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        // 생년월일 검사
        if (postSignupReq.getBirthdate() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTHDATE);
        }

        try{
            PostSignupRes postSignupRes = userService.createUser(postSignupReq);
            return new BaseResponse<>(postSignupRes);
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

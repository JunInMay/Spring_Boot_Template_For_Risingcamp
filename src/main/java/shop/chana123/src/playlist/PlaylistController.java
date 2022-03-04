package shop.chana123.src.playlist;



import static shop.chana123.config.BaseResponseStatus.*;
import shop.chana123.src.playlist.model.*;
import shop.chana123.src.playlist.model.PostPlaylistReq;
import shop.chana123.src.playlist.model.PostPlaylistRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shop.chana123.config.BaseException;
import shop.chana123.config.BaseResponse;
import shop.chana123.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/app/pl")
public class PlaylistController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PlaylistProvider playlistProvider;
    @Autowired
    private final PlaylistService playlistService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    public PlaylistController(PlaylistProvider playlistProvider, PlaylistService playlistService, JwtService jwtService) {
        this.playlistProvider = playlistProvider;
        this.playlistService = playlistService;
        this.jwtService = jwtService;
    }

    // 재생목록 담기
    @ResponseBody
    @PostMapping("/have") // (POST) 127.0.0.1:9000/app/pl/have
    public BaseResponse<PostPlaylistHaveRes> postPlaylistHave(@RequestBody PostPlaylistHaveReq postPlaylistHaveReq) {
        try{
            PostPlaylistHaveRes postPlaylistHaveRes = playlistService.createPlaylistHave(postPlaylistHaveReq);
            return new BaseResponse<>(postPlaylistHaveRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 유저가 보관한 재생목록 조회
    @ResponseBody
    @GetMapping("/have/{user_id}") // (POST) 127.0.0.1:9000/app/videos/dis
    public BaseResponse<List<GetPlaylistHaveRes>> getPlaylistHave(@PathVariable("user_id") long user_id) {
        try{
            List<GetPlaylistHaveRes> getPlaylistHaveRes = playlistProvider.retrievePlaylistHave(user_id);
            return new BaseResponse<>(getPlaylistHaveRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    // 특정 재생목록 상세 내용 조회
    @ResponseBody
    @GetMapping("/{playlist_id}") // (POST) 127.0.0.1:9000/app/videos/dis
    public BaseResponse<GetPlaylistDetailRes> getPlaylistDetail(@PathVariable("playlist_id") long playlist_id) {
        try{
            GetPlaylistDetailRes getPlaylistDetailRes = playlistProvider.retrievePlaylistDetail(playlist_id);
            return new BaseResponse<>(getPlaylistDetailRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 재생목록 생성
    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/app/pl
    public BaseResponse<PostPlaylistRes> postPlaylist(@RequestBody PostPlaylistReq postPlaylistReq) {

        // 재생목록 이름이 비어있을 경우
        if (postPlaylistReq.getPl_name() == null || postPlaylistReq.getPl_name() == "") {
            return new BaseResponse<>(EMPTY_PLAYLIST_NAME);
        }
        // 잘못된 재생목록 타입 입력시
        if (postPlaylistReq.getType() != 0) {
            return new BaseResponse<>(FORBIDDEN_PLAYLIST_TYPE);
        }
        try{
            PostPlaylistRes postPlaylistRes = playlistService.createPlaylist(postPlaylistReq);
            return new BaseResponse<>(postPlaylistRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 재생목록에 동영상 담기
    @ResponseBody
    @PostMapping("/video") // (POST) 127.0.0.1:9000/app/pl/video
    public BaseResponse<PostPlaylistVideoRes> postPlaylistVideo(@RequestBody PostPlaylistVideoReq postPlaylistVideoReq) {
        try{
            PostPlaylistVideoRes postPlaylistVideoRes = playlistService.createPlaylistVideo(postPlaylistVideoReq);
            return new BaseResponse<>(postPlaylistVideoRes);
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

package shop.chana123.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // 유저 관련
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 입력해주세요."),
    USERS_INVALID_USER_ID(false, 2011, "유저 아이디 값이 양의 정수가 아닙니다."),

    // [POST] /users

    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_NAME(false, 2018, "이름을 입력해주세요."),
    POST_USERS_EMPTY_BIRTHDATE(false, 2019, "생년월일을 입력해주세요."),
    POST_USERS_INVALID_BIRTHDATE(false, 2020, "올바른 생년월일을 입력해주세요."),

    // 채널 관련
    CHANNEL_INVALID_CHANNEL_ID(false, 2030, "채널 아이디 값이 양의 정수가 아닙니다."),
    CHANNEL_EMPTY_CHANNEL_ID(false, 2031, "채널 아이디를 입력해주세요."),



    // [PATCH] /subscribe
    PATCH_SUBSCRIBE_INVALID_STATUS(false, 2043, "바꾸려는 구독 상태를 확인해주세요."),
    PATCH_SUBSCRIBE_EMPTY_STATUS(false, 2044, "바꾸려는 구독 상태를 입력해주세요."),

    // comments
    COMMENTS_AMBIGUOUS_RECOMMENT_OR_NOT(false,2050,"코멘트가 대댓글인지 아닌지 불분명합니다."),

    // playlist

    // [POST] /app/pl
    EMPTY_PLAYLIST_NAME(false,2065,"재생목록 이름을 입력해주세요."),
    FORBIDDEN_PLAYLIST_TYPE(false,2066,"좋아요 표시한 재생목록, 나중에 볼 동영상은 수동으로 생성할 수 없습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    DATABASE_QUERY_ERROR(false, 4002, "데이터베이스 질의문에 오류가 있습니다."),
    VALIDATION_QUERY_ERROR(false, 4003, "요청 검증을 위한 질의문에 오류가 있습니다."),

    // 존재하지 않음
    NOT_EXISTS_USER(false, 4010, "해당 정보를 가진 유저가 DB에 존재하지 않습니다."),
    NOT_EXISTS_CHANNEL(false, 4011, "해당 정보를 가진 채널이 DB에 존재하지 않습니다."),

    //[GET] /app/users/{user_id}
    GET_FAIL_USER(false, 4020, "유저를 받아오지 못했습니다."),


    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4024,"유저네임 수정 실패"),

    //[POST] /app/users/signup
    POST_FAIL_SIGNUP(false,4025,"회원가입에 실패했습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4021, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4022, "비밀번호 복호화에 실패하였습니다."),

    // [POST] /subscribe
    POST_SUBSCRIBE_ALREADY_SUBSCRIBED(false, 4030, "이미 구독한 관계입니다."),

    // [PATCH] /subscribe
    PATCH_SUBSCRIBE_NOT_SUBSCRIBED(false, 4040, "구독하지 않은 관계여서 수정할 수 없습니다."),
    PATCH_SUBSCRIBE_STATUS_IS_SAME(false, 4041, "이미 구독 상태가 바꾸려는 상태와 동일합니다."),

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    LAST(false, 9999, "마지막 에러 메시지용 입니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

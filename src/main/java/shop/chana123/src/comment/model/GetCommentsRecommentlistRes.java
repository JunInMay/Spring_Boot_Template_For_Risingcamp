package shop.chana123.src.comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsRecommentlistRes {
    private String profile_image;
    private String user_name;
    private String created_at;
    private String content;
    private long likes ;
}

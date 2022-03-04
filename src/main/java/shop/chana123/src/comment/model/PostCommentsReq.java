package shop.chana123.src.comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentsReq {
    private Long user_id;
    private String content;
    private Long video_id;
    private int is_recomment;
    private int original_comment_id;
}

package shop.chana123.src.comment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentsEvaRes {
    private Long user_id;
    private Long comment_id;
    private int evaluation;
}

package shop.chana123.src.video.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchVideosEvaReq {
    private Long user_id;
    private Long video_id;
    private int evaluation;
}

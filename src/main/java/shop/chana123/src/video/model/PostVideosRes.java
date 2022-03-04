package shop.chana123.src.video.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoRes {
    private Long video_id;
    private String title;
    private Long uploader_id;
    private String thumbnail;
}

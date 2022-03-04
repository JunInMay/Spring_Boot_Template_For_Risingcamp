package shop.chana123.src.video.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoReq {
    private String title;
    private Long uploader_id;
    private String thumbnail;
    private String location;
    private String description;
}

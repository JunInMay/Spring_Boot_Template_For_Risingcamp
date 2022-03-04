package shop.chana123.src.video.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetVideosDetailRes {
    private String location;
    private String title;
    private Long views;
    private String created_at;
    private Long likes;
    private String profile_image;
    private String uploader_name;
    private Long subscribers;
    private String description;
}

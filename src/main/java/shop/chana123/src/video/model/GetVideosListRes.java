package shop.chana123.src.video.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetVideosListRes {
    private String user_id;
    private String login_id;
    private String name;
    private String uploader;
    private Long views;
    private String created_at;
}

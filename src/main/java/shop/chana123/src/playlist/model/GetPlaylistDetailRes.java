package shop.chana123.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;



@Getter
@Setter
@AllArgsConstructor
public class GetPlaylistDetailRes {
    private String playlist_thumbnail;
    private String playlist_name;
    private int count_videos;
    private String updated_at;
    private String description;
    private String profile_image;
    private String user_name;
    private List<GetPlaylistElem> playlist_elems;
}

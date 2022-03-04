package shop.chana123.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetPlaylistElem {
    private int row_num;
    private String thumbnail;
    private String title;
    private String user_name;
}

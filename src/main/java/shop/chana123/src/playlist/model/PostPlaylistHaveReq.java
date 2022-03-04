package shop.chana123.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPlaylistHaveReq {
    private Long user_id;
    private Long playlist_id;
}

package shop.chana123.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPlaylistHaveRes {
    private Long user_id;
    private Long playlist_id;
    private String created_at;
}

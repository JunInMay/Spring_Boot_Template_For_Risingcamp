package shop.chana123.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.*;

@Getter
@Setter
@AllArgsConstructor
public class PostPlaylistRes {
    private Long pl_id;
    private String pl_name;
    private int type;
}

package shop.chana123.src.subscribe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSubscribeReq {
    private Long user_id;
    private Long channel_id;
}

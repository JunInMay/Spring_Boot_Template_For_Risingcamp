package shop.chana123.src.subscribe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchSubscribeRes {
    private Long user_id;
    private Long channel_id;
    private int is_subscribed;
}

package shop.chana123.src.reference.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String UserName;
    private String id;
    private String email;
    private String password;
}

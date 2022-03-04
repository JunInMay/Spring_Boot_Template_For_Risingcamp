package shop.chana123.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostSignupReq {
    private String login_id;
    private String name;
    private Date birthdate;
    private String profile_image;
}

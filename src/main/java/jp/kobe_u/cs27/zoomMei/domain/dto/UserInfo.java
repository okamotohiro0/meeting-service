package jp.kobe_u.cs27.zoomMei.domain.dto;

import jp.kobe_u.cs27.zoomMei.domain.entity.User;
import lombok.Data;

@Data
public class UserInfo {
    String uid;
    String nickname;

    public static UserInfo build(User user){
        UserInfo userinfo=new UserInfo();
        userinfo.uid=user.getUid();
        userinfo.nickname=user.getNickname();
        return userinfo;
    }
}

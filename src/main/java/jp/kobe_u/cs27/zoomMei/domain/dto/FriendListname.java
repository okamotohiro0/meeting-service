package jp.kobe_u.cs27.zoomMei.domain.dto;

import java.util.List;

import lombok.Data;


@Data
public class FriendListname {
    //
    private final List<FriendList> followname;

    private Long listid;

    // フォローされた人のID
    private String fid;

    // 関係値を表す(1.家族　2.友達　3.介護者)
    private int relationship;

  // フォローした人のID（自分のID）
  private String uid;
}

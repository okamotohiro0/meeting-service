package jp.kobe_u.cs27.zoomMei.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FollowerWithNickname {
    private Long listid;

    // フォローされた人のID
    private String fid;
    // フォロワーのニックネーム
    private String followerNickname;

    // 関係値を表す(1.家族　2.友達　3.介護者)
    private int relationship;

  // フォローした人のID（自分のID）
  private String uid;
}

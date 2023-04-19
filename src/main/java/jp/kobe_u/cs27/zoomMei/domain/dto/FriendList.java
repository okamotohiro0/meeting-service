package jp.kobe_u.cs27.zoomMei.domain.dto;


import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FriendList {
  //自分のIDがuidのフレンドリスト
  private final List<FriendWithNickname> logs;
  //自分のIDがfidのフレンドリスト
  private final List<FollowerWithNickname> flogs;
}
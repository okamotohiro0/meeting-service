package jp.kobe_u.cs27.zoomMei.form;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * IDでフォローするフォーム
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendForm {
  // ユーザID
  @NotNull
  private String uid;

  // フォローID
  @NotNull
  private String fid; 
  
  // 関係値
  @Min(1)
  @Max(3)
  private int relationship;

  


}

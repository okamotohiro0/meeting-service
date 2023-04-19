package jp.kobe_u.cs27.zoomMei.form;

import lombok.*;


import javax.validation.constraints.NotBlank;



/**
 * ユーザ登録・更新フォーム
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

  // ユーザID
  @NotBlank
  private String uid;

  // ニックネーム
  @NotBlank
  private String nickname;

  // パスワード
  @NotBlank
  private String password;

}

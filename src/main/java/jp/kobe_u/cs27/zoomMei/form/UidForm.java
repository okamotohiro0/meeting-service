package jp.kobe_u.cs27.zoomMei.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;



/**
 * ユーザIDフォーム
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UidForm {
      // ユーザID
  @NotBlank
  private String uid;

      // パスワード
  @NotBlank
  private String password;    
}

package jp.kobe_u.cs27.zoomMei.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ユーザエンティティ
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  
  // ユーザID
  @Id
  private String uid;

  // ニックネーム
  private String nickname;

  // パスワード
  private String password;

}

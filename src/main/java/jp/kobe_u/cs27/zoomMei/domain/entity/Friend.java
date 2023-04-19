package jp.kobe_u.cs27.zoomMei.domain.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * ユーザエンティティ
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
      // listId（自動採番・主キー)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long listid;

    // フォローされた人のID
    private String fid;

    // 関係値を表す(1.家族　2.友達　3.介護者)
    private int relationship;

  // フォローした人のID（自分のID）
  private String uid;


}

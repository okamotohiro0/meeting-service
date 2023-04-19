package jp.kobe_u.cs27.zoomMei.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;


/**
 * メンバーエンティティ
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
         // groupID（自動採番・主キー)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupid;

  // 参加者ID
  private String pid;

  // zoomid
  private Long zoomid;
  
  //参加するかどうか(デフォルトが0(参加するかどうか未定)、参加する場合は１、参加しない場合は、2(entityには残るがUIでは消える))
  private int state;

}

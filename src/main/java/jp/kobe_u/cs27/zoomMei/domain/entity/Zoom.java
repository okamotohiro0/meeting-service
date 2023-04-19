package jp.kobe_u.cs27.zoomMei.domain.entity;
import lombok.*;

import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * zoomエンティティ
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Zoom {
    
      // zoomID（自動採番・主キー)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long zoomid;

  // 主催者ID
  private String hostid;

  // スタート時間
  private LocalDateTime starttime;

  // 会議のリンク
  private String link;


}
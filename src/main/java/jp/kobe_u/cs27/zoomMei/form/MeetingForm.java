package jp.kobe_u.cs27.zoomMei.form;



import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;


/**
 * 会議を登録するフォーム
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingForm {
      // ユーザID
  @NotNull
  private String uid;
      // 参加者ID軍団
  @NotNull
  private List<String> pid;
      // 会議の日時
  @NotNull    
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
  private LocalDateTime starttime;
        

}

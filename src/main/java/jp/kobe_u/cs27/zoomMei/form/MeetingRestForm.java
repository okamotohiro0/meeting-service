package jp.kobe_u.cs27.zoomMei.form;






import javax.validation.constraints.NotNull;


import lombok.*;


/**
 * 会議を登録するフォーム
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRestForm {
      // ユーザID
  @NotNull
  private String uid;
      // 参加者ID軍団
  @NotNull
  private String[] pidnicknames;
      // 会議の日時
  @NotNull    
  // String → LocalDate
   // String → LocalDate
   private String starttime;
   
   
    //  入力例：{
    // "uid":"okamoto",
    // "pid":["hiro","aaa","bbb"],
    // "starttime":"2023-01-03 23:00"
    //   }

}

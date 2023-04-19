package jp.kobe_u.cs27.zoomMei.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;
/**
 * 会議を承認するかどうかのフォーム
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConductForm {

    //ユーザID
    @NotNull
    private String pid;

    private Long zoomid;

  
    //参加するかどうか(デフォルトが0(参加するかどうか未定)、参加する場合は１、参加しない場合は、2(entityには残るがUIでは消える))
    @Min(0)
    @Max(2)
    private int state;
    
}
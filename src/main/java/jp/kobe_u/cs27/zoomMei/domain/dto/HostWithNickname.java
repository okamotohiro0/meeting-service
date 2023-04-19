package jp.kobe_u.cs27.zoomMei.domain.dto;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class HostWithNickname {
    private Long zoomid;

    // 主催者ID
    private String hostid;
    //主催者名
    private String hostname;
  
    // スタート時間
    private LocalDateTime starttime;
  
    // 会議のリンク
    private String link;

    //招待者の名前
    private List<String> membername;

    private HashMap<String,String> states;
    
}

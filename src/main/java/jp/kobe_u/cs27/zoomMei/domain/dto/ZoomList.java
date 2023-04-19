package jp.kobe_u.cs27.zoomMei.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class ZoomList {
    //主催したmtgのログ
    private final List<HostWithNickname> meetinglogs;
    //主催されたmtgのログ
    private final List<PidWithNickname> pidLogs;

}

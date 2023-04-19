package jp.kobe_u.cs27.zoomMei.controller.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.kobe_u.cs27.zoomMei.configuration.exception.Response;
import jp.kobe_u.cs27.zoomMei.configuration.exception.ResponseCreater;
import jp.kobe_u.cs27.zoomMei.domain.dto.FriendList;
import jp.kobe_u.cs27.zoomMei.domain.dto.ZoomList;
import jp.kobe_u.cs27.zoomMei.domain.repository.UserRepository;
import jp.kobe_u.cs27.zoomMei.domain.service.FriendService;
import jp.kobe_u.cs27.zoomMei.domain.service.MeetingService;
import jp.kobe_u.cs27.zoomMei.form.ConductForm;
import jp.kobe_u.cs27.zoomMei.form.MeetingForm;
import jp.kobe_u.cs27.zoomMei.form.MeetingRestForm;
import lombok.RequiredArgsConstructor;

// mtgのget（member-entityとzoom-entityを組み合わせて、ひとつのデータの行を作る）
// とpost（member-entityとzoom-entityを受け取って、一つのデータの行をでmtgのデータに付け加える）
// 、友達関係のget（フォローしている人のニックネームを出す）


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api")
public class ZoomMeiRestController {
    private final FriendService friendService;
    private final MeetingService meetingService;
    private final UserRepository userRepository;

    public static <K, V> HashMap<K, List<V>> add(HashMap<K, List<V>> map, K key, V value) {
        List<V> list = map.get(key);
        if (Objects.isNull(list)) {
            list = new ArrayList<>();
        }
        list.add(value);
        map.put(key, list);
        return map;
    }


    @GetMapping("/test")
    public String s(){
        return "Hello World";
    }

    /*会議相手候補者（フォローしている人）を提示する*/
    @GetMapping("/friend")
    public Response<FriendList> searchFriend(
        @RequestParam("uid")
        @Pattern(regexp = "[0-9a-zA-Z_\\-]+") String uid){
            return ResponseCreater.succeed(friendService.query(uid));
    }

    /*自分が予定したmtgを表示する*/
    @GetMapping("/meeting")
    public Response<ZoomList> searchMeeting(
        @RequestParam("uid")
        @Pattern(regexp = "[0-9a-zA-Z_\\-]+") String uid){
            return ResponseCreater.succeed(meetingService.put(uid));
    }

    /*主催するmtgを追加する*/
    //      入力例：{
    // "uid":"okamoto",
    // "pid":["hiro","aaa","bbb"],
    // "starttime":"2023-01-03 23:00"
    //   }

    @PostMapping("/meeting/input")
    public MeetingForm inputMeeting(@RequestBody @Validated MeetingRestForm form){
        LocalDateTime localDateTime = LocalDateTime.parse(form.getStarttime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        final String[] pidnicknames=form.getPidnicknames();
        HashMap<LocalDateTime,List<String>> map=new HashMap<>();
        for(String pidnickname:pidnicknames){
            map=add(map,localDateTime,userRepository.findByNickname(pidnickname).getUid());
        }
        MeetingForm newform=new MeetingForm(form.getUid(),map.get(localDateTime),localDateTime);
        meetingService.record(newform);
        meetingService.pidrecord(newform);
        return newform;
    }

    @PostMapping("/meeting/conduct")
    public ConductForm concentMeeting(@RequestBody @Validated ConductForm form){
        meetingService.answer(form);
        return form;
    }


    }




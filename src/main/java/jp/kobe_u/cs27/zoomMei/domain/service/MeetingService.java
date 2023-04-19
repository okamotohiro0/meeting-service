package jp.kobe_u.cs27.zoomMei.domain.service;

import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import jp.kobe_u.cs27.zoomMei.configuration.exception.UserValidationException;
import jp.kobe_u.cs27.zoomMei.domain.dto.FollowerWithNickname;
import jp.kobe_u.cs27.zoomMei.domain.dto.FriendList;
import jp.kobe_u.cs27.zoomMei.domain.dto.FriendWithNickname;
import jp.kobe_u.cs27.zoomMei.domain.dto.HostWithNickname;
import jp.kobe_u.cs27.zoomMei.domain.dto.PidWithNickname;
import jp.kobe_u.cs27.zoomMei.domain.dto.ZoomList;
import jp.kobe_u.cs27.zoomMei.domain.entity.Friend;
import jp.kobe_u.cs27.zoomMei.domain.entity.Member;
import jp.kobe_u.cs27.zoomMei.domain.entity.Zoom;
import jp.kobe_u.cs27.zoomMei.domain.repository.FriendRepository;
import jp.kobe_u.cs27.zoomMei.domain.repository.MemberRepository;
import jp.kobe_u.cs27.zoomMei.domain.repository.UserRepository;
import jp.kobe_u.cs27.zoomMei.domain.repository.ZoomRepository;
import jp.kobe_u.cs27.zoomMei.form.ConductForm;
import jp.kobe_u.cs27.zoomMei.form.MeetingForm;
import lombok.RequiredArgsConstructor;
import static jp.kobe_u.cs27.zoomMei.configuration.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final UserRepository userrepo;    
    private final FriendRepository friendrepo; 
    private final ZoomRepository zoomrepo;
    private final MemberRepository memberrepo;
    private final UserService uservice;
    private final FriendService friendService;

    public static <K, V> HashMap<K, List<V>> add(HashMap<K, List<V>> map, K key, V value) {
      List<V> list = map.get(key);
      if (Objects.isNull(list)) {
          list = new ArrayList<>();
      }
      list.add(value);
      map.put(key, list);
      return map;
  }

    public FriendList query(String uid){
    
    
        //フレンドのリストから自分がuidのリストを拾ってくる。
        List<Friend> friends=(List<Friend>) friendrepo.findByUid(uid);
        
        //フレンドのリストから自分がuidのリストを拾ってくる。
        List<Friend> followers=(List<Friend>) friendrepo.findByFid(uid);
    
        //フレンドのリストとフォローする人のニックネームも足したリスト
        List<FriendWithNickname> friendname=new ArrayList<FriendWithNickname>();
    
        //フレンドのリストとフォロワーのニックネームも足したリスト
        List<FollowerWithNickname> followername=new ArrayList<FollowerWithNickname>();

        String relation;
        
        for(Friend friend:friends){
          String fNickname=uservice.getUser(friend.getFid()).getNickname();
          if(friend.getRelationship()==1){
            relation="家族";
          }else if(friend.getRelationship()==2){
            relation="友達";
          }else{
            relation="介護者";
          }
          friendname.add(new FriendWithNickname(friend.getListid(),friend.getFid(),fNickname,relation,friend.getUid()));
        }
    
        for(Friend follower:followers){
          String followerNickname=uservice.getUser(follower.getUid()).getNickname();
          followername.add(new FollowerWithNickname(follower.getListid(),follower.getFid(),followerNickname,follower.getRelationship(),follower.getUid()));
        }
    
        // System.out.println(friendname);
    
        return new FriendList(
          friendname,
          followername
        );
      }



      public Zoom record(
        MeetingForm form) {
    
      // ユーザIDを変数に格納する
      final String uid = form.getUid();
      final LocalDateTime starttime=form.getStarttime();
      final List<String> pid = form.getPid();
      final double random= Math.random();
      final String link="https://wsapp.cs.kobe-u.ac.jp/meetcs27/"+random+"?user=";
      LocalDateTime ldt=LocalDateTime.now();
    
      // ユーザが登録されていない場合エラーを返す
      if (!userrepo.existsById(uid)) {
        throw new UserValidationException(
            USER_DOES_NOT_EXIST,
            "record the friend",
            String.format(
                "user %s does not exist",
                uid));
      }
      //会議登録する人がフォローしている人の中からか確認
      for(String id:pid){
        if(!friendService.existsFollow(uid,id)){
          throw new UserValidationException(
            USER_DOES_NOT_EXIST,
            "record the friend",
            String.format(
                "user %s does not exist",
                uid));
        }
      }
      if(zoomrepo.existsByHostidAndStarttime(uid,starttime)){
        throw new UserValidationException(
          USER_DOES_NOT_EXIST,
          "record the friend",
          String.format(
              "user %s does not exist",
              uid));
      }

      //会議の時間が過去の場合
      if(ldt.isAfter(starttime)){
        throw new UserValidationException(
          USER_DOES_NOT_EXIST,
          "record the friend",
          String.format(
              "user %s does not exist",
              uid));
      }

      


      // 体調の記録をDBに保存し、保存した記録を戻り値として返す
      return zoomrepo.save(new Zoom(
        null,
        uid,
        form.getStarttime(),
        link
          ));


  
    }

    public void pidrecord(
      MeetingForm form
    ){
      final String uid=form.getUid();
      
      final LocalDateTime starttime=form.getStarttime();

      Zoom zoom=zoomrepo.findByHostidAndStarttime(uid,starttime);


      Long zoomid=zoom.getZoomid();

      final List<String> pid=form.getPid();

       for(int i=0;i<pid.size();i++){
         memberrepo.save(new Member(
          null,
          pid.get(i),
          zoomid,
          0
         ) );
      }
     }
      
    

     public ZoomList put(String uid){
      // final String uid=form.getUid();
      LocalDateTime ldt=LocalDateTime.now();

      //自分が主催者のzoom-entityを拾ってくる。
      List<Zoom> zoomlists =(List<Zoom>) zoomrepo.findByHostid(uid);

      //自分がpidのmember-entityを拾ってくる。
      List<Member> memberlists=(List<Member>) memberrepo.findByPid(uid);


      //自分が主催者のリストをまず上から並べる
      List<HostWithNickname> hostname=new ArrayList<HostWithNickname>();

      //自分がpidのリストを次に並べる
      List<PidWithNickname> pidname=new ArrayList<PidWithNickname>();

      //自分が招待したmemberのリスト
      HashMap<Long,List<String>> map=new HashMap<>();

      //自分が招待されたmemberのリスト
      // List<String> pid=new ArrayList<String>();
      HashMap<Long,List<String>> pidmap=new HashMap<>();

      String stating;

      //自分が主催したmtgリストをまとめる
      for(Zoom zoomlist:zoomlists){
        if(ldt.isBefore(zoomlist.getStarttime())){
        String host=uservice.getUser(zoomlist.getHostid()).getNickname();
        List<Member> members=(List<Member>)memberrepo.findByZoomid(zoomlist.getZoomid());
        HashMap<String,Integer> states=new HashMap<String,Integer>();
        HashMap<String,String> statas=new HashMap<String,String>(); 
        for(Member member:members){
          //  membernames.add(uservice.getUser(member.getPid()).getNickname()); 
          map=add(map,zoomlist.getZoomid(),uservice.getUser(member.getPid()).getNickname());
          states.put(uservice.getUser(member.getPid()).getNickname(), member.getState());
          if(member.getState()==0){
            stating="未定";
          }else if(member.getState()==1){
            stating="承認";
          }else{
            stating="拒否";
          }
          statas.put(uservice.getUser(member.getPid()).getNickname(), stating);
        }

        
        hostname.add(new HostWithNickname(zoomlist.getZoomid(),zoomlist.getHostid(),
        host,zoomlist.getStarttime(),zoomlist.getLink()+uid,map.get(zoomlist.getZoomid()),statas));
      }}

      // LocalDateTime hosttime=zoomlist.getStarttime();
      // if(ldt.isBefore(hosttime)){
      //       zoomrepo.deleteByStarttime(hosttime);      
      // }
 

      //自分が招待されたmtgのリストをまとめる

      for(Member memberlist:memberlists){
         Long zoomid=memberlist.getZoomid();
         Zoom zoom=zoomrepo.findByZoomid(zoomid);
         String invitename=uservice.getUser(zoom.getHostid()).getNickname();
         LocalDateTime starttime=zoom.getStarttime();
         String link=zoom.getLink();
         List<Member> teams=(List<Member>)memberrepo.findByZoomid(memberlist.getZoomid());
         for(Member team:teams){
          pidmap=add(map,memberlist.getZoomid(),uservice.getUser(team.getPid()).getNickname());
         }
         if(ldt.isBefore(starttime)){
         pidname.add(new PidWithNickname(memberlist.getPid(), memberlist.getGroupid(),memberlist.getZoomid(), zoom.getHostid(),invitename, starttime, link+uid, pidmap.get(memberlist.getZoomid())));
            }    
          }



      return new ZoomList(
        hostname,
        pidname
      );
     }

     public void answer(ConductForm form){
      Member member=memberrepo.findByPidAndZoomid(form.getPid(), form.getZoomid());
      member.setState(form.getState());
      memberrepo.save(member);

     }





}

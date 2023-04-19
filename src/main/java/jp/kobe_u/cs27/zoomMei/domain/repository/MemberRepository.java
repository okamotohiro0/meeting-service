package jp.kobe_u.cs27.zoomMei.domain.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.kobe_u.cs27.zoomMei.domain.entity.Member;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long>{
    List<Member> findByZoomid(Long zoomid);

    List<Member> findByPid(String pid);

    Member findByPidAndZoomid(String pid,Long zoomid);
}

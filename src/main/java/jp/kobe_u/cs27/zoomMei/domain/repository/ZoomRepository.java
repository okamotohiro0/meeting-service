package jp.kobe_u.cs27.zoomMei.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.kobe_u.cs27.zoomMei.domain.entity.Zoom;

/**
 * zoomのリポジトリ
 */
@Repository
public interface ZoomRepository extends CrudRepository<Zoom,Long> {
    Zoom findByHostidAndStarttime(String hostid,LocalDateTime starttime);

    Zoom findByZoomid(Long zoomid);

    Zoom deleteByStarttime(LocalDateTime starttime);
 
    boolean existsByHostidAndStarttime(String hostid,LocalDateTime starttime);   

    List<Zoom> findByHostid(String hostid);
}

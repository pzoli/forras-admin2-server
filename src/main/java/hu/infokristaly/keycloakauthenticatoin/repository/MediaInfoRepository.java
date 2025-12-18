package hu.infokristaly.keycloakauthenticatoin.repository;

import hu.infokristaly.keycloakauthenticatoin.entity.MediaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaInfoRepository extends JpaRepository<MediaInfo, Long> {
    public Optional<MediaInfo> findByFileName(String fileName);
    public Optional<List<MediaInfo>> findBySystemUserSub(String systemUserSub);
}

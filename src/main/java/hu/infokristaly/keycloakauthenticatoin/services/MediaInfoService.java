package hu.infokristaly.keycloakauthenticatoin.services;

import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import hu.infokristaly.keycloakauthenticatoin.entity.MediaInfo;
import hu.infokristaly.keycloakauthenticatoin.repository.DoctorRepository;
import hu.infokristaly.keycloakauthenticatoin.repository.MediaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaInfoService {
    @Autowired
    MediaInfoRepository mediaInfoRepository;

    public MediaInfoService() {
        super();
    }

    public List<MediaInfo> getAllMediaInfo() {
        return mediaInfoRepository.findAll();
    }

    public List<MediaInfo> getAllMediaInfoBySystemUserSub(String systemUserSub) {
        return mediaInfoRepository.findBySystemUserSubOrderByFileNameAsc(systemUserSub).orElse(null);
    }

    public MediaInfo getMediaInfo(Long mediaInfoId) {
        return mediaInfoRepository.findById(mediaInfoId).orElse(null);
    }

    public MediaInfo createMediaInfo(MediaInfo mediaInfo) {
        return mediaInfoRepository.save(mediaInfo);
    }

    public MediaInfo updateMediaInfo(MediaInfo mediaInfo) {
        return mediaInfoRepository.save(mediaInfo);
    }

    public void deleteById(Long id) {
        mediaInfoRepository.deleteById(id);
    }

    public  MediaInfo findByFileName(String name) {
        return mediaInfoRepository.findByFileName(name).orElse(null);
    }
}

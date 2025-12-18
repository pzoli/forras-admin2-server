package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.MediaInfo;
import hu.infokristaly.keycloakauthenticatoin.services.MediaInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/video")
@SecurityRequirement(name = "Keycloak")
public class VideoController {

    private final Path videoPath = Paths.get("src/main/resources/videos");

    @Autowired
    MediaInfoService mediaInfoService;

    @PutMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public void updateMediaFile(@RequestParam("file") MultipartFile file, @RequestParam("origin") String origin) throws IOException {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!origin.startsWith(user.getSubject())) {
            throw new IOException("Yuo are not permitted to upload this video!");
        }
        Path path = Paths.get(videoPath.toString(), origin + ".webm");
        if (!path.toFile().exists()) {
            MediaInfo foundMediaInfo = mediaInfoService.findByFileName(origin);
            if (foundMediaInfo != null && foundMediaInfo.getCloseDate() != null) {
                throw new IOException("Closed MediaInfo found in database!");
            }
            try {
                Files.copy(file.getInputStream(), path);
                mediaInfoService.createMediaInfo(new MediaInfo(origin, user.getSubject()));
            }  catch (Exception ex) {
                throw ex;
            }
        } else {
            MediaInfo foundMediaInfo = mediaInfoService.findByFileName(origin);
            if (foundMediaInfo == null) {
                throw new IOException("MediaInfo not found in database!");
            }
            Files.write(path, file.getBytes(), StandardOpenOption.APPEND);
        }
    }

    @GetMapping(path = "/filename")
    public String getUniqueFileName() {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getSubject() + "_" +new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
    }

    @GetMapping(path = "/mediainfos/{sub}")
    public List<MediaInfo> getMediaInfos(@PathParam("sub") String sub) {
        return mediaInfoService.getAllMediaInfoBySystemUserSub(sub);
    }

    @PutMapping(path = "/closemedia")
    public void setCloseDateOfMediaInfo(@QueryParam("origin") String origin) throws IOException {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!origin.startsWith(user.getSubject())) {
            throw new IOException("Yuo are not permitted to upload this video!");
        }
        MediaInfo foundMediaInfo = mediaInfoService.findByFileName(origin);
        foundMediaInfo.setCloseDate(new Date());
        mediaInfoService.updateMediaInfo(foundMediaInfo);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteMediaInfos(@PathParam("id") String id) throws IOException {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MediaInfo foundMediaInfo = mediaInfoService.findByFileName(id);
        if (foundMediaInfo != null  && !foundMediaInfo.getSystemUserSub().equals(user.getSubject())) {
            throw new IOException("Yuo are not permitted to delete this video!");
        }
        mediaInfoService.deleteById(Long.parseLong(id));
    }

    @GetMapping(path = "/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadMediaFile(@QueryParam("origin") String origin) throws IOException {
        Path filePath = Paths.get(videoPath.toString(), origin);
        if (filePath.toFile().exists()) {
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+origin + ".webm")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.MediaInfo;
import hu.infokristaly.keycloakauthenticatoin.services.MediaInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
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
    public void updateMediaFile(@RequestParam("file") MultipartFile file, @RequestParam("origin") String origin, @RequestParam("islast") boolean isLastChunk ) throws IOException {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!origin.startsWith(user.getSubject())) {
            throw new IOException("Yuo are not permitted to upload this video!");
        }
        Path path = Paths.get(videoPath.toString(), origin + ".webm");
        MediaInfo foundMediaInfo = mediaInfoService.findByFileName(origin);
        if (!path.toFile().exists()) {
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
            if (foundMediaInfo == null) {
                throw new IOException("MediaInfo not found in database!");
            }
            Files.write(path, file.getBytes(), StandardOpenOption.APPEND);
            if (isLastChunk) {
                foundMediaInfo.setCloseDate(new Date());
                mediaInfoService.updateMediaInfo(foundMediaInfo);
                fixVideoDuration(origin+".webm");
            }
        }
    }

    @GetMapping(path = "/filename")
    public String getUniqueFileName() {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getSubject() + "_" +new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
    }


    @GetMapping(path = "/mediainfos")
    public List<MediaInfo> getMediaInfos(@QueryParam("sub") String sub) {
        List<MediaInfo> result = mediaInfoService.getAllMediaInfoBySystemUserSub(sub);
        return result;
    }

    private void fixVideoDuration(String inputPath) {
        try {
            String outputPath = inputPath.replace(".webm", "_fixed.webm");
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y", "-i", inputPath, "-c", "copy", outputPath
            );
            pb.directory(videoPath.toFile());
            pb.inheritIO().start().waitFor();
            Files.move(Paths.get(videoPath.toString(),outputPath), Paths.get(videoPath.toString(),inputPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping
    public void deleteMediaInfos(@QueryParam("id") Long id) throws IOException {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MediaInfo foundMediaInfo = mediaInfoService.getMediaInfo(id);
        if (foundMediaInfo != null  && !foundMediaInfo.getSystemUserSub().equals(user.getSubject())) {
            throw new IOException("Yuo are not permitted to delete this video!");
        }
        mediaInfoService.deleteById(id);
        Path filePath = Paths.get(videoPath.toString(), foundMediaInfo.getFileName()+".webm");
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
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

package hu.infokristaly.keycloakauthenticatoin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/video")
@SecurityRequirement(name = "Keycloak")
public class VideoController {

    private final Path videoPath = Paths.get("src/main/resources/videos");

    @PutMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateLanguage(@RequestParam("file") MultipartFile file, @RequestParam("origin") String origin) throws IOException {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!origin.startsWith(user.getSubject())) {
            throw new IOException("Yuo are not permitted to upload this video!");
        }
        Path path = Paths.get(videoPath.toString(), origin + ".webm");
        if (!path.toFile().exists()) {
            Files.copy(file.getInputStream(), path);
        } else {
            Files.write(path, file.getBytes(), StandardOpenOption.APPEND);
        }
    }

    @GetMapping(path = "/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@QueryParam("origin") String origin) throws IOException {
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

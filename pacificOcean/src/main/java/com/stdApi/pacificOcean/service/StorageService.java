package com.stdApi.pacificOcean.service;


import com.stdApi.pacificOcean.config.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class StorageService {
    private final Path uploadDir;

    public StorageService(StorageProperties storageProperties) throws IOException {
        this.uploadDir = Paths.get(storageProperties.getUploadDir());
        Files.createDirectories(this.uploadDir);
    }

    public String storeFile(MultipartFile file) throws IOException {
        try {
            log.info("경로 {}", uploadDir);
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            log.info("파일 이름: {}", filename);

            Path targetLocation = uploadDir.resolve(filename);
            log.info("타겟 위치: {}", targetLocation);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("파일 저장 성공");

            String fileUrl = "/img/" + filename;
            log.info("저장된 파일 URL: {}", fileUrl);

            return fileUrl;
        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            throw e;
        }
    }
}

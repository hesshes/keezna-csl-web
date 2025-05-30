package com.keezna.webfolio.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomFileUtil {

	@Value("${keezna.upload.path}")
	private String uploadPath;

	@PostConstruct
	public void init() {
		File tempFolder = new File(uploadPath);

		if (tempFolder.exists() == false)
			tempFolder.mkdir();

		uploadPath = tempFolder.getAbsolutePath();

		log.info("uploadPath : {}", uploadPath);
	}

	public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

		if (files == null || files.size() == 0) {
			return List.of();
		}

		List<String> uploadNames = new ArrayList<>();

		for (MultipartFile mf : files) {
			String savedName = UUID.randomUUID().toString() + "_" + mf.getOriginalFilename();

			Path savePath = Paths.get(uploadPath, savedName);

			try {
				Files.copy(mf.getInputStream(), savePath);
				String contentType = mf.getContentType();
				if (contentType != null && contentType.startsWith("image")) {
					Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);

					Thumbnails.of(savePath.toFile()).size(200, 200).toFile(thumbnailPath.toFile());
				}

				uploadNames.add(savedName);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return uploadNames;
	}

	public ResponseEntity<Resource> getFile(String fileName) {
		Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
		if (!resource.isReadable()) {
			resource = new FileSystemResource(uploadPath + File.separator + "aaa.png");
		}

		HttpHeaders headers = new HttpHeaders();

		try {
			headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok().headers(headers).body(resource);
	}

	public void deleteFiles(List<String> fileNames) {
		if(fileNames == null || fileNames.size() == 0){
			return;
		}
		fileNames.forEach(fileName -> {
			String thumbnailFileName = "s_" + fileName;
			Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
			Path filePath = Paths.get(uploadPath, fileName);
			try {
				Files.deleteIfExists(filePath);
				Files.deleteIfExists(thumbnailPath);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
			
		});
	}
}

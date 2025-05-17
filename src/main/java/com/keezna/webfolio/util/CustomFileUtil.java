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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	public List<String> saveFiles(List<MultipartFile> files)
			throws RuntimeException {

		if (files == null || files.size() == 0) {
			return List.of();
		}

		List<String> uploadNames = new ArrayList<>();

		for (MultipartFile mf : files) {
			String savedName = UUID.randomUUID().toString() + "_"
					+ mf.getOriginalFilename();

			Path savePath = Paths.get(uploadPath, savedName);

			try {
				Files.copy(mf.getInputStream(), savePath);
				uploadNames.add(savedName);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return uploadNames;
	}

}

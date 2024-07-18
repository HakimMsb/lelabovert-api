package com.hakmesb.lelabovert.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	@Value("${lelabovert.images}")
	private String imagesDirectory;

	public String uploadImage(MultipartFile file) throws IOException {

		String originalFileName = file.getOriginalFilename();
		String randomId = UUID.randomUUID().toString();
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		String fileName = randomId + fileExtension;
		
		Path directoryPath = Paths.get(imagesDirectory);
		Files.createDirectories(directoryPath);
		
		Path filePath = directoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

		return fileName;
	}
	
	public String deleteImage(String fileName) throws IOException {
		Path filePath = Paths.get(imagesDirectory).resolve(fileName);
        Files.deleteIfExists(filePath);

        return "Image with path: " + filePath.toString() + " deleted.";
	}
	
	public String getImagesDirectory() {
		return imagesDirectory;
	}

}

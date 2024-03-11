package com.gamelist.main.cloudinary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gamelist.main.helpers.SecretsStore;



@Service
public class CloudService {

	Cloudinary cloudinary;


	private String cloudName;
	private String apiKey;
	private String apiSecret;

    public CloudService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", System.getenv("cloudName"));
        valuesMap.put("api_key", System.getenv("apiKey"));
        valuesMap.put("api_secret", System.getenv("apiSecret"));
        cloudinary = new Cloudinary(valuesMap);
    }


    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }
        return result;
    }

    public Map delete(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

}

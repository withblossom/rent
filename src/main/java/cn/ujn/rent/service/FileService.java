package cn.ujn.rent.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {
    String postImageFile(MultipartFile image);

    ResponseEntity<byte[]> download(String path, HttpServletRequest request, HttpServletResponse response);
}

package cn.ujn.rent.controller;

import cn.ujn.rent.service.FileService;
import cn.ujn.rent.utils.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    FileService fileService;

    @PostMapping("/image")
    public Result postImageFile(@RequestParam("file") MultipartFile image) {
        String path = fileService.postImageFile(image);
        return Result.ok(path);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename,
                                           HttpServletRequest request, HttpServletResponse response) {
        return fileService.download(filename, request, response);
    }
    @GetMapping("/download/{dir1}/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String dir1,
                                           @PathVariable String filename,
                                           HttpServletRequest request, HttpServletResponse response) {
        return fileService.download(dir1 + "/"+filename, request, response);
    }

    @GetMapping("/download/{dir1}/{dir2}/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String dir1,
                                           @PathVariable String dir2,
                                           @PathVariable String filename,
                                           HttpServletRequest request, HttpServletResponse response) {
        return fileService.download(dir1 + "/" + dir2 + "/" + filename, request, response);
    }
}

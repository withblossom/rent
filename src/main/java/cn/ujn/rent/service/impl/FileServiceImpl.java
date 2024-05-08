package cn.ujn.rent.service.impl;

import cn.ujn.rent.error.RentException;
import cn.ujn.rent.service.FileService;
import cn.ujn.rent.utils.SystemConstants;
import com.j256.simplemagic.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String postImageFile(MultipartFile image) {
        try (InputStream is = image.getInputStream()){
            String mimeType = image.getContentType();
            ContentType contentType = ContentType.fromMimeType(mimeType);
            String[] fileExtensions = contentType.getFileExtensions();
            if (fileExtensions == null || fileExtensions.length == 0) {
                RentException.cast("未知文件类型！！！");
            }
            String fileMd5 = null;
            try {
                fileMd5 = DigestUtils.md5DigestAsHex(is);
            }catch (IOException e){
                RentException.cast("文件损坏！！！");
            }

            String dir = getFileFolderPath(fileMd5);
            File dirFile = new File(SystemConstants.IMAGE_PATH, dir);
            String newFilename = fileMd5 + "." + fileExtensions[0];
            File imageFile = new File(dirFile,newFilename);
            if (imageFile.exists()){
                System.out.println("文件："+newFilename+" 已存在");
                return dir + newFilename;
            }

            if (!dirFile.exists()) {
                boolean b = dirFile.mkdirs();
                if (!b) {
                    RentException.cast("存储目录创建失败！！！");
                }
            }
            image.transferTo(imageFile);
            return dir + newFilename;
        } catch (IOException e) {
            RentException.cast(e.getMessage());
        }
        return "";
    }

    @Override
    public ResponseEntity<byte[]> download(String path, HttpServletRequest request, HttpServletResponse response) {
        byte[] bytes = null;
        try (FileInputStream fi = new FileInputStream(SystemConstants.IMAGE_PATH + path)) {
            bytes = new byte[fi.available()];
            fi.read(bytes);
        } catch (Exception e) {
            RentException.cast("文件下载出错！！！");
        }

        int index = path.lastIndexOf('/');
        String filename = path.substring(index + 1);
        String[] strings = filename.split("\\.");

        ContentType contentType = ContentType.fromFileExtension(strings[1]);
        String mimeType = contentType.getMimeType();
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(strings[0], "UTF-8"))
                    .header(HttpHeaders.CACHE_CONTROL, "public,max-age=1800")
                    .contentType(MediaType.asMediaType(MimeType.valueOf(mimeType)))
                    .contentLength(bytes.length)
                    .body(bytes);
        } catch (UnsupportedEncodingException e) {
            RentException.cast("文件名解析出错！！！");
        }
        return null;
    }


    private String getFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/";
    }

}

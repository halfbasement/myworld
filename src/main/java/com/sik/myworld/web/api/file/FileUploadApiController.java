package com.sik.myworld.web.api.file;

import com.sik.myworld.domain.post.Post;
import com.sik.myworld.domain.post.PostRepository;
import com.sik.myworld.domain.post.PostService;
import com.sik.myworld.web.post.dto.UploadFileDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FileUploadApiController {

    private final PostService postService;

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/uploadFile")
    public ResponseEntity<List<UploadFileDto>> upload(MultipartFile[] uploadFiles){


        List<UploadFileDto> resultDtoList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            if(uploadFile.getContentType().startsWith("image")==false){ //이미지파일 체크


                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            //브라우저 호환
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.info("filename ={}",fileName);

            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try{
                uploadFile.transferTo(savePath); //이미지 저장
                resultDtoList.add(new UploadFileDto(fileName,uuid,folderPath));
            }catch (IOException e){
                e.printStackTrace();
            }

        }//for


        return new ResponseEntity<>(resultDtoList,HttpStatus.OK);
    }

    private String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath,folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }


    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName){

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName,"UTF-8");

            File file = new File(uploadPath+File.separator+srcFileName);

            HttpHeaders header = new HttpHeaders();

            header.add("Content-TYPE", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;

    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){

        String srcFileName = null;

        try{
            srcFileName = URLDecoder.decode(fileName,"UTF-8");
            File file = new File(uploadPath+File.separator+srcFileName);
            boolean result = file.delete();

            return new  ResponseEntity<>(result,HttpStatus.OK);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file/{pid}")
    public ResponseEntity postFile(@PathVariable Long pid){

        Post findByPost = postService.findById(pid);

        List<UploadFileDto> result = findByPost.getFiles().stream().map(file -> new UploadFileDto(file.getFileName(),file.getUuid(), file.getPath())).collect(Collectors.toList());


        return new ResponseEntity(result,HttpStatus.OK);
    }
}

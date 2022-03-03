package com.sik.myworld.web.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDto {

    private String fileName;
    private String uuid;
    private String path;

    public String getImageURL(){
        try{
            return URLEncoder.encode(path+"/"+uuid+"_"+ fileName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

}

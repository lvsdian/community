package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.dto.FileDTO;
import cn.andios.majiangcommunity.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:文件上传
 * @author:LSD
 * @when:2019/7/31/16:07
 */
@Controller
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){

        //强转request
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        //得到文件
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("editormd-image-file");
        //调用UCloudProvider上传文件
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(),file.getContentType(),file.getOriginalFilename());

            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/andios-blue.jpg");

        return fileDTO;
    }
}

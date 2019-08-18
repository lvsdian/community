package cn.andios.majiangcommunity.provider;

import cn.andios.majiangcommunity.exception.CustomizeErrorCode;
import cn.andios.majiangcommunity.exception.CustomizeException;
import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/31/21:07
 */
@Service
public class UCloudProvider {

    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    @Value("${ucloud.ufile.bucketName}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.proxySuffix}")
    private String proxySuffix;

    @Value("${ucloud.ufile.expiresDuration}")
    private int expiresDuration;

    public String upload(InputStream fileStream,String mimeType,String fileName){
        //避免传入的文件重名
        String generateFileName = "";
        //这里的.需要转义
        String[] filePath = fileName.split("\\.");
        if(filePath.length > 1){
            generateFileName = UUID.randomUUID().toString() + "." + filePath[filePath.length - 1];
        }else{
            throw new CustomizeException(CustomizeErrorCode.PIC_UPLOAD_FAIL);
        }
        try {
            // 对象相关API的授权器
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);

            // 对象操作需要ObjectConfig来配置您的地区和域名后缀
            ObjectConfig config = new ObjectConfig(region, proxySuffix);

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generateFileName)
                    .toBucket(bucketName)

                    //是否上传校验MD5, Default = true
                    //  .withVerifyMd5(false)
                    //指定progress callback的间隔, Default = 每秒回调
                     //.withProgressConfig(ProgressConfig.callbackWithPercent(10))

                    //配置进度监听
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();
            //上传成功
            if(response != null && response.getRetCode() == 0){
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(generateFileName, bucketName, expiresDuration)
                        .createUrl();
                return url;
            }else{
                throw new CustomizeException(CustomizeErrorCode.PIC_UPLOAD_FAIL);
            }
        } catch (UfileClientException | UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.PIC_UPLOAD_FAIL);
        }
    }
}

package com.lxkj.gmall.gmall_manage_web.util;

import com.lxkj.gmall.gmall_manage_web.GmallManageWebApplication;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile) throws IOException, MyException {
        String imgUrl = "http://192.168.79.129";
        //上传图片到服务器

        //获取配置文件的路径
        String tracker = GmallManageWebApplication.class.getResource("/tracker.conf").getPath();

        ClientGlobal.init(tracker);

        TrackerClient trackerClient = new TrackerClient();

        TrackerServer trackerServer = trackerClient.getTrackerServer();
        //通过tracker获得一个Storage连接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);

        byte[] bytes = multipartFile.getBytes(); //获得上传对象的二进制对象

        //获得文件后缀名
        String originalFilename = multipartFile.getOriginalFilename(); //a.jpg

        int i =originalFilename.lastIndexOf(".");
        String extName = originalFilename.substring(i+1);

        String[] uploadInfos = storageClient.upload_file(bytes,"jpg",null);

        for (String uploadInfo : uploadInfos) {
            imgUrl+="/"+uploadInfo;
        }
        return imgUrl;
    }
}

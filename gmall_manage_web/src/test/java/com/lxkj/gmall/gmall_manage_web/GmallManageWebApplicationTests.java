package com.lxkj.gmall.gmall_manage_web;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
public class GmallManageWebApplicationTests {

    @Test
   public void contextLoads() throws IOException, MyException {
        //获取配置文件的路径
       String tracker = GmallManageWebApplication.class.getResource("/tracker.conf").getPath();

        ClientGlobal.init(tracker);

        TrackerClient trackerClient = new TrackerClient();

        TrackerServer trackerServer = trackerClient.getTrackerServer();

        StorageClient storageClient = new StorageClient(trackerServer,null);

        String[] uploadInfos = storageClient.upload_file("e:/a.jpg","jpg",null);

        String url = "http://192.168.79.129/";
        for (String uploadInfo : uploadInfos) {
           url+=uploadInfo;
            System.out.println(url);
        }
    }

}

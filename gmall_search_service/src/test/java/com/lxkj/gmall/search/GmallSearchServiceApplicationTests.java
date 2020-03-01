package com.lxkj.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsSearchSkuInfo;
import com.lxkj.gmall.bean.PmsSkuInfo;
import com.lxkj.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GmallSearchServiceApplicationTests {




    @Test
   public void contextLoads() throws IOException {


    }

}

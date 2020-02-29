package com.lxkj.gmall.gmall_manage_service.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lxkj.gmall.bean.*;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsSkuAttrValueMapper;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsSkuImageMapper;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsSkuInfoMapper;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsSkuSaleAttrValueMapper;
import com.lxkj.gmall.service.SkuService;
import com.lxkj.gmall.util.RedisUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JestClient jestClient;


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();
        //插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        //插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
        //插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        try {
            searChUtil();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuId) {
        //sku商品对象
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setSkuImageList(pmsSkuImages);
        return pmsSkuInfo1;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId,String ip){
        System.out.println("ip为"+ip+"的人"+Thread.currentThread().getName()+"进入的商品");
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        //连接缓存
        Jedis jedis = redisUtil.getJedis();
        //查询缓存
        String skuKey ="sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);
        if(StringUtils.isNoneBlank(skuJson)){
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        }else{
            //如果缓存中没有,查询mysql
            System.out.println("ip为"+ip+"的人"+Thread.currentThread().getName()+"没有缓存"+"sku:"+skuId+":lock");
            //pmsSkuInfo = getSkuByIdFromDb(skuId);

            //设置分布式锁
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10*1000);//拿到线程程的过期时间
            if(StringUtils.isNoneBlank(OK)&&OK.equals("OK")){
                //设置成功,有权在10秒的过期时间内访问数据库
                System.out.println("ip为"+ip+"的人"+Thread.currentThread().getName()+"有权在10秒过期访问数据库"+"sku:"+skuId+":lock");
                pmsSkuInfo = getSkuByIdFromDb(skuId);
                try {
                    Thread.sleep(1000*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(pmsSkuInfo!=null){
                    //mysql查询结果存入redis
                    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
                }else{
                    //数据库不存在该sku
                    //为了防止缓存穿透，null值设置给redis
                    jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
                }

                //在访问mysql后，将mysql的分布式锁释放
                System.out.println("ip为"+ip+"的人"+Thread.currentThread().getName()+"使用完毕，将锁归还"+"sku:"+skuId+":lock");
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if(StringUtils.isNoneBlank(lockToken)&&lockToken.equals(token)){
                    //jedis.eval("lua");可以用lua脚本，在查询到key的同时删除该key，防止高并发的意外发生
                    jedis.del("sku:"+skuId+":lock"); //用token确认删除的是自己的sku的锁
                }

            }else{
                //设置失败,自旋(该线程在睡眠几秒后，重新尝试访问本方法)
                System.out.println("ip为"+ip+"的人"+Thread.currentThread().getName()+"开始自旋");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               return getSkuById(skuId,ip);
            }
        }
        jedis.close();
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String id = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(id);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }

    public void searChUtil() throws IOException {
        //查询mysql
        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();

        pmsSkuInfoList = getAllSku();
        //转化为es的数据结构
        List<PmsSearchSkuInfo> searchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo searchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo,searchSkuInfo);

            searchSkuInfos.add(searchSkuInfo);
        }
        //导入es
        for (PmsSearchSkuInfo searchSkuInfo : searchSkuInfos) {
            Index put = new Index.Builder(searchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(searchSkuInfo.getId()).build();
            jestClient.execute(put);
        }
    }
}

package com.shiyitiancheng.test;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.IOException;

public class QiniuTest {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "-3j6ZpHdWHr8j6lQNB8fB9-dJqxm76VV2Hz0IYR0";
    String SECRET_KEY = "8Oe9fx_TYfSuCo_uV-MAxcr_4Olj3ZjpcbS3uLLF";

    //上传文件的路径
    String FilePath = "D:\\TEST_UPLOAD\\20180331192451_i8a2C.jpeg";
    //要上传的空间
    String bucketname = "shiyitiancheng-health";
    //上传到七牛后保存的文件名
    String key = "my-java.png";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
     Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
//    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);


    public static void main(String args[]) throws IOException {
        new UploadDemo().upload();
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        //要上传的空间
        String bucketname = "shiyitiancheng-health";
        //上传到七牛后保存的文件名
        String key = "my-java.png";
        return auth.uploadToken(bucketname);
    }

    @Test
    public void uploadTest() throws IOException {

        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }

    @Test
    public void deleteTest(){
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = "shiyitiancheng-health";
        String key = "my-java.png";
        try {

            //调用delete方法移动文件
            bucketManager.delete(bucket,key);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());

        }
    }

}

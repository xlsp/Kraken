package com.opensesame.api;

import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.bean.MarsTimer;
import com.mars.common.annotation.bean.MarsWrite;
import com.mars.common.base.InitBean;
import com.mars.server.server.request.HttpMarsResponse;
import com.opensesame.api.vo.LCRestVO;
import com.opensesame.core.dao.ExpDAO;
import com.opensesame.core.utils.HttpGetUtil;
import com.opensesame.core.utils.JobService;
import com.opensesame.core.utils.Result;
import com.opensesame.core.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * service层
 */
@MarsBean("restApiService")
public class LCRestApiService implements InitBean {

    private Logger logger = LoggerFactory.getLogger(LCRestApiService.class);

    public final static String FILE_UPLOAD_DIC = "/Volumes/Untitled/upload/";//上传文件的默认url前缀，根据部署设置自行修改

    /**
     * 为了让大家可以快速的跑起来，所以本示例没有连接数据库
     * 所以自然也就不会调用dao的方法了，这里注入进来dao，只是为了演示IOC的用法
     */
    @MarsWrite
    private ExpDAO expDAO;

    /**
     * 初始化bean示例
     */
    @Override
    public void init() {
        logger.info("执行了初始化bean, bean里面注入了DAO: " + expDAO);
    }

    public Result expGetRequest() {
        JobService.push(() -> {
            try {
                String results = "https://lab.magiconch.com/api/hosts/unicom-host";
                results = getExpResultData(results);
                System.out.println("results = " + results);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            return true;
        });
        Result resultSuccess = ResultGenerator.genSuccessResult("获取成功");
        return resultSuccess;
    }

    //    @Traction(level = TractionLevel.READ_COMMITTED)
    public Result expPostRequest(LCRestVO restVO) {
        String base64 = restVO.getBase64();
        String[] newStr = restVO.getBase64().split(";base64,");    // 分割成数组
        String str = "";
        for (int i = 0; i < newStr.length; i++) {
            if (i == 0) {
                str = newStr[i].replace("data:image/", "");
            } else {
                base64 = newStr[i];
            }
        }
        String fileName = "123." + str;
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        boolean okImage = GenerateImage(base64, this.FILE_UPLOAD_DIC + newFileName);
        if (okImage) {
            Result resultSuccess = ResultGenerator.genSuccessResult("文件上传成功,路径为：" + this.FILE_UPLOAD_DIC + newFileName);
            return resultSuccess;
        } else {
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String restUploadRequest(LCRestVO restVO) throws Exception {
        // 打印expDAO，如果不为null就说明已经注入了
        logger.info(String.valueOf(restVO == null));

        // 打印接收到的参数，看是否接收成功
//        logger.info(expVO.getName());
//        logger.info(JSON.toJSONString(expVO.getNames()));
//        logger.info(String.valueOf(expVO.getDate()));
//
//        // 打印接收到的文件，看是否接收成功
//        MarsFileUpLoad[] marsFileUpLoads = expVO.getMarsFileUpLoad();
//        if (marsFileUpLoads == null) {
//            return "上传失败";
//        }
//        for (MarsFileUpLoad marsFileUpLoad : marsFileUpLoads) {
//            System.out.println(marsFileUpLoad.getFileName());
//            System.out.println(marsFileUpLoad.getInputStream());
//
//            InputStream inputStream = marsFileUpLoad.getInputStream();
//            writeToLocal("/Users/luchao/Desktop/aaa/" + marsFileUpLoad.getFileName(), inputStream);
//        }
//        MarsFileUpLoad marsFileUpLoad = restVO.getFile();
//        writeToLocal("/Users/yuye/Downloads/aaa/aaas" + marsFileUpLoad.getFileName(), marsFileUpLoad.getInputStream());
        return "上传成功";
    }

    public void expDownLoadRequest(HttpMarsResponse response) throws Exception {
        InputStream inputStream = new FileInputStream(new File("/Users/yuye/Downloads/vvv2.zip"));
        response.downLoad("aaa2.zip", inputStream);
    }

    /**
     * 定时任务示例
     * 5秒执行一次
     */
    @MarsTimer(loop = 5000)
    public void timerExp() {
//        logger.info("执行了定时任务");
//        String results = "https://lab.magiconch.com/api/hosts/unicom-host";
//        results = getExpResultData(results);
    }

    /**
     * 这是把返回数据写死了，不然你们还得搭环境，连接数据库
     */
    private String getExpResultData(String results) {
        logger.info("hello world");
        logger.info("The world dies");
        results = HttpGetUtil.get("https://lab.magiconch.com/api/hosts/unicom-host", null);
        return results;
    }

    private static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }
}

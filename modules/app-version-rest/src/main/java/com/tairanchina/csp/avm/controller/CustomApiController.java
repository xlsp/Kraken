package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.utils.Result;
import com.tairanchina.csp.avm.utils.ResultGenerator;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.service.CustomApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * Created by hzlizx on 2018/6/14 0014
 */
@Api(value = "/c", description = "自定义接口相关")
@RestController
@RequestMapping("/c")
public class CustomApiController {

    private static final Logger logger = LoggerFactory.getLogger(CustomApiController.class);

    @Autowired
    private CustomApiService customApiService;

    public final static String FILE_UPLOAD_DIC = "/Volumes/Untitled/upload/";//上传文件的默认url前缀，根据部署设置自行修改

    @ApiOperation(
            value = "查询自定义接口",
            notes = "根据应用ID、接口KEY、版本、平台获取自定义接口信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantAppId", value = "应用appId", dataType = "string", defaultValue = "uc28ec7f8870a6e785", required = true),
            @ApiImplicitParam(name = "key", value = "自定义接口的key", required = true),
            @ApiImplicitParam(name = "platform", value = "平台，值应为 ios 或 android", required = true),
            @ApiImplicitParam(name = "version", value = "版本号", required = true),
    })
    @GetMapping("/{tenantAppId}/{key}/{version}/{platform}")
    public ServiceResult custom(@PathVariable String tenantAppId,
                                @PathVariable String key,
                                @PathVariable String platform,
                                @PathVariable String version) {
        logger.info("version: " + version);
        if (StringUtilsExt.hasBlank(tenantAppId, key, platform, version)) {
            return ServiceResultConstants.NEED_PARAMS;//缺少参数 请带上正确的参数进行请求，如有疑问请查看开发文档
        }
        if (!platform.equalsIgnoreCase("ios") && !platform.equalsIgnoreCase("android")) {
            return ServiceResultConstants.PLATFORM_ERROR;//请指定查询的版本iOS还是Andorid版本
        }

        return customApiService.getCustomContent(tenantAppId, key, platform, version);
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
    @RequestMapping({"/upload/file"})
    @ResponseBody
    @CrossOrigin
    public Result upload(HttpServletRequest httpServletRequest , String base64) throws URISyntaxException {
        //,@RequestPart("file")

        System.out.println(httpServletRequest.getParameterMap());
//        System.out.println(this.FILE_UPLOAD_DIC + "--------------" + base64);
        String[] newStr = base64.split(";base64,");	// 分割成数组
        String str = "";
        for (int i = 0; i < newStr.length; i++) {
            if ( i == 0) {
                str = newStr[i].replace("data:image/","");
            }else{
                base64 = newStr[i];
            }
        }
        if (httpServletRequest != null) {
            String fileName = "123."+str;
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //生成文件名称通用方法
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
            String newFileName = tempName.toString();
            GenerateImage(base64,this.FILE_UPLOAD_DIC + newFileName);
            Result resultSuccess1 = ResultGenerator.genSuccessResult();
            return resultSuccess1;
        }
        MultipartFile file = null;
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        File fileDirectory = new File(this.FILE_UPLOAD_DIC);
        //创建文件
        File destFile = new File(this.FILE_UPLOAD_DIC + newFileName);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            Result resultSuccess = ResultGenerator.genSuccessResult();
//            resultSuccess.setData(NewBeeMallUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    /**
     * 剪裁成正方形
     */
    public static BufferedImage getSque(BufferedImage bi) {
        int init_width = bi.getWidth();
        int init_height = bi.getHeight();
        if (init_width != init_height){
            int width_height = 0;
            int x = 0;
            int y = 0;
            if (init_width > init_height) {
                width_height = init_height;//原图是宽大于高的长方形
                x = (init_width-init_height)/2;
                y = 0;
            } else if (init_width < init_height) {
                width_height = init_width;//原图是高大于宽的长方形
                y = (init_height-init_width)/2;
                x = 0;
            }
            bi = bi.getSubimage(x, y, width_height, width_height);
        }
        return bi;
    }
}

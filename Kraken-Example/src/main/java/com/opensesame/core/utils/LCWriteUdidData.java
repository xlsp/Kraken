package com.opensesame.core.utils;


import cn.jiguang.common.utils.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LCWriteUdidData {

    public static void main(String[] args) {
/*
 SELECT '"',f.original,'"','=','"',f.en,'"',';' FROM `b2c_one`.`fanyi` f LIMIT 0,10000
  
 */
        List<String> stringList = new ArrayList<>();
        stringList.add("\"语言\" = \"language\";");
        writeDataHubData(stringList, null, "multiple-language-en-ios");
        System.out.println("语言");

    }

    public static void addFile(String udid) {
        List<String> stringList = new ArrayList<>();
        stringList.add(udid + "	iPhone6");
        writeDataHubData(stringList, null, "multiple-device-upload-ios");
        System.out.println(111);
    }

    public static boolean checkTempFile(String udid) throws InterruptedException {
        for (int i = 0; i <= 12; i++) {
            Thread.sleep(5000);
            if (deleteFile("/Volumes/Untitled/code/SVN/super_qian/" + udid + ".txt")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 写入txt文件
     *
     * @param result
     * @param fileName
     * @return
     */
    public static boolean writeDataHubData(List<String> result, String filePath, String fileName) {
        long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(filePath)) {
            filePath = "/Users/luchao/Desktop";
        }
        StringBuilder content = new StringBuilder();
        boolean flag = false;
        BufferedWriter out = null;
        try {
            if (result != null && !result.isEmpty()) {
//	                fileName +=  ".txt";
                File pathFile = new File(filePath);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                String relFilePath = filePath + File.separator + fileName;
                File file = new File(relFilePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
//	                //标题头
//	                out.write("curr_time,link_id,travel_time,speed,reliabilitycode,link_len,adcode,time_stamp,state,public_rec_time,ds");
//	                out.newLine();
                for (String info : result) {
//	                	System.out.println(info+"*********************************");
                    out.write(info);
                    out.newLine();
                }
                flag = true;
//	                logger.info("写入文件耗时：*********************************" + (System.currentTimeMillis() - start) + "毫秒");
//	                System.out.println("写入"+relFilePath+"文件耗时：*********************************\n" + (System.currentTimeMillis() - start) + "毫秒");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return flag;
        }
    }

}

package com.opensesame.api;

import cn.jiguang.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.bean.MarsTimer;
import com.mars.common.annotation.bean.MarsWrite;
import com.mars.common.base.InitBean;
import com.mars.jdbc.core.helper.templete.JdbcTemplate;
import com.opensesame.api.vo.LCFanyiVO;
import com.opensesame.core.dao.LCFanyiDAO;
import com.opensesame.core.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * service层
 */
@MarsBean("fanyiApiService")
public class LCFanyiApiService implements InitBean {

    private Logger logger = LoggerFactory.getLogger(LCFanyiApiService.class);

    ///mac写入路径
    private static final String realPathForMac = "/Volumes/Untitled/code/ZMKMProject2/ZMKMProject2/Resources/Localizables/";
    ///win写入路径
    private static final String realPathForWin = "D:\\apache-tomcat-9.0.27\\Localizables\\";

    /**
     * 所以自然也就不会调用dao的方法了，这里注入进来dao，只是为了演示IOC的用法
     */
    @MarsWrite
    private LCFanyiDAO fanyiDAO;

    /**
     * 初始化bean示例
     */
    @Override
    public void init() {
        logger.info("执行了初始化bean, bean里面注入了DAO: " + fanyiDAO);
    }

    public Result getTranslate(LCFanyiVO fanyiVO) throws InterruptedException {
        JobService.push(() -> {
            try {
                String results = "https://lab.magiconch.com/api/hosts/unicom-host";
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
    public Result translateRequest(LCFanyiVO fanyiVO) throws Exception {
        Result responce = ResultGenerator.genSuccessResult("");
        if (getYiNum(fanyiVO.getQuery()) == 0) {
            System.out.println(fanyiVO.getQuery() + " getYiNum = " + getYiNum(fanyiVO.getQuery()));
            try {
                if (fanyiVO.getQuery().length() > 0) {
                    int res = fanyiDAO.insertFromJob(fanyiVO.getQuery());
//                    int res = JdbcTemplate.get().update("INSERT INTO fanyi_job (original) VALUES('" + fanyiVO.getQuery() + "'); ");
                    System.out.println("fanyi_job sql insert()	" + res);
                }
            } catch (Exception e2) {
            }
            JobService.push(() -> {
                try {
                    Integer job_resultCount = fanyiDAO.selectCountFromJob();
                    System.out.println("\n" + job_resultCount);
                    if (fanyiVO.getQuery().length() > 0 && job_resultCount > 0) {
                        Date date = new Date();
                        JiGuangPushUtil.pushNotice("alias", "481", "你有新翻译'" + fanyiVO.getQuery() + "'任务，请及时处理" + date.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
//
                }
                return true;
            });
        } else {
            String res = JdbcTemplate.get().selectOne("select " + fanyiVO.getLang() + " from fanyi f where f.original='" + fanyiVO.getQuery() + "' limit 0,1", Map.class).get(fanyiVO.getLang()).toString();
            if (res.length() > 0) {
                System.out.println("数据库里有该数据	翻译为	" + res);
                responce.setData(res);
                return responce;
            }
        }

        return responce;
    }

    /**
     * @param query 查某词
     * @return 查询ALL翻译结果数量
     */
    public int getYiNum(String query) {

        return fanyiDAO.selectCountById(query);
    }

    /**
     * 定时任务示例
     * 5秒执行一次
     */
    @MarsTimer(loop = 5000)
    public int timerExp() {
        logger.info("执行了定时任务");
        try {
            //System.err.println("定时执行翻译任务时间: " + LocalDateTime.now() + " >> " + getNumData());
            //num++;
            Integer job_resultCount = fanyiDAO.selectCountFromJob();
            if (job_resultCount > 0) {
                List<Map> job_resultArr2 = fanyiDAO.selectFromJob();

                for (int i = 0; i < job_resultCount; i++) {
                    System.out.println(i + "\n" + job_resultArr2.get(i));
                    String tempQuery = (String) job_resultArr2.get(i).get("original");
                    fanyiJob1(tempQuery, (job_resultCount - i));
//					Thread.sleep(1000*job_resultArr2.size());
                }
                Date date = new Date();
                JiGuangPushUtil.pushNotice("alias", "481", "你的翻译任务已经执行完毕，请及时查看" + date.toString());
            }
            return job_resultCount;

            //System.err.println("结束翻译任务定时任务时间: " + LocalDateTime.now() + " >> " + getNumData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return 0;
    }

    /**
     * 百度翻译renwu query 要翻译的内容 lang
     * 翻译成什么语言（zh:中文;cht:繁体中文';en:英文';fra:法语';spa:西班牙语';）
     *
     * @throws Exception
     */
    public void fanyiJob1(String query, int isJob2) throws InterruptedException {
        if (query == null || query.length() == 0) {
            return;
        } else {

            //JobService.push(()-> {
            try {
                long start = System.currentTimeMillis();
                //model.insert();
                Object result01;
                Object result02;
                Object result03;
                Object result04;
                Object result05;
                if (getYiNum(query) > 0) {
                    return;
                }
//                		Thread.sleep(2000);
                result01 = this.baidutranslate(query, "zh");
                if (getYiNum(query) > 0) {//return true;
                }
                Thread.sleep(2000);
                result02 = this.baidutranslate(query, "cht");
                if (getYiNum(query) > 0) {//return true;
                }
                Thread.sleep(2000);
                result03 = this.baidutranslate(query, "en");
                if (getYiNum(query) > 0) {//return true;
                }
                Thread.sleep(2000);
                result04 = this.baidutranslate(query, "fra");
                if (getYiNum(query) > 0) {//return true;
                }
                Thread.sleep(2000);
                result05 = this.baidutranslate(query, "spa");
                if (getYiNum(query) > 0) {//return true;
                }
                Thread.sleep(2000);
                System.out.println("翻译结果:" + result01);
                System.out.println("翻译结果:" + result02);
                System.out.println("翻译结果:" + result03);
                System.out.println("翻译结果:" + result04);
                System.out.println("翻译结果:" + result05);


                if (
                        ((String) result01).length() > 0 &&
                                ((String) result02).length() > 0 &&
                                ((String) result03).length() > 0 &&
                                ((String) result04).length() > 0 &&
                                ((String) result05).length() > 0
                ) {
                    try {
                        String addSql = "INSERT INTO fanyi VALUES(\""
                                + query
                                + "\", \""
                                + result01
                                + "\",\""
                                + result02
                                + "\",\""
                                + result03
                                + "\",\""
                                + result04
                                + "\",\""
                                + result05
                                + "\"); ";
                        int out_res = JdbcTemplate.get().update(addSql.toString());
                        System.out.println("fanyi sql insert()	" + out_res);
                        long endStart = System.currentTimeMillis() - start;
                        System.out.println("翻译耗时：*********" + endStart + "毫秒");

                        int del_res = JdbcTemplate.get().update("DELETE FROM fanyi_job where original = '" + query + "'");
                        System.out.println("del_fanyi_job " + del_res);
                        ///写入翻译数据文件到项目里
                        if (out_res == 1) {
                            if (isJob2 == 1) {
                                translateResult();
                            }
                        }

                    } catch (Exception e) {
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
//    			return true;
//    		});
        }
    }


    public Object baidutranslate(String query, String lang) {
        Boolean isBaidu = false;
//        if (isBaidu) {
//            TransApi api = new TransApi(APP_ID1, SECURITY_KEY1);
////		if (job.length != 0)
//            {
//                api = new TransApi(APP_ID, SECURITY_KEY);
//            }
//            String desc = "";
//            if ("zh".equalsIgnoreCase(lang)) {
//                desc = TransApi.convert(api.getTransResult(query, "auto", "zh"));// 中文
//            } else if ("cht".equalsIgnoreCase(lang)) {
//                desc = TransApi.convert(api.getTransResult(query, "auto", "cht"));// 繁体中文
//            } else if ("en".equalsIgnoreCase(lang)) {
//                desc = TransApi.convert(api.getTransResult(query, "auto", "en"));// 英文
//            } else if ("fra".equalsIgnoreCase(lang)) {
//                desc = TransApi.convert(api.getTransResult(query, "auto", "fra"));// 法语
//            } else if ("spa".equalsIgnoreCase(lang)) {
//                desc = TransApi.convert(api.getTransResult(query, "auto", "spa"));// 西班牙语
//            } else {
//                desc = TransApi.convert(api.getTransResult(query, "auto", "zh"));// 中文
//            }
//            try {
//                JSONObject jobj = JSON.parseObject(desc);
//                String desc_ = jobj.getString("trans_result");
//                JSONArray jarr = JSON.parseArray(desc_);
//                JSONObject temp = jarr.getJSONObject(0);
//                return temp.getString("dst");
//            } catch (Exception e) {
//                try {
//                    Thread.sleep(1000);
//                    //return baidutranslate(query,lang);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//                System.err.println("翻译失败<<" + query + ">>为" + "baidu desc = " + desc);
//                return "";// temp.getString("dst");
//            }
//        }

        ///google fanyi
        try {
            if ("zh".equalsIgnoreCase(lang)) {
                return query;
            }
            JSONObject jobj = JSON.parseObject((String) googletTranslate(query, lang));
            String desc_ = jobj.getString("data");
            return desc_;
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
                //return baidutranslate(query,lang);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.err.println("翻译失败<<" + query + ">>为" + "Google lang = " + lang);
            return "";// temp.getString("dst");
        }

//		return ;
    }

    public Object googletTranslate(String query, String lang) {

        String desc = "";
        if ("zh".equalsIgnoreCase(lang)) {
            desc = "zh_cn";// 中文
        } else if ("cht".equalsIgnoreCase(lang)) {
            desc = "zh_tw";// 繁体中文
        } else if ("en".equalsIgnoreCase(lang)) {
            desc = "en";// 英文
        } else if ("fra".equalsIgnoreCase(lang)) {
            desc = "fr";// 法语
        } else if ("spa".equalsIgnoreCase(lang)) {
            desc = "es";// 西班牙语
        } else {
            desc = "zh_cn";// 中文
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("targetLang", desc);
            params.put("text", query);
            return HttpGetUtil.get("http://18.162.56.208:8880/app/googleapitranslate.do", params);
//			return temp.getString("dst");
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
                //return baidutranslate(query,lang);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.err.println("翻译失败<<" + query + ">>为" + "baidu desc = " + desc);
            return "";// temp.getString("dst");
        }

    }

    /**
     * 写入翻译 All翻译的内容 lang
     *
     * @return
     * @throws InterruptedException
     */
    public Map<String, Object> translateResult() throws Exception {
        Map<String, Object> responce = new HashMap<>();
        responce.put("code", 200);
        responce.put("msg", "");
        String[] langStrings = {"zh", "cht", "en", "fra", "spa"};
//		List resultTempArr = new ArrayList();
        long start = System.currentTimeMillis();
        for (int i = 0; i < langStrings.length; i++) {
            String array_element = langStrings[i];
            writeYiResultWithLang(array_element);
        }

        long endStart = System.currentTimeMillis() - start;
        System.out.println("写入Localizable文件耗时：*********" + endStart + "毫秒");

//		String cmd = "";
//		if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
//			cmd = realPathForWin + "gitcmd\\git_push.bat ";
//		} else {
//			cmd = "sh " + realPathForMac + "gitcmd/git_push.sh ";
//		}
//		Process process = Runtime.getRuntime().exec(cmd);
//		process.waitFor();
//		responce.put("data", cmd);
//		responce.put("data", resultTempArr);

        return responce;
    }

    /*
     * * 写入 翻译成什么语言（zh:中文;cht:繁体中文';en:英文';fra:法语';spa:西班牙语';）
     */
    public Map<String, Object> writeYiResultWithLang(String lang) throws Exception {
        Map<String, Object> responce = new HashMap<>();
        Integer resultArr1Count = fanyiDAO.selectCount();

        List<String> resultTempArr = new ArrayList<String>();
        for (int j = 0; j < resultArr1Count; j++) {
            List<Map> resultArr1 = fanyiDAO.select();
            Map<String, Object> map1 = resultArr1.get(j);
            String tempString = "\"" + map1.get("original").toString() + "\" = \"" + map1.get(lang).toString() + "\";";

            resultTempArr.add(tempString);
        }
        if (StringUtils.isEmpty(lang)) {
            responce.put("code", -1000);
            responce.put("msg", "query不能为空");
            return responce;
        }
        if (StringUtils.isEmpty(lang)) {
            responce.put("code", -1000);
            responce.put("msg", "lang不能为空");
            return responce;
        }
        responce.put("code", 200);
        responce.put("msg", "");
        responce.put("data", resultTempArr);

        String filePath = "/Users/luchao/Desktop";
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
            filePath = realPathForWin;
        } else {
            filePath = realPathForMac;
        }
        if ("zh".equalsIgnoreCase(lang)) {
            filePath = filePath + "zh-Hans.lproj";
        } else if ("cht".equalsIgnoreCase(lang)) {
            filePath = filePath + "zh-Hant.lproj";
        } else if ("en".equalsIgnoreCase(lang)) {
            filePath = filePath + "en.lproj";
        } else if ("fra".equalsIgnoreCase(lang)) {
            filePath = filePath + "fr.lproj";
        } else if ("spa".equalsIgnoreCase(lang)) {
            filePath = filePath + "es.lproj";
        } else {
            filePath = filePath + "zh-Hant.lproj";
        }
        LCWriteUdidData.writeDataHubData(resultTempArr, filePath, "Localizable.strings");//(" + lang + ")

        return responce;
    }
}

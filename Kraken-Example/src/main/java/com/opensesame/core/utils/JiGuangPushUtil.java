package com.opensesame.core.utils;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.Date;
import java.util.HashMap;

/**
 * 极光推送工具类
 * created by ZhangCH on 2019/11/7
 */
public class JiGuangPushUtil {

    //两个参数分别填写你申请的masterSecret和appKey
    private static JPushClient jPushClient = new JPushClient("", "");

    /**
     * 通知推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     *
     * @param type  推送方式：1、“tag”标签推送，2、“alias”别名推送
     * @param value 推送的标签或别名值
     * @param alert 推送的内容
     */
    public static void pushNotice(String type, String value, String alert) {
        Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.all());//设置接受的平台，all为所有平台，包括安卓、ios、和微软的
        //设置如果用户不在线、离线消息保存的时间
        Options options = Options.sendno();
        options.setTimeToLive(86400l);    //设置为86400为保存一天，如果不设置默认也是保存一天
        builder.setOptions(options);
        //设置推送方式
        if (type.equals("alias")) {
            builder.setAudience(Audience.alias(value));//根据别名推送
        } else if (type.equals("tag")) {
            builder.setAudience(Audience.tag(value));//根据标签推送
        } else {
            builder.setAudience(Audience.all());//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }
        //设置为采用通知的方式发送消息
        builder.setNotification(Notification.alert(alert));
        PushPayload pushPayload = builder.build();
        try {
            //进行推送，实际推送就在这一步
//            PushResult pushResult=jPushClient.sendPush(pushPayload);
            PushResult pushResult01 = jPushClient.sendPush(buildPushObject_ios_audienceMore_regId(value, alert));
            //testSendIosAlert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testSendIosAlert() {
        JPushClient jpushClient = new JPushClient("448b0994c6d0d27a641808ea", "b8abaf2dfbabfba134721802");

        IosAlert alert = IosAlert.newBuilder()
                .setTitleAndBody("test alert", "subtitle", "test ios alert json")
                //.setLaunchImage("http://www.opensesameclub.com:8081/files/apple/image.512x512.png")
                .setActionLocKey("PLAY")
                .build();
        try {
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.ios())
                    .setAudience(Audience.alias("481"))
                    .setNotification(Notification.ios(alert, new HashMap<String, String>()))
                    .build();
            PushResult result = jpushClient.sendPush(payload);
            System.out.println("Got result - " + result);
        } catch (APIConnectionException e) {
//            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
//            LOG.error("Error response from JPush server. Should review and fix it. ", e);
//            LOG.info("HTTP Status: " + e.getStatus());
//            LOG.info("Error Code: " + e.getErrorCode());
//            LOG.info("Error Message: " + e.getErrorMessage());
        }
    }

    /**
     * 构建推送对象：平台是 Andorid 与 iOS，按照registrationId推送
     * 推送内容是 - 内容为 msgContent 的消息，并且附加字段 from = JPush。
     * 作者:温海金
     * 最后更改时间 : 2017年2月20日 下午4:20:46
     */
    public static PushPayload buildPushObject_ios_audienceMore_regId(String registrationId, String msgContent) {
        IosAlert alert = IosAlert.newBuilder()
                .setTitleAndBody("fanyi alert", "subtitle", "" + msgContent)
                //.setLaunchImage("http://www.opensesameclub.com:8081/files/apple/image.512x512.png")
                .setActionLocKey("PLAY")
                .build();
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(
                        Audience.newBuilder()
                                .addAudienceTarget(AudienceTarget.alias(registrationId))
                                .build()
                )
//                .setMessage(Message.newBuilder()
//                		.setTitle("title")
//                        .setMsgContent(msgContent)
//                        .addExtra("from", "JPush")
//                        .build())
                //.setAudience(Audience.all())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)
                        .build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(
                                IosNotification.newBuilder()
                                        .setAlert(alert)
                                        .setSound("im_normal.caf")//这一步是关键，设置默认声音，这样就会使用手机本身的设置
                                        .addExtra("from", "Jpush")
                                        .build()

                        )
                        .build())
                .build();
    }

    /**
     * 自定义消息推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     *
     * @param type  推送方式：1、“tag”标签推送，2、“alias”别名推送
     * @param value 推送的标签或别名值
     * @param alert 推送的内容
     */
    public static void pushMsg(String type, String value, String alert) {
        Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.all());//设置接受的平台
        if (type.equals("alias")) {
            builder.setAudience(Audience.alias(value));//别名推送
        } else if (type.equals("tag")) {
            builder.setAudience(Audience.tag(value));//标签推送
        } else {
            builder.setAudience(Audience.all());//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }
        Message.Builder newBuilder = Message.newBuilder();
        newBuilder.setMsgContent(alert);//消息内容
        Message message = newBuilder.build();
        builder.setMessage(message);
        PushPayload pushPayload = builder.build();
        try {
            PushResult pushResult = jPushClient.sendPush(pushPayload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //给标签为kefu的用户进行消息推送
//        JiGuangPushUtil.pushNotice("tag","kefu","你有新的任务，请及时处理");
//        JiGuangPushUtil.pushMsg("all","","222");
        Date date = new Date();
        JiGuangPushUtil.pushNotice("alias", "481", "你有新消息，请及时处理" + date.toString());
//        JiGuangPushUtil.pushNotice("all","","你有新消息，请及时处理"+date.toString());
//        BigDecimal bigDecimal=new BigDecimal("");
//        System.err.println(
//                StringUtils.isEmpty(String.valueOf(bigDecimal)));
    }


//            <dependency>
//            <groupId>cn.jpush.api</groupId>
//            <artifactId>jiguang-common</artifactId>
//            <version>1.1.8</version>
//        </dependency>
//
//        <dependency>
//            <groupId>cn.jpush.api</groupId>
//            <artifactId>jpush-client</artifactId>
//            <version>3.4.7</version>
//        </dependency>
//
//        <dependency>
//            <groupId>io.netty</groupId>
//            <artifactId>netty-all</artifactId>
//            <version>4.1.6.Final</version>
//            <scope>compile</scope>
//        </dependency>
//
//        <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
//        <dependency>
//            <groupId>org.aspectj</groupId>
//            <artifactId>aspectjweaver</artifactId>
//            <version>1.9.6</version>
//            <scope>runtime</scope>
//        </dependency>

}

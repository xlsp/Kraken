package com.opensesame.api;

import com.mars.common.annotation.api.MarsApi;
import com.mars.common.annotation.api.MarsReference;
import com.mars.common.annotation.api.RequestMethod;
import com.mars.common.annotation.enums.RefType;
import com.mars.common.annotation.enums.ReqMethod;
import com.opensesame.api.vo.ExpVO;
import com.opensesame.api.vo.LCRestVO;
import com.opensesame.core.utils.Result;

import java.util.List;

/**
 * Controller变成了一个interface
 */
@MarsApi(refBean = "restApiService")
public interface LCRestApi {

    /**
     * get请求示例,此示例引用的是expApiServiceTwo里的方法
     * http://127.0.0.1:8080/expGetRequest?name=张三&names=王五&names=赵六
     *
     * @param expVO
     * @return
     */
    //beanName = "expApiServiceTwo", bean 名称为 expApiServiceTwo，现在需要通过 MarsApi 来调用 aService 里面的 expApiServiceTwo 方法
    @MarsReference(refName = "expGetRequest", refType = RefType.METHOD)
    List<ExpVO> expGetRequest(ExpVO expVO);

    /**
     * post请求示例
     * http://127.0.0.1:8080/expPostRequest
     *
     * @param restVO
     * @return
     */
    @RequestMethod(ReqMethod.POST)
    Result expPostRequest(LCRestVO restVO);

    /**
     * 文件上传示例
     * http://127.0.0.1:8080/restUploadRequest
     *
     * @param restVO
     * @return
     */
    @RequestMethod(ReqMethod.PUT)
    String restUploadRequest(LCRestVO restVO) throws Exception;

//    /**
//     * 文件下载示例
//     * http://127.0.0.1:8080/expDownLoadRequest
//     */
//    void expDownLoadRequest(HttpMarsResponse response) throws Exception;
}
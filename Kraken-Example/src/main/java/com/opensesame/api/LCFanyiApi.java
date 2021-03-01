package com.opensesame.api;

import com.mars.common.annotation.api.MarsApi;
import com.mars.common.annotation.api.MarsLog;
import com.mars.common.annotation.api.MarsReference;
import com.mars.common.annotation.api.RequestMethod;
import com.mars.common.annotation.enums.RefType;
import com.mars.common.annotation.enums.ReqMethod;
import com.opensesame.api.vo.LCFanyiVO;
import com.opensesame.core.utils.Result;

/**
 * Controller变成了一个interface
 */
@MarsApi(refBean = "fanyiApiService")
public interface LCFanyiApi {

    /**
     * get请求示例
     * http://127.0.0.1:8080/getTranslate?query=测试&lang=zh
     *
     * @param fanyiVO
     * @return
     */
    @MarsReference(refName = "getTranslate", refType = RefType.METHOD)
    @MarsLog
    Result getTranslate(LCFanyiVO fanyiVO);

    /**
     * post请求示例
     * http://127.0.0.1:8080/translateRequest
     *
     * @param fanyiVO
     * @return
     */
    @RequestMethod({ReqMethod.POST, ReqMethod.GET})
    @MarsLog
    Result translateRequest(LCFanyiVO fanyiVO);

}

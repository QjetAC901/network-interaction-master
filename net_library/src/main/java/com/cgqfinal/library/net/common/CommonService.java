package com.cgqfinal.library.net.common;


import com.cgqfinal.library.token.TokenResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 默认的网络请求Service
 */

public interface CommonService {

    /**
     * //直接使用网址下载
     * @param url
     *  网络地址
     * @return
     *  返回数据
     */
    @Streaming// 标记 表示相应体以流的形式返回
    @GET
    Observable<ResponseBody> download(@Url String url);

    /**
     * 获取token
     *
     * @return Token实体类
     */
    @POST("ouath/token")
    Observable<TokenResponse> refreshToken();
}

package com.musi.cgqfinal.service;


import com.cgqfinal.library.net.common.HttpApiService;

/**
 * Created by Chen_Mr on 2018/4/11.
 */

public class RetrofitHelper {

    private static BelleImgService service;

    public static BelleImgService getApiService(String http){
        return HttpApiService.getApiService(BelleImgService.class, http);
    }
}

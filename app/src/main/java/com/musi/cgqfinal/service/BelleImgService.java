package com.musi.cgqfinal.service;

import com.musi.cgqfinal.bean.Welfare;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by dell on 2018/5/18.
 */

public interface BelleImgService {

    @Headers("Cache-Control: public, max-age=100")
    @GET("data/福利/{number}/{pages}")
    Observable<List<Welfare>> getWelfare(@Path("number") int number, @Path("pages") int pages);

}

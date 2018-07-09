package com.cgqfinal.library.net.converter;

import com.cgqfinal.library.net.common.BasicResponse;
import com.cgqfinal.library.net.common.ErrorCode;
import com.cgqfinal.library.net.exception.NoDataExceptionException;
import com.cgqfinal.library.net.exception.ServerResponseException;
import com.cgqfinal.library.net.exception.TokenInvalidException;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {

    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {
            BasicResponse response = (BasicResponse) adapter.fromJson(value.charStream());
            if (response.getCode() == ErrorCode.TOKEN_EXPIRED) {
                // token 过期
                throw new TokenInvalidException();
            } else if (response.isError()) {
                // 特定 API 的错误，在相应的 DefaultObserver 的 onError 的方法中进行处理
                throw new ServerResponseException(response.getCode(), response.getMessage());
            } else if (!response.isError()) {
                if (response.getResults() != null)
                    return response.getResults();
                else
                    throw new NoDataExceptionException();
            }
        } finally {
            value.close();
        }
        return null;
    }
}

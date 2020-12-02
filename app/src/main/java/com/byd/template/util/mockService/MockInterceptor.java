package com.byd.template.util.mockService;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by liaohailiang on 2018/12/4.
 */
public abstract class MockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!accept(chain.request())) {
            return chain.proceed(chain.request());
        }
        String path = chain.request().url().encodedPath();
        String method = chain.request().url().queryParameter("method");
        Map<String, ResponseInfo> infoMap = MockDataManager.get().getInfoMap();
        if (infoMap.containsKey(method)) {
            ResponseInfo responseInfo = infoMap.get(method);
            Response.Builder builder = new Response.Builder()
                    .message(responseInfo.getMessage())
                    .code(responseInfo.getCode())
                    .protocol(Protocol.get(responseInfo.getProtocol()))
                    .request(chain.request())
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseInfo.getBodyAsString().getBytes()));
            Map<String, String> header = responseInfo.getHeader();
            if (header != null && header.size() > 0) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    builder.header(entry.getKey(), entry.getValue());
                }
            }
            Log.d("wwl", "OkHttp====Mock Message:" + responseInfo.getBodyAsString());
            return builder.build();
        } else {
            return chain.proceed(chain.request());
        }
    }

    abstract public boolean accept(Request request);
}

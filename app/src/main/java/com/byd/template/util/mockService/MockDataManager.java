package com.byd.template.util.mockService;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Changed by wang.weili on 2019/12/23.
 */
public class MockDataManager {

    private static final String DEFAULT_DATA_PATH = "mock.json";

    private static class Holder {
        private static final MockDataManager INSTANCE = new MockDataManager();
    }

    public static MockDataManager get() {
        return Holder.INSTANCE;
    }

    private Map<String, ResponseInfo> infoMap = new HashMap<>();

    private MockDataManager() {
    }

    public void init(Context context, String path) {
        try {
            String json = AssetsUtil.getAssetsAsString(context,
                    TextUtils.isEmpty(path) ? DEFAULT_DATA_PATH : path);
            JSONArray methods = new JSONArray(json);
            if(methods.length()!=0){
                for (int i = 0; i < methods.length(); i++) {
                    String method = methods.getString(i);
                    String body = AssetsUtil.getAssetsAsString(context,method+".json");
                    if(body!=null){
                        try{
                            JSONObject bodyBean = new JSONObject(body);
                            ResponseInfo responseInfo = new ResponseInfo(bodyBean);
                            infoMap.put(method,responseInfo);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            infoMap = new HashMap<>();
            e.printStackTrace();
        }
    }

    public Map<String, ResponseInfo> getInfoMap() {
        return infoMap;
    }
}

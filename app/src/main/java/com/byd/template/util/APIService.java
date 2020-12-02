/*
 * Copyright (C) 2019 BYD, Inc. All Rights Reserved.
 */
package com.byd.template.util;

import com.byd.template.constant.Constants;

import java.util.TreeMap;

import org.json.JSONObject;

public class APIService {

    private APIService() {

    }

    private static volatile APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            synchronized (APIService.class) {
                if (instance == null) {
                    instance = new APIService();
                }
            }
        }
        return instance;
    }

    public void init() {
        HttpUtils.getInstance().init();
    }

    /**
     * 登录
     */
    public void login(OnResultListener listener, String user, String psw) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("user", user);
        map.put("pwd", psw);
        JSONObject param = new JSONObject(map);
        HttpUtils.getInstance().post(Constants.url +"Service.action?method=Login",param.toString(),listener);
//        HttpUtils.getInstance().get(Constants.finalWms +"index.do?command=ext_login&loginId="+user+"&password="+psw,listener);
    }

    /**
     * 获取最新版本
     */
    public void GetUpdate(OnResultListener listener){
        HttpUtils.getInstance().get(Constants.url +"api?method=GetUpdate&param="+Constants.filename+"_"+Constants.idx,listener);
    }

    /**
     * 获取调试开关值，即日志输出类型DebugUtil.DEBUG值
     */
    public void GetDebug(OnResultListener listener){
        HttpUtils.getInstance().get(Constants.url +"api?method=GetDebug&param="+Constants.filename+"_"+Constants.idx,listener);
    }

    /**
     * 根据工段获取线体信息
     */
    public void GetLines(OnResultListener listener, String workStation) {
        String[] names1 = {"LOGIN_ID","WORKSTATION","CLIENT_ID"};
        String[] values1 = {"-1",workStation,"1"};
        String param = BaseUtil.getParam(names1, values1);
        HttpUtils.getInstance().post(Constants.url +"api?method=GetLines&param=",param,listener);
    }
}



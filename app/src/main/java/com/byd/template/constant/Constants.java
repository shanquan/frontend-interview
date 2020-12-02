package com.byd.template.constant;

/**
 * Created by wwl on 2018/9/12.
 */

public class Constants {

    public static String finalWms = "";
    public static String clientIp = "";
    public static String url;
    public static int idx = 0;
    // 升级(filename)、调试开关查询使用的名称（filename-idx）
    public static String filename = "PDAapp";
    // 服务器地址获取地址
    public static String[] urls = {"http://10.6.78.237:808/","http://192.168.11.237:808"};
    // wms 办公网,wms 生产网
    private final static String [] [] projects = new String[][] {
            {"宝龙一厂","http://10.5.197.20/","http://172.16.19.20/"},
            {"长沙工厂","http://10.35.64.1","http://172.31.19.1"},
            {"华为笔电","http://10.5.198.5","http://172.16.18.5"},
            {"葵涌工厂","http://10.1.3.40","http://172.18.1.40"},
            {"二厂雷蛇","http://10.5.197.20","http://172.16.19.20"},
            {"七厂","http://10.5.197.2","http://172.16.19.2"}
    };
    public static void setUrl(boolean isOffice){
        if(isOffice){
            url = urls[0];
        }else{
            url = urls[1];
        }
    }
    public static String[] getServerUrls(){
        // 这里设置使用的项目地址
        return projects[idx];
    }
    public static String[] getServers(){
        int length = projects.length;
        String[] servers = new String[projects.length];
        for (int i = 0; i < length; i++){
            servers[i] = projects[i][0];
        }
        return servers;
    }

}

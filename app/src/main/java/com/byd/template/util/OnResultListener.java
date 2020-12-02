/*
 * Copyright (C) 2019 BYD, Inc. All Rights Reserved.
 */
package com.byd.template.util;


public interface OnResultListener {
    void onResult(String result);

    void onError(int code,String msg);
}

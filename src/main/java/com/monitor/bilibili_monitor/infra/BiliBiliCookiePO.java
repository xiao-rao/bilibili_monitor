package com.monitor.bilibili_monitor.infra;

import lombok.Data;

import java.io.Serializable;

@Data
public class BiliBiliCookiePO implements Serializable {

    private String DedeUserID;
    private String DedeUserID__ckMd5;
    private String SESSDATA;
    private String bili_jct;


    public String getCookies() {
        return
                "DedeUserID=" + DedeUserID +
                ";DedeUserID__ckMd5=" + DedeUserID__ckMd5 +
                ";SESSDATA=" + SESSDATA +
                ";bili_jct=" + bili_jct;
    }

}

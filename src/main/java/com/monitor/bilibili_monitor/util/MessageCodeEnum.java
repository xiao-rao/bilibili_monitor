package com.monitor.bilibili_monitor.util;

public enum MessageCodeEnum {
    success(200, "正常"),

    not_login(401, "暂无权限"),

    parameter_fail(403, "参数错误"),

    fail(500, "服务器内部繁忙,请稍后在试"),

    sms_fail(1300, "短信发送失败"),

    wx_s_fail(1400, "微信公众号消息推送失败"),

    excel_check_title(1501, "请检查导入excel模版是否正确!"),
            ;
    private int code;

    /**
     * 错误描述
     */
    private String desc;


    MessageCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }
}

package com.monitor.bilibili_monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "二维码信息对象")
public class LoginQRVO implements Serializable {

    @Schema(description = "二维码连接")
    private String url;

    @Schema(description = "二维码密钥")
    private String oauthKey;
}

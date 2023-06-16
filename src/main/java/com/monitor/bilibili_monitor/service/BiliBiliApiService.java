package com.monitor.bilibili_monitor.service;

import com.monitor.bilibili_monitor.infra.BiliBiliCookiePO;
import com.monitor.bilibili_monitor.util.ResultDTO;
import com.monitor.bilibili_monitor.vo.LoginQRVO;
import com.monitor.bilibili_monitor.vo.SendMessageLogVO;
import com.monitor.bilibili_monitor.vo.WorksVO;

import java.util.List;

public interface BiliBiliApiService {

    /**
     * 获取稿件的详细信息
     *
     * @param authorId 作者id
     */
    List<WorksVO> getAuthorInfo(String authorId);

    /**
     * 获取视频基本信息
     *
     * @param bvid
     * @return
     */
    boolean getWorksInfo(String bvid);

    /**
     * 获得登陆连接即密钥
     */
    ResultDTO<LoginQRVO> getLoginQR();

    /**
     * 获取扫码状态及登陆信息
     */
    ResultDTO<String> getLoginInfo(String oauthKey);

    /**
     * 检验账号
     */
    ResultDTO<String> checkLogin(Long id);


    /**
     * 重新发送消息
     */
    ResultDTO<String> reSendMessage(SendMessageLogVO vo);

    /**
     * 发送私信
     * @param receiver_id 接受者id
     * @param content 内容
     * @param biliBiliCookiePO cookie
     */
//    void sendMessage(String receiver_id, String content, BiliBiliCookiePO biliBiliCookiePO);

    /**
     * 监听作者
     */
    void monitorTheAuthor();

    /**
     * 监听作品
     */
    void monitorTheWorks();


}

package com.monitor.bilibili_monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.monitor.bilibili_monitor.dto.*;
import com.monitor.bilibili_monitor.infra.*;
import com.monitor.bilibili_monitor.service.*;
import com.monitor.bilibili_monitor.util.Constant;
import com.monitor.bilibili_monitor.util.MessageCodeEnum;
import com.monitor.bilibili_monitor.util.ParameterEncryptionUtil;
import com.monitor.bilibili_monitor.util.ResultDTO;
import com.monitor.bilibili_monitor.vo.*;
import io.github.admin4j.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Slf4j
@Service
public class BiliBiliApiServiceImpl implements BiliBiliApiService {

    public static final String AUTHOR_INFO_URL = "https://api.bilibili.com/x/space/wbi/acc/info";
    public static final String USER_WORKS_URL = "https://api.bilibili.com/x/space/wbi/arc/search";
    public static final String LOGIN_URL = "https://passport.bilibili.com/qrcode/getLoginUrl";
    public static final String TOKEN_URL = "https://passport.bilibili.com/qrcode/getLoginInfo";
    public static final String CHECK_LOGIN_URL = "https://api.bilibili.com/x/web-interface/nav";
    private static final String SEND_MESSAGE_URL = "https://api.vc.bilibili.com/web_im/v1/web_im/send_msg";
    private static final String REPLY_URL = "https://api.bilibili.com/x/v2/reply";
    @Autowired
    private WorksService worksService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private MonitorLogService logService;
    @Autowired
    private SendMessageLogService messageLogService;


    @Override
    public List<WorksVO> getAuthorInfo(String authorId) {
        AccountDTO accountDTO = new AccountDTO();
        MonitorLogPO monitorLog = null;
        List<WorksDTO> worksDTOArrayList = new ArrayList<>();
        QueryAccountDTO queryAccountDTO = new QueryAccountDTO();
        queryAccountDTO.setAccountId(authorId);
        AccountVO one = accountService.findOne(queryAccountDTO);
        try {
            if (null != one) {
                long time = one.getModifiedAt().toEpochSecond(ZoneOffset.UTC);
                getAuthorWorksInfo(authorId, 1, time, worksDTOArrayList);
                AccountPO accountPO = BeanUtil.copyProperties(one, AccountPO.class);
                accountPO.setModifiedAt(LocalDateTime.now());
                accountPO.setAccountWorksCount(one.getAccountWorksCount() + worksDTOArrayList.size());
                accountService.update(accountPO);
                monitorLog = createMonitorLog(authorId, one.getAccountNickname(), Constant.AUTHOR, Constant.YES, String.valueOf(worksDTOArrayList.size()));
            } else {
                Map<String, Object> parameterMap = new HashMap<>();
                parameterMap.put("mid", authorId);
                parameterMap.put("token", "");
                parameterMap.put("platform", "web");
                parameterMap.put("web_location", 1550101);
                HttpRequest httpRequest = HttpRequest.get(AUTHOR_INFO_URL);
                httpRequest.headers(getHeaders());
                ParameterEncryptionUtil.encryption(parameterMap);
                parameterMap.forEach((k, v) -> httpRequest.queryMap(k, v.toString()));
                Response response = httpRequest.execute();
                if (!response.isSuccessful() && null == response.body()) {
                    throw new IllegalArgumentException(response.message());
                }
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                if (jsonObject.getInteger("code") != 0) {
                    throw new IllegalArgumentException(jsonObject.getString("message"));
                }
                //添加作者信息
                accountDTO.setAccountId(authorId);
                accountDTO.setAccountNickname(jsonObject.getJSONObject("data").getString("name"));
                getAuthorWorksInfo(authorId, 1, 0L, worksDTOArrayList);
                accountDTO.setAccountWorksCount(worksDTOArrayList.size());
                accountService.create(accountDTO);
                monitorLog = createMonitorLog(authorId, accountDTO.getAccountNickname(), Constant.AUTHOR, Constant.YES, String.valueOf(worksDTOArrayList.size()));
            }
        } catch (IllegalArgumentException e) {
            String nickname;
            if (null != accountDTO.getAccountNickname()) {
                nickname = accountDTO.getAccountNickname();
            } else if (null != one) {
                nickname = one.getAccountNickname();
            } else {
                nickname = "解析作者信息出错！";
            }
            monitorLog = createMonitorLog(authorId, nickname, Constant.AUTHOR, Constant.NO, e.getMessage());
            log.error("运行时错误日志{}", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            logService.saveLOg(monitorLog);
        }
        return worksService.batchCreate(worksDTOArrayList);
    }

    public static void getAuthorWorksInfo(String authorId, Integer pn, long time, List<WorksDTO> worksDTOArrayList) {
        Map<String, Object> headers = getHeaders();
        Map<String, Object> t = new HashMap<>();
        t.put("mid", authorId);
        t.put("pn", pn);
        t.put("ps", 25);
        t.put("index", 1);
        t.put("order", "pubdate");
        t.put("order_avoided", "true");
        t.put("platform", "web");
        t.put("web_location", 1550101);
        HttpRequest httpRequest = HttpRequest.get(USER_WORKS_URL);
        httpRequest.headers(headers);
        try {
            ParameterEncryptionUtil.encryption(t);
            t.forEach((k, v) -> httpRequest.queryMap(k, v.toString()));
            Response response = httpRequest.execute();
            if (!response.isSuccessful() && null == response.body()) {
                throw new IllegalArgumentException(response.message());
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (jsonObject.getInteger("code") != 0) {
                throw new IllegalArgumentException(jsonObject.getString("message"));
            }
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray jsonArray = data.getJSONObject("list").getJSONArray("vlist");
            if (null == jsonArray || 0 == jsonArray.size()) {
                return;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.getLong("created") < time) {
                    return;
                }
                WorksDTO worksDTO = new WorksDTO();
                worksDTO.setAuthorId(authorId);
                worksDTO.setWorkCommentCount(Integer.valueOf(jsonObject1.getString("comment")));
                worksDTO.setWorkId(jsonObject1.getString("aid"));
                worksDTO.setWorkTitle(jsonObject1.get("title").toString());
                worksDTO.setIsMonitored(0);
                worksDTOArrayList.add(worksDTO);
            }
            pn++;
            //防止接口非法访问
            Thread.sleep(500);
            getAuthorWorksInfo(authorId, pn, time, worksDTOArrayList);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getWorksInfo(String bvid) {
        String aid = ParameterEncryptionUtil.bVidToAid(bvid);
        QueryWorksDTO queryWorksDTO = new QueryWorksDTO();
        queryWorksDTO.setWorkId(aid);
        Integer count = worksService.count(queryWorksDTO);
        Assert.isFalse(count > 0, "该作品已存在！");
        String url = String.format("https://api.bilibili.com/x/web-interface/view?aid=%s", aid);
        HttpRequest httpRequest = HttpRequest.get(url);
        httpRequest.headers(getHeaders());
        try {
            Response response = httpRequest.execute();
            if (response.isSuccessful() && null != response.body()) {
                WorksDTO worksDTO = new WorksDTO();
                String string = response.body().string();
                JSONObject jsonObject = JSON.parseObject(string);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject stat = data.getJSONObject("stat");
                JSONObject owner = data.getJSONObject("owner");
                worksDTO.setWorkTitle(data.getString("title"));
                worksDTO.setWorkId(stat.getString("aid"));
                worksDTO.setWorkCommentCount(stat.getInteger("reply"));
                worksDTO.setAuthorId(owner.getString("mid"));
                worksDTO.setIsMonitored(1);
                return worksService.create(worksDTO);
            } else {
                throw new IOException("输入的链接或bvid错误");
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }


    @Override
    public ResultDTO<LoginQRVO> getLoginQR() {
        ResultDTO<LoginQRVO> resultDTO = new ResultDTO<>();
        HttpRequest get = HttpRequest.get(LOGIN_URL);
        get.headers(getHeaders());
        try {
            Response response = get.execute();
            if (!response.isSuccessful() || null == response.body()) {
                throw new IllegalArgumentException("获取二维码失败！");
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            JSONObject data = jsonObject.getJSONObject("data");
            String oauthKey = data.getString("oauthKey");
            String url = data.getString("url");
            log.info("获得的url是{}", url);
            LoginQRVO loginQRVO = new LoginQRVO();
            loginQRVO.setUrl(url);
            loginQRVO.setOauthKey(oauthKey);
            resultDTO.setData(loginQRVO);
//            getLoginInfo(oathKey);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("获取二维码失败:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @Override
    public ResultDTO<String> getLoginInfo(String oauthKey) {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        HttpRequest post = HttpRequest.post(TOKEN_URL);
        post.headers(getHeaders());
        post.form("oauthKey", oauthKey);
        post.form("gourl", "https://www.bilibili.com/");
        try {
            Response response = post.execute();
            if (!response.isSuccessful() || null == response.body()) {
                throw new IllegalArgumentException(response.message());
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (jsonObject.getBoolean("status")) {
                log.info("扫描成功！！");
                List<String> headers = response.headers("Set-Cookie");
                BiliBiliCookiePO biliBiliCookiePO = new BiliBiliCookiePO();
                headers.forEach(item -> {
                    String[] split = item.split(";")[0].split("=");
                    String key = split[0].trim();
                    String value = split[1].trim();
                    if ("DedeUserID".equals(key)) {
                        biliBiliCookiePO.setDedeUserID(value);
                    }
                    if ("DedeUserID__ckMd5".equals(key)) {
                        biliBiliCookiePO.setDedeUserID__ckMd5(value);
                    }
                    if ("SESSDATA".equals(key)) {
                        biliBiliCookiePO.setSESSDATA(value);
                    }
                    if ("bili_jct".equals(key)) {
                        biliBiliCookiePO.setBili_jct(value);
                    }
                });
                String cookies = JSONUtil.toJsonStr(biliBiliCookiePO);
                log.info("获得的coookie是{}", cookies);
                HttpRequest httpRequest = HttpRequest.get(CHECK_LOGIN_URL);
                httpRequest.headers(getHeaders());
                httpRequest.header("cookie", biliBiliCookiePO.getCookies());
                Response execute = httpRequest.execute();
                JSONObject jsonObject1 = JSON.parseObject(execute.body().string());
                if (jsonObject1.getInteger("code") == 0) {
                    JSONObject data = jsonObject1.getJSONObject("data");
                    String uname = data.getString("uname");
                    String mid = data.getString("mid");
                    QueryUserDTO queryUserDTO = new QueryUserDTO();
                    queryUserDTO.setUserId(mid);
                    UserPO userPO = userService.findOne(queryUserDTO);
                    if (null == userPO) {
                        userPO = new UserPO();
                        userPO.setUserId(mid);
                        userPO.setUserName(uname);
                        userPO.setUserCookie(cookies);
                        userPO.setIsValid(Constant.YES);
                        userPO.setStatus(Constant.YES);
                        userPO.setReplyContent("hello");
                        userPO.setCreatedAt(LocalDateTime.now());
                        userService.create(userPO);
                    } else {
                        userPO.setUserId(mid);
                        userPO.setUserName(uname);
                        userPO.setUserCookie(cookies);
                        userPO.setIsValid(Constant.YES);
                        userPO.setStatus(Constant.YES);
                        userPO.setUpdatedAt(LocalDateTime.now());
                        userService.update(userPO);
                    }
                    resultDTO.setData("登陆成功！获取账号信息成功");
                } else {
                    resultDTO.setData("登陆成功！获取信息失败！请在管理页面重新检测账号");
                }
            } else {
                String data = jsonObject.getString("data");
                if (data.equals("-2")) {
                    log.error("扫码超时！");
                    resultDTO.setSuccess(false);
                    resultDTO.setData("-2");
                } else if (data.equals("-4")) {
                    log.info("未扫描！");
                    resultDTO.setSuccess(false);
                    resultDTO.setData("-4");
                } else if (data.equals("-5")) {
                    log.info("已扫描未确认！");
                    resultDTO.setSuccess(false);
                    resultDTO.setData("-5");
                } else {
                    log.error("密钥错误！");
                    resultDTO.setSuccess(false);
                    resultDTO.setData("-1");
                }
            }
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("获取登陆账号状态出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }


    @Override
    public ResultDTO<String> checkLogin(Long id) {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        try {
            QueryUserDTO queryUserDTO = new QueryUserDTO();
            queryUserDTO.setId(id);
            UserPO one = userService.findOne(queryUserDTO);
            String userCookie = one.getUserCookie();
            BiliBiliCookiePO biliBiliCookiePO = JSONUtil.toBean(userCookie, BiliBiliCookiePO.class);
            HttpRequest httpRequest = HttpRequest.get(CHECK_LOGIN_URL);
            httpRequest.headers(getHeaders());
            httpRequest.header("cookie", biliBiliCookiePO.getCookies());
            Response response = httpRequest.execute();
            if (!response.isSuccessful() || null == response.body()) {
                throw new IllegalArgumentException(response.message());
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (jsonObject.getInteger("code") != 0) {
                one.setIsValid(0);
                userService.update(one);
                resultDTO.setData("账号登陆状态失效！请重新扫码！");
            } else {
                resultDTO.setData(String.format("%s 登陆成功！状态正常！", one.getUserName()));
            }
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("检验账号状态出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @Override
    public ResultDTO<String> reSendMessage(SendMessageLogVO vo) {
        UserVO randomUser = getRandomUser();
        try {
            if (null == randomUser) {
                return ResultDTO.fail("没有可用账号发送私信");
            } else {
                SendMessageLogPO po = BeanUtil.copyProperties(vo, SendMessageLogPO.class);
                sendMessage(po, randomUser);
                messageLogService.updateSend(po);
            }
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("重新发送私信出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return ResultDTO.success("请求成功！");
    }

//    public static void main(String[] args) {
//        BiliBiliCookiePO biliBiliCookiePO = new BiliBiliCookiePO();
//        biliBiliCookiePO.setBili_jct("2a76384651e93a3b94844b757f163e42");
//        biliBiliCookiePO.setDedeUserID("1687506845");
//        biliBiliCookiePO.setSESSDATA("e38b7e4d%2C1701432757%2C93f71*61");
//        biliBiliCookiePO.setDedeUserID__ckMd5("d421e3dbc9dc1524");
//        sendMessage("1687506845", "hello", biliBiliCookiePO);
//        Map<Integer, List<String>> map = new HashMap<>();
//        List<String> strings = map.computeIfAbsent(1, k -> new ArrayList<>());
//        strings.add("123");
//        System.out.println(map);
//    }

    //    @Override
    public void batchSendMessage(List<SendMessageLogPO> list) {
        log.info("数据{}", list);
        UserVO randomUser = getRandomUser();
        if (null == randomUser) {
            for (SendMessageLogPO item : list) {
                item.setStatus(Constant.NO);
                item.setErrorMsg("没有可用的账号发送私信！");
                item.setCreateAt(LocalDateTime.now());
            }
        } else {
            for (SendMessageLogPO item : list) {
                sendMessage(item, randomUser);
            }
        }
        messageLogService.batchSave(list);
    }


    private void sendMessage(SendMessageLogPO po, UserVO randomUser) {
        String userCookie = randomUser.getUserCookie();
        BiliBiliCookiePO biliBiliCookiePO = JSONUtil.toBean(userCookie, BiliBiliCookiePO.class);
        HttpRequest post = HttpRequest.post(SEND_MESSAGE_URL);
        post.headers(getHeaders());
        post.header("cookie", biliBiliCookiePO.getCookies());
        post.form("msg[sender_uid]", biliBiliCookiePO.getDedeUserID());
        post.form("csrf", biliBiliCookiePO.getBili_jct());
        post.form("msg[dev_id]", ParameterEncryptionUtil.getDevId());
        post.form("msg[content]", String.format("{\"content\": \" %s\"}", randomUser.getReplyContent()));
        post.form("msg[receiver_type]", 1);
        post.form("msg[msg_type]", 1);
        po.setSenderName(randomUser.getUserName());
        po.setContent(randomUser.getReplyContent());
        po.setCreateAt(LocalDateTime.now());
        post.form("msg[receiver_id]", po.getReceiverId());
        post.form("msg[timestamp]", (int) Math.round(System.currentTimeMillis() / 1000.0));
        try {
            Response response = post.execute();
            if (!response.isSuccessful() || null == response.body()) {
                po.setStatus(Constant.NO);
                po.setErrorMsg(response.message());
                log.info("失败原因：{}", response.message());
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (jsonObject.getInteger("code") == 0) {
                po.setStatus(Constant.YES);
                log.info("发送私信成功！");
            } else {
                po.setStatus(Constant.NO);
                po.setErrorMsg(jsonObject.getString("message"));
                log.info(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("检验账号状态出错:{}", e.getMessage());
        }

    }

    @Override
    public void monitorTheAuthor() {
        List<WorksDTO> worksDTOArrayList = new ArrayList<>();
        List<MonitorLogPO> monitorLogPOArrayList = new ArrayList<>();
        List<AccountVO> all = getAuthorWhoNeedToBeMonitor();
        HttpRequest httpRequest = HttpRequest.get(USER_WORKS_URL);
        httpRequest.headers(getHeaders());
        int num = 0;
        for (AccountVO vo : all) {
            try {
                getAuthorWorksInfo(vo.getAccountId(), 1, vo.getModifiedAt().toEpochSecond(ZoneOffset.UTC), worksDTOArrayList);
                num = worksDTOArrayList.size() - num;
                AccountPO accountPO = BeanUtil.copyProperties(vo, AccountPO.class);
                accountPO.setAccountWorksCount(vo.getAccountWorksCount() + num);
                accountPO.setModifiedAt(LocalDateTime.now());
                accountService.update(accountPO);
                MonitorLogPO monitorLog = createMonitorLog(vo.getAccountId(), vo.getAccountNickname(), Constant.AUTHOR, Constant.YES, String.valueOf(num));
                monitorLogPOArrayList.add(monitorLog);
            } catch (IllegalArgumentException e) {
                MonitorLogPO monitorLog = createMonitorLog(vo.getAccountId(), vo.getAccountNickname(), Constant.AUTHOR, Constant.NO, e.getMessage());
                monitorLogPOArrayList.add(monitorLog);
            } catch (Exception e) {
                log.error("系统级异常！！");
            }
        }
        if (worksDTOArrayList.size() > 0) {
            worksService.batchCreate(worksDTOArrayList);
        }
        if (monitorLogPOArrayList.size() > 0) {
            logService.batchCreate(monitorLogPOArrayList);
        }
    }

    @Override
    public void monitorTheWorks() {
        List<MonitorLogPO> monitorLogPOArrayList = new ArrayList<>();
        Map<String, List<SendMessageLogPO>> sendMsgMap = new HashMap<>();
        List<WorksVO> worksVOS = getWorksWhoNeedToBeMonitor();
        HttpRequest httpRequest = HttpRequest.get(REPLY_URL);
        httpRequest.headers(getHeaders());
        httpRequest.queryMap("type", "1");
        httpRequest.queryMap("nohot", "1");
        httpRequest.queryMap("ps", "10");
        for (WorksVO worksVO : worksVOS) {
            try {
                List<SendMessageLogPO> sendMessageLogPOList = sendMsgMap.computeIfAbsent(worksVO.getWorkId(), k -> new ArrayList<>());
                long time = null != worksVO.getModifiedAt() ? worksVO.getModifiedAt().toEpochSecond(ZoneOffset.UTC) : worksVO.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
                httpRequest.queryMap("oid", worksVO.getWorkId());
                getReplies(httpRequest, sendMessageLogPOList, 1, time);
                monitorLogPOArrayList.add(createMonitorLog(worksVO.getWorkId(), worksVO.getWorkTitle(), Constant.WORKS, Constant.YES, String.valueOf(sendMessageLogPOList.size())));
                WorksPO worksPO = BeanUtil.copyProperties(worksVO, WorksPO.class);
                worksPO.setWorkCommentCount(worksVO.getWorkCommentCount() + sendMessageLogPOList.size());
                worksService.update(worksPO);
            } catch (IllegalArgumentException e) {
                monitorLogPOArrayList.add(createMonitorLog(worksVO.getWorkId(), worksVO.getWorkTitle(), Constant.WORKS, Constant.NO, e.getMessage()));
            }
        }
        Map<String, Long> logIdMap = logService.batchCreate(monitorLogPOArrayList);
        List<SendMessageLogPO> list = new ArrayList<>();
        logIdMap.forEach((k, v) -> {
            List<SendMessageLogPO> sendMessageLogPOList = sendMsgMap.get(k);
            sendMessageLogPOList.forEach(item -> {
                item.setLogId(v);
                list.add(item);
            });
        });
        Thread thread = new Thread(() -> {
            log.info("The internal method begins to execute.");
            batchSendMessage(list);
            log.info("The implementation of the internal method is completed.");
        });
        thread.start();
    }


    public void getReplies(HttpRequest httpRequest, List<SendMessageLogPO> sendMessageLogPOList,
                           Integer pn, long time) {
        httpRequest.queryMap("pn", pn.toString());
        try {
            Response response = httpRequest.execute();
            if (!response.isSuccessful() || null == response.body()) {
                log.error("Request failed with code: " + response.code());
                throw new IllegalArgumentException(response.message());
            }
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (jsonObject.getInteger("code") != 0) {
                String message = jsonObject.getString("message");
                log.error("Request failed with message: " + message);
                throw new IllegalArgumentException(message);
            }
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("replies");
            if (null == jsonArray || 0 == jsonArray.size()) {
                return;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject replies = jsonArray.getJSONObject(i);
                Long ctime = replies.getLong("ctime");
                if (ctime < time) {
                    return;
                }
                JSONObject member = replies.getJSONObject("member");
                String mid = member.getString("mid");
                String uname = member.getString("uname");
                String message = replies.getJSONObject("content").getString("message");
                SendMessageLogPO sendMessageLogPO = new SendMessageLogPO();
                sendMessageLogPO.setReceiverId(mid);
                sendMessageLogPO.setReceiverName(uname);
                sendMessageLogPO.setReplies(message);
                sendMessageLogPOList.add(sendMessageLogPO);
            }
            pn++;
            getReplies(httpRequest, sendMessageLogPOList, pn, time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> getHeaders() {
        Map<String, Object> headers = new HashMap<>();
        String authority = "api.bilibili.com";
        String accept = "application/json, text/plain, */*";
        String acceptLanguage = "zh-CN,zh;q=0.9";
        String origin = "https://space.bilibili.com";
        String referer = "https://space.bilibili.com/51588985";
        String secChUa = "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"";
        String secChUaMobile = "?0";
//        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
        String userAgent = ParameterEncryptionUtil.getRandomUserAgent();
        headers.put("authority", authority);
        headers.put("accept", accept);
        headers.put("accept-language", acceptLanguage);
        headers.put("origin", origin);
        headers.put("referer", referer);
        headers.put("sec-ch-ua", secChUa);
        headers.put("sec-ch-ua-mobile", secChUaMobile);
        headers.put("user-agent", userAgent);
        return headers;
    }

    private static Map<String, Object> getParameters(Long authorId, int pn) {
        Map<String, Object> t = new HashMap<>();
        t.put("mid", authorId);
        t.put("token", "");
        t.put("platform", "web");
        t.put("order", "pubdate");
        t.put("pn", pn);
        t.put("ps", 25);
        t.put("order_avoided", "true");
        return t;
    }

    private List<AccountVO> getAuthorWhoNeedToBeMonitor() {
        QueryAccountDTO queryAccountDTO = new QueryAccountDTO();
        queryAccountDTO.setIsMonitored(1);
        return accountService.findAll(queryAccountDTO);
    }

    private List<WorksVO> getWorksWhoNeedToBeMonitor() {
        QueryWorksDTO queryWorksDTO = new QueryWorksDTO();
        queryWorksDTO.setIsMonitored(1);
        return worksService.queryAll(queryWorksDTO);
    }

    private MonitorLogPO createMonitorLog(String id, String name, Integer type, Integer status, String msg) {
        MonitorLogPO monitorLogPO = new MonitorLogPO();
        monitorLogPO.setSourceId(id);
        monitorLogPO.setSourceName(name);
        monitorLogPO.setType(type);
        monitorLogPO.setStatus(status);
        monitorLogPO.setMessage(msg);
        monitorLogPO.setCreatedAt(LocalDateTime.now());
        return monitorLogPO;
//        logService.create(monitorLogPO);
    }

    private UserVO getRandomUser() {
        QueryUserDTO queryUserDTO = new QueryUserDTO();
        queryUserDTO.setIsValid(Constant.YES);
        queryUserDTO.setStatus(Constant.YES);
        List<UserVO> all = userService.findAll(queryUserDTO);
        if (all.size() == 0) {
            return null;
        }
        Random random = new Random();
        int i = random.nextInt(all.size());
        return all.get(i);
    }
}

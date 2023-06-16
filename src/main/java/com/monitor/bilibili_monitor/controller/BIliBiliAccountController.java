package com.monitor.bilibili_monitor.controller;


import com.monitor.bilibili_monitor.dto.QueryUserDTO;
import com.monitor.bilibili_monitor.infra.UserPO;
import com.monitor.bilibili_monitor.service.BiliBiliApiService;
import com.monitor.bilibili_monitor.service.UserService;
import com.monitor.bilibili_monitor.util.MessageCodeEnum;
import com.monitor.bilibili_monitor.util.ResultDTO;
import com.monitor.bilibili_monitor.vo.LoginQRVO;
import com.monitor.bilibili_monitor.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/b")
@Api(tags = "b站账号操作API")
@Slf4j
public class BIliBiliAccountController {
    @Autowired
    private BiliBiliApiService biliBiliApiService;
    @Autowired
    private UserService userService;


    @GetMapping("/getLOginQR")
    @Operation(summary = "获取登陆二维码", method = "GET")
    public ResultDTO<LoginQRVO> getLoginQR(){
       return biliBiliApiService.getLoginQR();
    }

    @GetMapping("/getLoginInfo")
    @Operation(summary = "获取登陆状态", method = "GET")
    public ResultDTO<String> getLoginInfo(
            @Parameter(description = "登陆二维码密钥", required = true) @RequestParam(value = "oauthKey") String oauthKey){
        return biliBiliApiService.getLoginInfo(oauthKey);
    }

    @GetMapping("/checkLogin")
    @Operation(summary = "检验账号状态", method = "GET")
    public ResultDTO<String> checkLogin(
            @Parameter(description = "主键", required = true) @RequestParam(value = "id") Long id){
        return biliBiliApiService.checkLogin(id);
    }


    @ApiOperation("查询所有登陆账号信息")
    @GetMapping("/list")
    public ResultDTO<List<UserVO>> findAll(
            @Parameter(description = "查询关键字") @RequestParam(value = "key", required = false) String key,
            @Parameter(description = "是否监听 1；是 0；否") @RequestParam(value = "type", required = false) Integer type
    ) {
        ResultDTO<List<UserVO>> resultDTO = new ResultDTO<>();
        try {
            QueryUserDTO queryUserDTO = new QueryUserDTO();
            queryUserDTO.setUserName(key);
            queryUserDTO.setIsValid(type);
            List<UserVO> all = userService.findAll(queryUserDTO);
            resultDTO.setData(all);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("查询所有登陆账号信息出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/set/status")
    @Operation(summary = "账号启用/禁用", method = "GET")
    public ResultDTO<Boolean> setStatus(
            @Parameter(description = "主键", required = true) @RequestParam(value = "id") Long id,
            @Parameter(description = "状态：1 - 启用，0 - 禁用", required = true) @RequestParam(value = "status") Integer status
    )
    {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            UserPO userPO = new UserPO();
            userPO.setId(id);
            userPO.setStatus(status);
            resultDTO.setData(userService.update(userPO));
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("设置账号状态出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/set/content")
    @Operation(summary = "设置私信内容", method = "GET")
    public ResultDTO<Boolean> setConbtent(
            @Parameter(description = "主键", required = true) @RequestParam(value = "id") Long id,
            @Parameter(description = "内容", required = true) @RequestParam(value = "content") String content
    )
    {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            UserPO userPO = new UserPO();
            userPO.setId(id);
            userPO.setReplyContent(content);
            resultDTO.setData(userService.update(userPO));
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("设置私信内容出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }
}

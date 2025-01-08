package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.dto.dingtalk.RobotMsg;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对接钉钉API
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/dingtalk")
@Api(tags = "测试20-钉钉API", produces = MediaType.APPLICATION_JSON_VALUE)
public class DingTalkController {

    @ApiOperation("自定义机器人消息")
    @PostMapping("msg/send")
    public String msgSend(RobotMsg dto) {
        try {
            // 接口地址
            DingTalkClient client = new DefaultDingTalkClient(dto.getRobotUrl());

            // at参数
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setIsAtAll(dto.getIsAtAll());
            at.setAtMobiles(dto.getAtMobiles());

            // 文本类型-消息内容
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent("测试告警消息");

            // 请求参数
            OapiRobotSendRequest req = new OapiRobotSendRequest();
            req.setAt(at);
            req.setMsgtype("text");
            req.setText(text);

            OapiRobotSendResponse rsp = client.execute(req, "");
            log.info("【钉钉】rsp={}", rsp);
            if (rsp.getErrorCode().equals("0")) {
                log.info("钉钉推送返回结果：成功");
                return Constants.MSG_SUCCESS;
            } else {
                // 130101： 1分钟发送次数超过20次
                log.info("钉钉推送返回结果：失败，原因：{}", rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("发送钉钉消息异常：", e);
        }
        return Constants.MSG_FAIL;
    }

}

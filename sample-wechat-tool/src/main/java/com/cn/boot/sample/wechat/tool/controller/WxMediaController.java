package com.cn.boot.sample.wechat.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/wx/media")
@Api(tags = "微信素材", produces = MediaType.APPLICATION_JSON_VALUE)
public class WxMediaController {

    @Autowired
    private WxMpService wxService;

    @ApiOperation(value = "上传临时素材")
    @PostMapping("/upload")
    public WxMediaUploadResult sendTemplateMsg(MultipartFile media) {
        try {
            return wxService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "png", media.getInputStream());
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

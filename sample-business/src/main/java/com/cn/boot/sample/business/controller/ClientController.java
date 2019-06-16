package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.dto.BaseRsp;
import com.cn.boot.sample.api.model.dto.client.*;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.IClientService;
import com.cn.boot.sample.api.utils.IdGenerator;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@RestController
@Api(tags = "商户管理", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping(value = "/client")
@Slf4j
public class ClientController {

    @Reference
    private IClientService clientService;

    @ApiOperation(value = "添加商户")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(@RequestBody @Valid ClientAddReq req) {
        log.info("【商户】开始添加：" + req);

        if (req.getId() == null) {
            req.setId(IdGenerator.nextId());
        }

        Client client = new Client();
        BeanUtils.copyProperties(req, client);
        client.setCreateTime(LocalDateTime.now());
        client.setUpdateTime(client.getCreateTime());
        clientService.insertSelective(client);

        Map<String, Object> rspMap = new HashMap<>(1);
        rspMap.put("id", client.getId());

        log.info("【商户】添加成功");
        return new BaseRsp().data(rspMap);
    }

    @ApiOperation(value = "修改商户")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Object edit(@ModelAttribute ClientEditReq req) {
        log.info("【商户】开始修改：" + req);

        Client client = clientService.selectByPrimaryKey(req.getId());
        if (client == null) {
            log.error("商户不存在");
            return new BaseRsp(Constants.CODE_FAILURE, "商户不存在");
        }

        BeanUtils.copyProperties(req, client);
        client.setUpdateTime(LocalDateTime.now());
        clientService.updateByPrimaryKeySelective(client);

        log.info("【商户】修改成功");
        return new BaseRsp().msg(Constants.MSG_SUCCESS);
    }

    @ApiOperation(value = "删除商户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object delete(@ModelAttribute ClientDeleteReq req) {
        log.info("【商户】开始删除：" + req);

        clientService.deleteByPrimaryKey(req.getId());

        log.info("【商户】删除成功");
        return new BaseRsp();
    }

    @ApiOperation(value = "查询商户")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Object get(@ModelAttribute ClientSearchReq req) {
        log.info("【商户】开始查询：" + req);

        Client client = clientService.selectByPrimaryKey(req.getId());
        if (client == null) {
            log.error("商户不存在");
            return new BaseRsp(Constants.CODE_FAILURE, "商户不存在" + req.getId());
        }

        Map<String, Object> rspMap = new HashMap<>(10);
        rspMap.put("id", client.getId());
        rspMap.put("name", client.getName());
        rspMap.put("status", client.getStatus());
        rspMap.put("thirdType", client.getThirdType());
        rspMap.put("thirdSecretId", client.getThirdSecretId());
        rspMap.put("thirdSecretKey", client.getThirdSecretKey());
        rspMap.put("thirdUserId", client.getThirdUserId());

        log.info("【商户】查询成功");
        return new BaseRsp().data(rspMap);
    }

    @ApiOperation(value = "查询商户")
    @GetMapping("/get/{id:\\d+}")
    public Object getById(@PathVariable Long id) {
        log.info("【商户】开始查询：" + id);

        Client client = clientService.selectByPrimaryKey(id);
        if (client == null) {
            log.error("商户不存在");
            return new BaseRsp(Constants.CODE_FAILURE, "商户不存在" + id);
        }

        Map<String, Object> rspMap = new HashMap<>(10);
        rspMap.put("id", client.getId());
        rspMap.put("name", client.getName());
        rspMap.put("status", client.getStatus());
        rspMap.put("thirdType", client.getThirdType());
        rspMap.put("thirdSecretId", client.getThirdSecretId());
        rspMap.put("thirdSecretKey", client.getThirdSecretKey());
        rspMap.put("thirdUserId", client.getThirdUserId());

        log.info("【商户】查询成功");
        return new BaseRsp().data(rspMap);
    }

    @ApiOperation(value = "商户列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(@RequestBody ClientListReq req) {
        log.info("【商户列表】开始：" + req);

        BaseRsp rsp = new BaseRsp();

        Page<Client> page = clientService.listPage(req);

        log.info("【商户列表】查询成功");
        return rsp.data(page.getResult());
    }
}

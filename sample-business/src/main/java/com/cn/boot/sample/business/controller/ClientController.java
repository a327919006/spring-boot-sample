package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.client.ClientAddReq;
import com.cn.boot.sample.api.model.dto.client.ClientEditReq;
import com.cn.boot.sample.api.model.dto.client.ClientListReq;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.model.vo.client.ClientGetRsp;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.UidGeneratorService;
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
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/client")
@Api(tags = "商户管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Reference
    private ClientService clientService;
    @Reference
    private UidGeneratorService uidGeneratorService;

    @ApiOperation("商户-添加")
    @PostMapping("")
    public String insert(@RequestBody @Valid ClientAddReq req) {
        if (req.getId() == null) {
            req.setId(uidGeneratorService.generate());
        }

        Client client = new Client();
        BeanUtils.copyProperties(req, client);
        clientService.insertSelective(client);

        return client.getId();
    }

    @ApiOperation("商户-修改")
    @PutMapping("/{id}")
    public void update(@PathVariable String id, @ModelAttribute ClientEditReq req) {
        Client client = clientService.selectByPrimaryKey(id);
        if (client == null) {
            throw new BusinessException("商户不存在");
        }

        BeanUtils.copyProperties(req, client);
        client.setUpdateTime(LocalDateTime.now());
        clientService.updateByPrimaryKeySelective(client);
    }

    @ApiOperation("商户-删除")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        clientService.deleteByPrimaryKey(id);
    }

    @ApiOperation("商户-获取")
    @GetMapping("/{id}")
    public ClientGetRsp get(@PathVariable String id) {
        Client client = clientService.selectByPrimaryKey(id);
        if (client == null) {
            throw new BusinessException("商户不存在");
        }

        ClientGetRsp rsp = new ClientGetRsp();
        BeanUtils.copyProperties(client, rsp);
        return rsp;
    }

    @ApiOperation("商户-列表")
    @GetMapping("")
    public List<Client> list(@ModelAttribute ClientListReq req) {
        Page<Client> page = clientService.listPage(req);
        return page.getResult();
    }

    @ApiOperation("商户-保存")
    @PostMapping("save")
    public String save(@RequestBody @Valid ClientAddReq req) {
        if (req.getId() == null) {
            req.setId(uidGeneratorService.generate());
        }

        Client client = new Client();
        BeanUtils.copyProperties(req, client);
        clientService.saveClient(client);

        return client.getId();
    }
}

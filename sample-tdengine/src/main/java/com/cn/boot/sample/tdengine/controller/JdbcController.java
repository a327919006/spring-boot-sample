package com.cn.boot.sample.tdengine.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.tdengine.model.po.Pile;
import com.cn.boot.sample.tdengine.service.PileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/jdbc")
@Api(tags = "测试JDBC", produces = MediaType.APPLICATION_JSON_VALUE)
public class JdbcController {

    @Autowired
    private PileService pileService;

    @ApiOperation("insert")
    @PostMapping("")
    public RspBase<Integer> insert(@RequestBody Pile pile) {
        int result = pileService.insertPile(pile);
        return RspBase.data(result);
    }

    @ApiOperation("list")
    @GetMapping("")
    public RspBase<List<Pile>> list(@ModelAttribute Pile pile) {
        List<Pile> list = pileService.list(pile);
        return RspBase.data(list);
    }
}

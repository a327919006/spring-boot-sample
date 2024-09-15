package com.cn.boot.sample.server.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cn.boot.sample.dal.mp.model.dto.PeopleDTO;
import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.dal.mp.model.vo.PeopleVO;
import com.cn.boot.sample.server.mp.service.IPeopleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/people")
@Api(tags = "", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PeopleController {

    private final IPeopleService peopleService;

    @ApiOperation("save")
    @PostMapping("")
    public Long save(@RequestBody PeopleDTO dto) {
        People people = BeanUtil.copyProperties(dto, People.class);
        peopleService.save(people);

        return people.getId();
    }

    @ApiOperation("delete")
    @DeleteMapping("{id}")
    public boolean delete(@PathVariable String id) {
        return peopleService.removeById(id);
    }

    @ApiOperation("update")
    @PutMapping("")
    public boolean update(@RequestBody PeopleDTO dto) {
        People people = BeanUtil.copyProperties(dto, People.class);
        return peopleService.updateById(people);
    }

    @ApiOperation("updateAccount")
    @PutMapping("/account")
    public boolean updateAccount(@RequestBody PeopleDTO dto) {
        return peopleService.updateAccount(dto);
    }

    @ApiOperation("detail")
    @GetMapping("/detail/{id}")
    public PeopleVO detail(@PathVariable String id) {
        People people = peopleService.getById(id);
        return BeanUtil.copyProperties(people, PeopleVO.class);
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public List<PeopleVO> list(People people) {
        return peopleService.list(people);
    }

    // @ApiOperation("")
    // @GetMapping("page")
    // public List<People> page(IPage page, People people) {
    //     return peopleService.page(page);
    // }

    @ApiOperation("getByName")
    @GetMapping("/getByName")
    public People getByName(String name) {
        return peopleService.getByName(name);
    }


    @ApiOperation("saveBatch")
    @PostMapping("/batch")
    public boolean saveBatch(@RequestBody List<PeopleDTO> dto) {
        List<People> people = BeanUtil.copyToList(dto, People.class);
        return peopleService.saveBatch(people);
    }
}

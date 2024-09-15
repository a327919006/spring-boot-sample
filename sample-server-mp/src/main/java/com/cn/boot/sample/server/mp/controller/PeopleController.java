package com.cn.boot.sample.server.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
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
@Api(tags = "人员信息", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PeopleController {

    private final IPeopleService peopleService;

    @ApiOperation("新增")
    @PostMapping("")
    public Long save(@RequestBody PeopleDTO dto) {
        People people = BeanUtil.copyProperties(dto, People.class);
        peopleService.save(people);

        return people.getId();
    }

    @ApiOperation("删除")
    @DeleteMapping("{id}")
    public boolean delete(@PathVariable String id) {
        return peopleService.removeById(id);
    }

    @ApiOperation("修改")
    @PutMapping("")
    public boolean update(@RequestBody PeopleDTO dto) {
        People people = BeanUtil.copyProperties(dto, People.class);
        return peopleService.updateById(people);
    }

    @ApiOperation("修改账户金额")
    @PutMapping("/account")
    public boolean updateAccount(@RequestBody PeopleDTO dto) {
        return peopleService.updateAccount(dto);
    }

    @ApiOperation("获取详情")
    @GetMapping("/detail/{id}")
    public PeopleVO detail(@PathVariable String id) {
        People people = peopleService.getById(id);
        return BeanUtil.copyProperties(people, PeopleVO.class);
    }

    @ApiOperation("获取列表")
    @GetMapping("/list")
    public List<PeopleVO> list(People people) {
        return peopleService.list(people);
    }

    // @ApiOperation("")
    // @GetMapping("page")
    // public List<People> page(IPage page, People people) {
    //     return peopleService.page(page);
    // }

    @ApiOperation("获取-根据姓名")
    @GetMapping("/getByName")
    public People getByName(String name) {
        return peopleService.getByName(name);
    }

    @ApiOperation("批量新增")
    @PostMapping("/batch")
    public boolean saveBatch(@RequestBody List<PeopleDTO> dto) {
        List<People> people = BeanUtil.copyToList(dto, People.class);
        return peopleService.saveBatch(people);
    }

    @ApiOperation(value = "获取-使用静态工具DB", notes = "在发生循环依赖时可使用DB进行调用")
    @GetMapping("/util")
    public People getUseUtil(String name) {
        return Db.getOne(new People().setName(name));
    }
}

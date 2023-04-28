package com.cn.boot.sample.doris.mapper;


import com.cn.boot.sample.doris.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface UserMapper {

    int insert(@Param("list") List<User> list);

}
package com.cn.boot.sample.tdengine.mapper;

import com.cn.boot.sample.tdengine.model.dto.TableFieldVO;
import com.cn.boot.sample.tdengine.model.dto.TableInfoVO;
import com.cn.boot.sample.tdengine.model.po.Pile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface PileMapper {
    int insertPile(Pile pile);

    List<Pile> list(Pile pile);

    List<TableInfoVO> listTable();

    List<TableFieldVO> desc(@Param("tableName") String tableName);
}
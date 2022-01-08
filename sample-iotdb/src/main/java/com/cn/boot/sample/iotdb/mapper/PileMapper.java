package com.cn.boot.sample.iotdb.mapper;

import com.cn.boot.sample.iotdb.model.po.Pile;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface PileMapper {
    int insertPile(Pile pile);

    List<Pile> list(Pile pile);
}
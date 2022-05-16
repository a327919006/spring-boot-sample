package com.cn.boot.sample.tdengine.mapper;

import com.cn.boot.sample.tdengine.model.po.Pile;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface PileMapper {
    int insertPile(Pile pile);

    List<Pile> list(Pile pile);
}
package com.cn.boot.sample.iotdb.service;

import com.cn.boot.sample.iotdb.model.po.Pile;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface PileService {
    int insertPile(Pile pile);

    List<Pile> list(Pile pile);
}

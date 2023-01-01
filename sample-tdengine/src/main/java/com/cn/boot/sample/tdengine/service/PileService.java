package com.cn.boot.sample.tdengine.service;

import com.cn.boot.sample.tdengine.model.dto.TableFieldVO;
import com.cn.boot.sample.tdengine.model.dto.TableInfoVO;
import com.cn.boot.sample.tdengine.model.po.Pile;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface PileService {
    int insertPile(Pile pile);

    List<Pile> list(Pile pile);

    List<TableInfoVO> listTable();

    List<TableFieldVO> desc(String tableName);
}

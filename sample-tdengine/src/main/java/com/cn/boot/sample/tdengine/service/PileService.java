package com.cn.boot.sample.tdengine.service;

import com.cn.boot.sample.tdengine.model.dto.TableFieldDTO;
import com.cn.boot.sample.tdengine.model.dto.TableInfoDTO;
import com.cn.boot.sample.tdengine.model.po.Pile;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface PileService {
    int insertPile(Pile pile);

    List<Pile> list(Pile pile);

    List<TableInfoDTO> listTable();

    List<TableFieldDTO> desc(String tableName);
}

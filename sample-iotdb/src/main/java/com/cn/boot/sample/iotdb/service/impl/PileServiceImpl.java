package com.cn.boot.sample.iotdb.service.impl;

import com.cn.boot.sample.iotdb.mapper.PileMapper;
import com.cn.boot.sample.iotdb.model.po.Pile;
import com.cn.boot.sample.iotdb.service.PileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class PileServiceImpl implements PileService {

    @Autowired
    private PileMapper mapper;

    @Override
    public int insertPile(Pile pile) {
        pile.setTimestamp(System.currentTimeMillis());
        return mapper.insertPile(pile);
    }

    @Override
    public List<Pile> list(Pile pile) {
        return mapper.list(pile);
    }
}

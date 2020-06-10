package com.cn.boot.sample.server.jpa.dynamic.service;

import com.cn.boot.sample.api.model.po.Room;
import com.cn.boot.sample.api.service.RoomService;
import com.cn.boot.sample.server.jpa.dynamic.config.dds.DynamicDataSource;
import com.cn.boot.sample.server.jpa.dynamic.dao.RoomRepository;
import com.github.pagehelper.Page;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Chen Nan
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repository;

    @Override
    public int deleteByPrimaryKey(Long id) {
        repository.deleteById(id);
        return 1;
    }

    @Override
    public int delete(Room record) {
        repository.delete(record);
        return 1;
    }

    @Override
    public int insert(Room record) {
        repository.save(record);
        return 1;
    }

    @Override
    public int insertSelective(Room record) {
        repository.save(record);
        return 1;
    }

    @Override
    public Room selectByPrimaryKey(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public int updateByPrimaryKeySelective(Room record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Room record) {
        return 0;
    }

    @Override
    public int count(Room record) {
        return 0;
    }

    @Override
    public Room get(Room record) {
        return null;
    }

    @Override
    public List<Room> list(Room record) {
        return null;
    }

    @Override
    public List<Room> listByCondition(Object record) {
        return null;
    }

    @Override
    public Page<Room> listPage(Object record) {
        return null;
    }

    @Override
    @DynamicDataSource
    public void insertRoom(String dbKey, Room teacher) {
        repository.save(teacher);
    }
}

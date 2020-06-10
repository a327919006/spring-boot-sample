package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.po.Room;

/**
 * 用于测试jpa动态数据源
 *
 * @author Chen Nan
 */
public interface RoomService extends BaseService<Room, Long> {
    void insertRoom(String appId, Room room);
}

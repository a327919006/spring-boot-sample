package com.cn.boot.sample.zookeeper.zkclient;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class ZkClientUtil {
    private static ZkClient zkClient;
    public static final String PARENT_NODE = "/test";

//    @PostConstruct
    public void init() {
        zkClient = new ZkClient("localhost:2181", 3000);

        boolean exists = zkClient.exists(PARENT_NODE);
        if (!exists) {
            zkClient.createPersistent(PARENT_NODE);
        }

        zkClient.subscribeChildChanges(PARENT_NODE, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                log.info("【ZkClientUtil】节点变更, parentPath={}, currentChilds={}", parentPath, currentChilds);
            }
        });

        zkClient.createEphemeral(PARENT_NODE + "/" + IdUtil.objectId());
    }

    public void createEphemeral(String node) {
        String newNode = PARENT_NODE + "/" + node;
        if (!zkClient.exists(newNode)) {
            zkClient.createEphemeral(newNode);
        }
    }

}

package com.cn.boot.sample.zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class CuratorUtil {
    private static CuratorFramework curator;
    public static final String PARENT_NODE = "/test";

//    @PostConstruct
    public void init() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

        curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(3000)
                .retryPolicy(retryPolicy)
                .build();

        curator.start();

        Stat stat = curator.checkExists().forPath(PARENT_NODE);
        if (stat == null) {
            String result = curator.create().withMode(CreateMode.PERSISTENT).forPath(PARENT_NODE);
            log.info("【CuratorUtil】PARENT_NODE create={}", result);
        } else {
            log.info("【CuratorUtil】PARENT_NODE stat={}", stat);
        }

        // 第三个参数表示是否需要返回所操作的子节点数据
        PathChildrenCache cache = new PathChildrenCache(curator, PARENT_NODE, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        cache.getListenable().addListener((curatorFramework, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    log.info("【CuratorUtil】新增子节点" + event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    log.info("【CuratorUtil】更新子节点" + event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    log.info("【CuratorUtil】删除子节点" + event.getData().getPath());
                    break;
                default:
                    break;
            }
        });
    }

    public String createEphemeral(String node) throws Exception {
        return curator.create().withMode(CreateMode.EPHEMERAL).forPath(PARENT_NODE + "/" + node);
    }
}

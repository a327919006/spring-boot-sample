package com.cn.boot.sample.zookeeper.register;

import cn.hutool.core.util.StrUtil;
import com.cn.boot.sample.zookeeper.register.properties.ServerConfig;
import com.cn.boot.sample.zookeeper.register.properties.ServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class RegisterUtil {
    @Autowired
    private ServerConfig serverConfig;

    @Value("${zookeeper.uri}")
    private String zkUri;

    private static CuratorFramework curator;
    private static Set<String> nodeSet = new HashSet<>();

    @PostConstruct
    public void init() throws Exception {
        // 连接Zookeeper
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 100);

        curator = CuratorFrameworkFactory.builder()
                .connectString(zkUri)
                .sessionTimeoutMs(60000)
                .retryPolicy(retryPolicy)
                .build();

        curator.start();

        // 判断根节点是否创建
        String parentNode = serverConfig.getParentNode();
        Stat stat = curator.checkExists().forPath(parentNode);
        if (stat == null) {
            // 创建根节点
            String result = curator.create().withMode(CreateMode.PERSISTENT).forPath(parentNode);
            log.info("【RegisterUtil】PARENT_NODE create={}", result);
        }

        // 第三个参数表示是否需要返回所操作的子节点数据
        PathChildrenCache cache = new PathChildrenCache(curator, parentNode, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        cache.getListenable().addListener((curatorFramework, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    // 监听节点上线
                    nodeOnline(event.getData().getPath(), event.getData().getData());
                    break;
                case CHILD_REMOVED:
                    // 监听节点下线
                    nodeOffline(event.getData().getPath(), event.getData().getData());
                    break;
                default:
                    break;
            }
        });

        reportNode(parentNode);
    }

    /**
     * 上报-本节点上线
     *
     * @param parentNode 根节点路径
     * @throws Exception 上报节点异常
     */
    private void reportNode(String parentNode) throws Exception {
        String nodeName = serverConfig.getNodeName();
        if (StrUtil.isNotBlank(nodeName) && serverConfig.getServerPort() != null && serverConfig.getServers() != null) {
            ServerInfo serverInfo = serverConfig.getServers().get(nodeName);
            if (serverInfo != null) {
                String nodeData = serverInfo.getServerUri() + ":" + serverConfig.getServerPort();
                curator.create().withMode(CreateMode.EPHEMERAL).forPath(parentNode + "/" + nodeName, nodeData.getBytes());
            }
        }
    }

    /**
     * 获取在线节点列表
     * @return 在线节点列表
     */
    public Set<String> getOnlineNodeSet() {
        return nodeSet;
    }

    /**
     * 节点上线
     *
     * @param path 节点路径
     * @param data 节点数据
     */
    private void nodeOnline(String path, byte[] data) {
        String node = pathToNode(path);
        log.info("【RegisterUtil】节点上线,node={},data={}", node, new String(data));
        nodeSet.add(node);
    }

    /**
     * 节点下线
     *
     * @param path 节点路径
     * @param data 节点数据
     */
    private void nodeOffline(String path, byte[] data) {
        String node = pathToNode(path);
        log.info("【RegisterUtil】节点下线,node={},data={}", node, new String(data));
        nodeSet.remove(node);
    }

    /**
     * path转节点名称
     *
     * @param path 节点路径
     * @return 节点名称
     */
    private String pathToNode(String path) {
        return StrUtil.removeAll(path, serverConfig.getParentNode() + "/");
    }
}

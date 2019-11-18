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

        // 根节点路径
        String parentNode = serverConfig.getParentNode();

        // 注册节点监听
        addListener(parentNode);

        // 服务注册
        reportNode(parentNode);
    }

    /**
     * 注册节点监听
     *
     * @param parentNode 根节点路径
     * @throws Exception 注册监听异常
     */
    private void addListener(String parentNode) throws Exception {
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
    }

    /**
     * 上报-本节点上线
     *
     * @param parentNode 根节点路径
     * @throws Exception 上报节点异常
     */
    private void reportNode(String parentNode) throws Exception {
        // 判断本机节点是否创建
        String currNode = parentNode + "/" + serverConfig.getNodeName();
        Stat currNodeStat = curator.checkExists().forPath(currNode);
        if (currNodeStat != null) {
            curator.delete().forPath(currNode);
            log.info("【RegisterUtil】CURR_NODE exist, delete={}", currNode);
        }

        String nodeName = serverConfig.getNodeName();
        if (StrUtil.isNotBlank(nodeName) && serverConfig.getServerPort() != null && serverConfig.getServers() != null) {
            ServerInfo serverInfo = serverConfig.getServers().get(nodeName);
            if (serverInfo != null) {
                String nodeData = serverInfo.getServerUri() + ":" + serverConfig.getServerPort();
                // 父节点不存在时自动创建
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(parentNode + "/" + nodeName, nodeData.getBytes());
            }
        }
    }

    /**
     * 获取在线节点列表
     *
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

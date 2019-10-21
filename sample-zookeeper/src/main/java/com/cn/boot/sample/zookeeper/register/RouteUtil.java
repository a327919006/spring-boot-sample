package com.cn.boot.sample.zookeeper.register;

import cn.hutool.core.util.StrUtil;
import com.cn.boot.sample.zookeeper.register.model.RouteInfoDTO;
import com.cn.boot.sample.zookeeper.register.model.ServerWeight;
import com.cn.boot.sample.zookeeper.register.monitor.ConnectMonitor;
import com.cn.boot.sample.zookeeper.register.monitor.CpuMonitor;
import com.cn.boot.sample.zookeeper.register.properties.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 长连接路由
 * 路由规则：节点在线、节点连接数未达到上限、节点标识优先、空闲服务器优先；即当节点正常的情况下，相同appId和区服的客户端路由到相同服务器，避免跨服通信
 * 1)	给每台服务器节点打上标识（无标识则为通用节点）
 * 2)	配置所有节点标识、权重、性能参数（CPU上限、连接数上限）；
 * 3)	减权重规则（如cpu 每使用10% 权重减1，连接数同等）；
 * 4)	客户端调用路由接口时，请求参数中标识APPID、区服；
 * 5)	遍历配置中的节点列表，根据节点状态增减其权重：
 * 6)	将节点列表按权重排序后返回给客户端
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class RouteUtil {

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private RegisterUtil registerUtil;
    @Autowired
    private CpuMonitor cpuMonitor;
    @Autowired
    private ConnectMonitor connectMonitor;

    /**
     * 路由
     *
     * @param appIdTag  appId标识
     * @param serverTag 区服标识，如游戏中开了100服，相同服的用户路由到同一台服务器节点
     * @return 服务器节点列表
     */
    public RouteInfoDTO route(String appIdTag, String serverTag) {
        // 获取配置中的节点列表
        Map<String, ServerInfo> servers = serverConfig.getServers();
        if (servers == null) {
            return null;
        }

        // 获取在线节点列表
        Set<String> onlineNodeSet = registerUtil.getOnlineNodeSet();
        if (onlineNodeSet.size() <= 0) {
            // 在线节点列表为空
            return null;
        }

        // 遍历配置中的节点列表
        Set<String> nodeNameSet = servers.keySet();
        List<ServerWeight> serverWeightList = new ArrayList<>();
        for (String nodeName : nodeNameSet) {
            // 获取节点配置和当前负载情况
            ServerInfo serverInfo = servers.get(nodeName);
            double cpuRate = cpuMonitor.getCpuRate(nodeName);
            int connectCount = connectMonitor.getConnectCount(nodeName);

            // 判断节点是否正常
            if (!judgeNodeNormal(nodeName, onlineNodeSet, serverInfo, cpuRate, connectCount)) {
                continue;
            }

            // 计算节点权重
            int nodeWeight = getNodeWeight(appIdTag, serverTag, serverInfo, cpuRate, connectCount);
            String serverUri = serverInfo.getServerUri();
            serverWeightList.add(new ServerWeight(serverUri, nodeWeight));
        }

        // 按权重排序后返回
        return sortServerWeight(serverWeightList);
    }

    /**
     * 判断节点是否正常
     *
     * @param nodeName      节点名称
     * @param onlineNodeSet 在线节点列表
     * @param serverInfo    节点配置
     * @param cpuRate       节点当前CPU使用率
     * @param connectCount  节点当前连接数
     * @return 是否正常 true：正常，false：不正常
     */
    private boolean judgeNodeNormal(String nodeName, Set<String> onlineNodeSet,
                                    ServerInfo serverInfo, double cpuRate, int connectCount) {
        // 判断节点是否在线
        if (!onlineNodeSet.contains(nodeName)) {
            return false;
        }

        // 判断节点CPU使用率是否大于阈值
        if (cpuRate >= serverInfo.getMaxCpu()) {
            return false;
        }

        // 判断节点连接数是否大于阈值
        if (connectCount >= serverInfo.getMaxConnect()) {
            return false;
        }
        return true;
    }

    /**
     * 获取节点权重
     *
     * @param appIdTag     客户端app标识
     * @param areaTag      客户端区服标识
     * @param serverInfo   节点信息
     * @param cpuRate      节点当前CPU使用率
     * @param connectCount 节点当前连接数
     * @return 节点权重
     */
    private int getNodeWeight(String appIdTag, String areaTag, ServerInfo serverInfo, double cpuRate, int connectCount) {
        int weight = 0;

        // 根据tag增加权重
        List<AppTagInfo> appTags = serverInfo.getAppTags();
        weight += judgeTag(appTags, appIdTag, areaTag);

        // 根据CPU负载修改权重
        weight += judgeLoad(cpuRate, serverConfig.getLoadRule().getCpu());

        // 根据连接数负载修改权重
        weight += judgeLoad(connectCount, serverConfig.getLoadRule().getConnect());

        return weight;
    }

    /**
     * 判断tag是否相同
     *
     * @param appTags  app标识列表
     * @param appIdTag 客户端app标识
     * @param areaTag  客户端区服标识
     * @return 权重
     */
    private int judgeTag(List<AppTagInfo> appTags, String appIdTag, String areaTag) {
        int weight = 0;
        if (appTags != null && StrUtil.isNotBlank(appIdTag)) {
            for (AppTagInfo appTag : appTags) {
                if (StringUtils.equalsAnyIgnoreCase(appIdTag, appTag.getName())) {
                    // app标识相同
                    weight = appTag.getWeight();

                    // 遍历区服标识
                    List<AreaTagInfo> areaTags = appTag.getAreaTags();
                    if (areaTags != null && StrUtil.isNotBlank(areaTag)) {
                        for (AreaTagInfo areaTagInfo : areaTags) {
                            if (StringUtils.equalsAnyIgnoreCase(areaTag, areaTagInfo.getName())) {
                                // 区服标识相同
                                weight += areaTagInfo.getWeight();
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return weight;
    }

    /**
     * 计算减权重值
     *
     * @param load     当前负载情况
     * @param loadRule 负载规则
     * @return 减权重值，返回的值为负数
     */
    private int judgeLoad(double load, LoadRuleInfo loadRule) {
        int weight = (int) (load / loadRule.getStep()) * loadRule.getWeight();
        log.debug("【RouteUtil】cpuRate={},loadRule={}, result={}, weight={}",
                load, loadRule.getStep(), (int) (load / loadRule.getStep()), weight);
        return weight;
    }

    /**
     * 按权重排序节点
     *
     * @param serverWeightList 节点权重列表
     */
    private RouteInfoDTO sortServerWeight(List<ServerWeight> serverWeightList) {
        Collections.sort(serverWeightList);
        log.info("【RouteUtil】serverWeightList={}", serverWeightList);
        String[] servers = new String[serverWeightList.size()];
        for (int i = 0; i < serverWeightList.size(); i++) {
            servers[i] = serverWeightList.get(i).getServerUri();
        }

        RouteInfoDTO routeInfoDTO = new RouteInfoDTO();
        routeInfoDTO.setServerList(StringUtils.join(servers, ","));
        routeInfoDTO.setServerPort(serverConfig.getServerPort());
        return routeInfoDTO;
    }
}

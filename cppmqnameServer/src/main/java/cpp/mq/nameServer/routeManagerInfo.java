package cpp.mq.nameServer;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class routeManagerInfo {
    private ConcurrentMap<String/**topic**/,BrokerLiveInfo> routeMap;
    private ConcurrentMap<String/**address**/,BrokerLiveInfo> brokerLiveInfoMap;

    public ConcurrentMap<String, BrokerLiveInfo> getRouteMap() {
        return routeMap;
    }

    public void setRouteMap(ConcurrentMap<String, BrokerLiveInfo> routeMap) {
        this.routeMap = routeMap;
    }

    public ConcurrentMap<String, BrokerLiveInfo> getBrokerLiveInfoMap() {
        return brokerLiveInfoMap;
    }

    public void setBrokerLiveInfoMap(ConcurrentMap<String, BrokerLiveInfo> brokerLiveInfoMap) {
        this.brokerLiveInfoMap = brokerLiveInfoMap;
    }

    public void scanExpired(){

    }

    public synchronized void registerBrokerLiveInfo(String brokerName,String topic,String ip,int port,int timeSatmp){
        BrokerLiveInfo brokerLiveInfo = this.brokerLiveInfoMap.get(ip);
        if(brokerLiveInfo == null){
            brokerLiveInfo = new BrokerLiveInfo();
            brokerLiveInfo.setName(brokerName);
            brokerLiveInfo.setIp(ip);
            brokerLiveInfo.setTopic(topic);
            brokerLiveInfo.setPort(port);
            brokerLiveInfo.setLastLiveStamp(timeSatmp);
            this.brokerLiveInfoMap.putIfAbsent(ip,brokerLiveInfo);
            return;
        }

        int oldTimeStamp = brokerLiveInfo.getLastLiveStamp();
        if(oldTimeStamp > timeSatmp){
            return;
        }
        brokerLiveInfo.setLastLiveStamp(timeSatmp);

    }

}

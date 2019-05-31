package cpp.mq.nameServer;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class BrokerLiveInfo {
    private String name;
    private String ip;
    private Integer port;
    private String topic;
    private Integer lastLiveStamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getLastLiveStamp() {
        return lastLiveStamp;
    }

    public void setLastLiveStamp(Integer lastLiveStamp) {
        this.lastLiveStamp = lastLiveStamp;
    }

    @Override
    public String toString() {
        return "BrokerLiveInfo{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", topic='" + topic + '\'' +
                ", lastLiveStamp=" + lastLiveStamp +
                '}';
    }
}

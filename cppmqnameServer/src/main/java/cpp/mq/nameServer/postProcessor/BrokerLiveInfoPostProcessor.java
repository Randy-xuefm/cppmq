package cpp.mq.nameServer.postProcessor;

import cpp.mq.nameServer.NameServerController;
import cpp.mq.nameServer.remoting.RemotingCmdPostProcessor;
import cpp.mq.nameServer.remoting.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fenming.xue on 2019/5/17.
 */
public class BrokerLiveInfoPostProcessor implements RemotingCmdPostProcessor {
    private NameServerController nameServerController;

    public BrokerLiveInfoPostProcessor(NameServerController nameServerController){
        this.nameServerController = nameServerController;
    }
    @Override
    public void process(ChannelHandlerContext ctx, RemotingCommand cmd) {
        byte[] body = cmd.getBody();

        this.nameServerController.getRouteManagerInfo().registerBrokerLiveInfo();
    }
}

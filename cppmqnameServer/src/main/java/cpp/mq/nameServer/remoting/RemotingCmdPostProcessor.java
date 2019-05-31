package cpp.mq.nameServer.remoting;


import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fenming.xue on 2019/5/17.
 */
public interface RemotingCmdPostProcessor {
    void process(ChannelHandlerContext ctx, RemotingCommand cmd);
}

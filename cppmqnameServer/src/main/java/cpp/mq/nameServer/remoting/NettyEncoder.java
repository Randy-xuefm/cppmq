package cpp.mq.nameServer.remoting;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class NettyEncoder extends MessageToByteEncoder<RemotingCommand> {

    protected void encode(ChannelHandlerContext ctx, RemotingCommand msg, ByteBuf out) throws Exception {
        if(null == msg){
            ctx.close();
            return;
        }
        byte[] body = msg.encode();
        if(body.length <= 0){
            ctx.close();
            return;
        }
        out.writeInt(body.length);
        out.writeBytes(body);
    }
}

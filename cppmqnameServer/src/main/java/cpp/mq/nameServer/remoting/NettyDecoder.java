package cpp.mq.nameServer.remoting;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class NettyDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() <= 4){
            return;
        }

        int length = in.readInt();
        in.markReaderIndex();
        if(length <= 0){
            ctx.close();
            return;
        }
        if(in.readableBytes() <length){
            in.resetReaderIndex();
            return;
        }
        byte[] body = new byte[length];
        in.readBytes(body);

        out.add(RemotingCommand.decode(body));
    }
}

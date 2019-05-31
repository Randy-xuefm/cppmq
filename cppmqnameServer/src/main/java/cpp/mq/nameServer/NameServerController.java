package cpp.mq.nameServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import cpp.mq.nameServer.postProcessor.BrokerLiveInfoPostProcessor;
import cpp.mq.nameServer.remoting.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class NameServerController implements RegisterRemotingCmdPostProcessor {
    private ServerBootstrap serverBootstrap;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup wrokerGroup;
    private Config config;
    private ScheduledExecutorService executorService;
    private routeManagerInfo routeManagerInfo;
    private Map<Integer, RemotingCmdPostProcessor> remotingCmdPostProcessorMap = new HashMap<>();

    public NameServerController(Config config){
        this.config = config;
    }

    public cpp.mq.nameServer.routeManagerInfo getRouteManagerInfo() {
        return routeManagerInfo;
    }

    public void setRouteManagerInfo(cpp.mq.nameServer.routeManagerInfo routeManagerInfo) {
        this.routeManagerInfo = routeManagerInfo;
    }

    private void init() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1, r -> new Thread(r, "Name Server boss thread"));
        this.wrokerGroup = new NioEventLoopGroup(8, new ThreadFactory() {
            AtomicInteger num = new AtomicInteger();
            public Thread newThread(Runnable r) {
                return new Thread(r,"Name Server wroker thread num:" + num.incrementAndGet());
            }
        });
        this.serverBootstrap.group(this.bossGroup,this.wrokerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .option(ChannelOption.SO_REUSEADDR, true)
                            .option(ChannelOption.SO_KEEPALIVE, false)
                            .childOption(ChannelOption.TCP_NODELAY, true)
                            .childOption(ChannelOption.SO_SNDBUF, 64*1024)
                            .childOption(ChannelOption.SO_RCVBUF, 64*1024)
                            .localAddress(new InetSocketAddress(config.getPort()))
                            .childHandler(new ChannelInitializer<SocketChannel>(){

                                protected void initChannel(SocketChannel ch) {
                                    ch.pipeline()
                                            .addLast(new NettyEncoder())
                                            .addLast(new NettyDecoder())
                                            .addLast(new NettyServerHandle());
                                }
                            });
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.routeManagerInfo = new routeManagerInfo();

        registerRemotingCmdPostProcessor(RequestCode.BrokerLiveInfo, new BrokerLiveInfoPostProcessor(this));
//        registerRemotingCmdPostProcessor(,);
    }

    public void start() throws InterruptedException {
        init();

        this.serverBootstrap.bind().sync();

        this.executorService.scheduleAtFixedRate(() -> routeManagerInfo.scanExpired(), 5, 10, TimeUnit.SECONDS);

        this.executorService.scheduleAtFixedRate(() -> {
            ConcurrentMap<String,BrokerLiveInfo> brokerLiveInfoMap = routeManagerInfo.getBrokerLiveInfoMap();
            if(brokerLiveInfoMap.isEmpty()){
                return;
            }
            brokerLiveInfoMap.values().forEach(brokerLiveInfo -> System.out.println(brokerLiveInfo.toString()));
        }, 1, 5, TimeUnit.MINUTES);
    }

    public void shutDown(){
        this.bossGroup.shutdownGracefully();
        this.wrokerGroup.shutdownGracefully();
        this.executorService.shutdown();
    }

    private void handleRemotingCmd(ChannelHandlerContext ctx, RemotingCommand cmd){
        String code = cmd.getCode();
        RemotingCmdPostProcessor processor = this.remotingCmdPostProcessorMap.get(code);
        if(processor == null){
            System.out.println(String.format("code:%s,find no remotingCmdPostProcessor!",code));
            return;
        }

        processor.process(ctx,cmd);
    }

    @Override
    public void registerRemotingCmdPostProcessor(Integer code,RemotingCmdPostProcessor postProcessor) {
        this.remotingCmdPostProcessorMap.put(code,postProcessor);
    }

    class NettyServerHandle extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            RemotingCommand cmd = (RemotingCommand) msg;
            handleRemotingCmd(ctx,cmd);
        }
    }
}

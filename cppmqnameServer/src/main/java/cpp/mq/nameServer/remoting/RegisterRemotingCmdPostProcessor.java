package cpp.mq.nameServer.remoting;

/**
 * Created by fenming.xue on 2019/5/17.
 */
public interface RegisterRemotingCmdPostProcessor {
    void registerRemotingCmdPostProcessor(Integer code,RemotingCmdPostProcessor postProcessor);
}

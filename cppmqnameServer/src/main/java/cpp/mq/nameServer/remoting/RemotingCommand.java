package cpp.mq.nameServer.remoting;

import java.io.UnsupportedEncodingException;
import com.alibaba.fastjson.JSON;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class RemotingCommand {
    public static final String CHARSET="utf-8";

    private String code;
    private byte[] body;
    private int ackFlag;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getAckFlag() {
        return ackFlag;
    }

    public void setAckFlag(int ackFlag) {
        this.ackFlag = ackFlag;
    }

    public static RemotingCommand decode(byte[] bytes){
        RemotingCommand cmd = null;
        try {
            final String json = new String(bytes, RemotingCommand.CHARSET);
            cmd = JSON.parseObject(json, RemotingCommand.class);
        } catch (UnsupportedEncodingException e) {
            System.out.println("RemotingCommand decode error!");
            e.printStackTrace();
        }
        return cmd;
    }

    public byte[] encode(){
        byte[] body = null;
        try {
            body = JSON.toJSONString(this, false).getBytes(RemotingCommand.CHARSET);
        } catch (UnsupportedEncodingException e) {
            System.out.println("object to json exception!");
            System.out.println(this.toString());
            e.printStackTrace();
        }

        return body;
    }

}

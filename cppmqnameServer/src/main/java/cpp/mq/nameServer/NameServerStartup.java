package cpp.mq.nameServer;

/**
 * Created by fenming.xue on 2019/5/16.
 */
public class NameServerStartup {

    public static void main(String[] args) throws InterruptedException {


        NameServerController controller = new NameServerController();

        controller.start();


    }
}

import com.itdotaer.netty.rpc.common.service.HelloService;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

import java.util.List;

/**
 * ZkClientTest
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class ZkClientTest {

    @Test
    public void zkClientTest() {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);

        zkClient.createPersistent("/netty-rpc", true);
        zkClient.createPersistent("/netty-rpc/" + HelloService.class.getName(), true);
        zkClient.createPersistent("/netty-rpc/" + HelloService.class.getName() +"/providers", true);
        zkClient.createEphemeral("/netty-rpc/" + HelloService.class.getName() + "/providers" + "/127.0.0.1:8080", "");

        zkClient.createPersistent("/netty-rpc/" + HelloService.class.getName() +"/consumers", true);
        zkClient.createEphemeral("/netty-rpc/" + HelloService.class.getName() + "/consumers" + "/127.0.0.1:8080", "");

        List<String> list = zkClient.getChildren("/netty-rpc");

    }

}

import com.itdotaer.netty.rpc.client.RpcClient;
import com.itdotaer.netty.rpc.client.RpcClientHandler;
import com.itdotaer.netty.rpc.client.RpcRequestProxy;
import com.itdotaer.netty.rpc.common.service.HelloService;

import java.lang.reflect.Proxy;

/**
 * Main
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        RpcClient rpcClient = new RpcClient();

        rpcClient.start("127.0.0.1", 8080);

        RpcClientHandler rpcClientHandler = rpcClient.chooseHandler();

        HelloService helloService = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                new Class<?>[]{HelloService.class},
                new RpcRequestProxy(HelloService.class, rpcClientHandler));

        String result = helloService.sayHello("abc");

        System.out.println(result);

        rpcClient.stop();
    }

}

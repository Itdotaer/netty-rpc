import com.itdotaer.netty.rpc.server.RpcServer;

/**
 * Main
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class Main {

    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();

        rpcServer.start("", 8080);
    }

}

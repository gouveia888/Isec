import config.ServerConfig;
import network.Server;
import network.ServerSetup;
import util.FancyLog;

public class Main {
    private static Server server = null;

    public static void main(String[] args) {
        // Shutdown the server on a CTRL+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(server != null){
                //server.signalShutdown();
                server.trueShutdown();
            }
        }));

        // Run the server
        try {
            ServerConfig config = ServerConfig.fromArgs(args);
            ServerSetup setup = new ServerSetup(config);

            server = setup.createServer();
            if(server == null){
                throw new Exception("Couldn't setup the server.");
            }
            server.start();
        } catch (Exception e) {
            FancyLog.println("Fatal error during startup: " + e.getMessage(), FancyLog.Status.FAILED);
            //e.printStackTrace();
        }
    }
}

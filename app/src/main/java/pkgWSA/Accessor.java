package pkgWSA;

/**
 * @author Marco Wilscher
 */

public final class Accessor {
    private static String serverUrl = "http://{ip}:{port}/team02/services/", serverIp, serverPort;
    private static boolean initialized = false;

    public static void init(String ip, String port) {
        serverIp = ip;
        serverPort = port;
        initialized = true;
    }
    private static String generateUrl() {
        return serverUrl.replace("{ip}", serverIp).replace("{port}", serverPort);
    }
    public static void runRequestAsync(HttpMethod httpMethod, String uriPath, String uriQuery, String requetsBody, WebRequestTaskListener listener) throws Exception {
        if (!initialized) {
            throw new Exception("Accessor not initialized");
        }
        RequestParameter parameter = new RequestParameter(generateUrl(), httpMethod, uriPath, uriQuery, requetsBody, true, listener);
        WebRequestTask requestTask = new WebRequestTask();
        requestTask.execute(parameter);
    }
    public static AccessorResponse runRequestSync(HttpMethod httpMethod, String uriPath, String uriQuery, String requetsBody) throws Exception {
        if (!initialized) {
            throw new Exception("Accessor not initialized");
        }
        RequestParameter parameter = new RequestParameter(generateUrl(), httpMethod, uriPath, uriQuery, requetsBody);
        WebRequestTask requestTask = new WebRequestTask();
        requestTask.execute(parameter);
        return requestTask.get();
    }
    public static String getServerUrl () {
        return generateUrl();
    }
}
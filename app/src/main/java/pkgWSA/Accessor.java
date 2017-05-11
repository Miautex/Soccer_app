package pkgWSA;

/**
 * @author Marco Wilscher
 */

public final class Accessor {
    private static String serverUrl = "http://192.168.196.82:51246/team02/services/";

    public static void runRequestAsync(HttpMethod httpMethod, String uriPath, String uriQuery, String requetsBody, WebRequestTaskListener listener) throws Exception {
        RequestParameter parameter = new RequestParameter(serverUrl, httpMethod, uriPath, uriQuery, requetsBody, true, listener);
        WebRequestTask requestTask = new WebRequestTask();
        requestTask.execute(parameter);
    }

    public static AccessorResponse runRequestSync(HttpMethod httpMethod, String uriPath, String uriQuery, String requetsBody) throws Exception {
        RequestParameter parameter = new RequestParameter(serverUrl, httpMethod, uriPath, uriQuery, requetsBody);
        WebRequestTask requestTask = new WebRequestTask();
        requestTask.execute(parameter);
        return requestTask.get();
    }

    public static void setServerUrl (String serverUrl) {
        Accessor.serverUrl = serverUrl;
    }

    public static String getServerUrl () {
        return serverUrl;
    }
}
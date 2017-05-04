package pkgWSA;

/**
 * Created by Wilscher Marco
 */

public final class Accessor {
    private static String serverUrl = "http://{ip}:8080/team02/services/";//"http://192.168.194.27:8080/team02/services/";

    public static AccessorResponse requestJSON(HttpMethod method, String servicePath, String serviceQuery, String body) throws Exception {
        String url = serverUrl.replace("{ip}", java.net.InetAddress.getByName("MWPC").getHostAddress());

        AccessorResponse response;
        WebServiceTask task = new WebServiceTask();

        task.execute(new TaskParams(url, method, servicePath, serviceQuery, body));
        response = task.get();
        if (response.getException() != null) {
            throw response.getException();
        }
        return response;
    }
}

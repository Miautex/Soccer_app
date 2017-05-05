package pkgWSA;

/**
 * Created by Wilscher Marco
 */

public final class Accessor {
    private static String serverUrl = "http://192.168.196.2:25120/team02/services/";//"http://192.168.194.27:8080/team02/services/";

    public static AccessorResponse requestJSON(HttpMethod method, String servicePath, String serviceQuery, String body) throws Exception {       
        AccessorResponse response;
        WebServiceTask task = new WebServiceTask();

        task.execute(new TaskParams(serverUrl, method, servicePath, serviceQuery, body));
        response = task.get();
        if (response.getException() != null) {
            throw response.getException();
        }
        return response;
    }
}

package pkgWSA;

/**
 * Created by Wilscher Marco
 */

final class TaskParams {
    private HttpMethod method;
    private String serverUrl, servicePath, serviceQuery, body;

    protected TaskParams(String serverUrl, HttpMethod method, String servicePath, String serviceQuery, String body) {
        setServerUrl(serverUrl);
        setMethod(method);
        setServicePath(servicePath);
        setServiceQuery(serviceQuery);
        setBody(body);
    }

    private void setServerUrl (String serverUrl) {
        this.serverUrl = serverUrl;
    }
    protected String getServerUrl () {
        return serverUrl;
    }

    private void setMethod (HttpMethod method) {
        this.method = method;
    }
    protected HttpMethod getMethod () {
        return method;
    }

    private void setServicePath (String servicePath) {
        this.servicePath = servicePath;
    }
    protected String getServicePath () {
        return servicePath;
    }

    private void setBody (String body) {
        this.body = body;
    }
    protected String getBody () {
        return body;
    }

    private void setServiceQuery (String serviceQuery) {
        this.serviceQuery = serviceQuery;
    }
    protected String getServiceQuery () {
        return serviceQuery;
    }
}

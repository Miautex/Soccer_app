package pkgWSA;

/**
 * @author Wilscher Marco
 */

final class RequestParameter {
    private HttpMethod httpMethod;
    private String uri, uriPath, uriQuery, requestBody;
    private boolean async;
    private int timeout;
    private WebRequestTaskListener listener;

    public RequestParameter (String uri, HttpMethod method, String path, String query, String body) {
        super();
        setUri(uri);
        setHttpMethod(method);
        setUriPath(path);
        setUriQuery(query);
        setRequestBody(body);
        setAsync(false);
        setListener(null);
        timeout = 1000;
    }

    public RequestParameter (String uri, HttpMethod method, String path, String query, String body, boolean async, WebRequestTaskListener listener) {
        this(uri, method, path, query, body);
        setAsync(async);
        setListener(listener);
    }

    public void setUri (String uri) {
        this.uri = uri;
    }

    public String getUri () {
        return uri;
    }

    public void setHttpMethod (HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HttpMethod getHttpMethod () {
        return httpMethod;
    }

    public void setUriPath (String uriPath) {
        this.uriPath = uriPath;
    }

    public String getUriPath () {
        return uriPath;
    }

    public void setRequestBody (String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestBody () {
        return requestBody;
    }

    public void setUriQuery (String uriQuery) {
        this.uriQuery = uriQuery;
    }

    public String getUriQuery () {
        return uriQuery;
    }

    public void setAsync (boolean async) {
        this.async = async;
    }

    public boolean isAsync () {
        return async;
    }

    public void setListener (WebRequestTaskListener listener) {
        this.listener = listener;
    }

    public void setTimeout (int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout () {
        return timeout;
    }

    public WebRequestTaskListener getListener () {
        return listener;
    }
}

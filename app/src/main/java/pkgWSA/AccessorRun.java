package pkgWSA;
import static pkgWSA.Accessor.requestJSON;

final class AccessorRun implements Runnable{
    private HttpMethod method;
    private String servicePath, serviceQuery, body;
    private AccessorRunListener listener;

    public AccessorRun (HttpMethod method, String servicePath, String serviceQuery, String body, AccessorRunListener listener) {
        this.method = method;
        this.servicePath = servicePath;
        this.serviceQuery = serviceQuery;
        this.body = body;
        this.listener = listener;
    }

    @Override
    public void run () {
        try {
            listener.done(requestJSON(method, servicePath, serviceQuery, body));
        } catch (Exception e) {
            listener.failed(e);
        }
    }
}

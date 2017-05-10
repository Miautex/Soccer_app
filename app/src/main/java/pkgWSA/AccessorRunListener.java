package pkgWSA;

/**
 * Created by Wilscher Marco
 */

public interface AccessorRunListener {
    void done(AccessorResponse response);
    void failed(Exception ex);
}

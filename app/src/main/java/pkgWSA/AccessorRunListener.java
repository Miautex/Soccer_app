package pkgWSA;

/**
 * Created by Wilscher Marco
 */

interface AccessorRunListener {
    void done(AccessorResponse response);
    void failed(Exception ex);
}

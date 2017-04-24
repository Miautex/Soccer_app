package pkgWSA;

/**
 * Created by Wilscher Marco
 */

public final class AccessorResponse {
    private int code = -1;
    private String json = null;

    public AccessorResponse(int responseCode, String responseData) {
        code = responseCode;
        json = responseData;
    }

    public int getResponseCode () {
        return code;
    }

    public String getJson () {
        return json;
    }
}

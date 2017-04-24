package pkgWSA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wilscher Marco
 */

public final class Accessor {
    private static String serverUrl = "http://192.168.194.27:8080/team02/services/";

    public static AccessorResponse requestJSON(HttpMethod method, String servicePath, String serviceQuery) throws Exception {
        URL url = null;
        HttpURLConnection con = null;
        int responseCode = 0;
        BufferedReader reader = null;
        String inputLine = null;
        StringBuffer jsonStringBuffer = null;
        String strUrl = "";

        try {
            strUrl += serverUrl;
            if (servicePath != null) {
                strUrl += servicePath;
            }
            if (serviceQuery != null) {
                if (!serviceQuery.isEmpty()) {
                    strUrl += ("?" + serviceQuery);
                }
            }

            url = new URL(strUrl);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod(method.toString());
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json; charset=UTF-8");

            responseCode = con.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            jsonStringBuffer = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                jsonStringBuffer.append(inputLine);
            }
        }
        catch (Exception exc) {
            throw exc;
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return new AccessorResponse(responseCode, jsonStringBuffer.toString());
    }

}

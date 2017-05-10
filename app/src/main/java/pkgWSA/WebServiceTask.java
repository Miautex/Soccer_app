package pkgWSA;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wilscher Marco
 */

final class WebServiceTask extends AsyncTask<TaskParams, Void, AccessorResponse> {

    @Override
    protected AccessorResponse doInBackground (TaskParams... taskparam) {
        TaskParams params = taskparam[0];

        URL url;
        HttpURLConnection con = null;
        int responseCode = 0;
        BufferedReader reader = null;
        String inputLine;
        StringBuffer jsonStringBuffer;
        String strUrl = "";
        Exception exception = null;
        AccessorResponse response = new AccessorResponse();

        if (params != null) {
            try {
                strUrl += params.getServerUrl();
                if (params.getServicePath() != null) {
                    strUrl += params.getServicePath();
                }
                if (params.getServiceQuery() != null) {
                    if (!params.getServiceQuery().isEmpty()) {
                        strUrl += ("?" + params.getServiceQuery());
                    }
                }
                url = new URL(strUrl);
                con = (HttpURLConnection) url.openConnection();

                con.setConnectTimeout(5000);
                con.setRequestMethod(params.getMethod().toString());
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json; charset=UTF-8");

                if (params.getBody() != null) {
                    con.setRequestProperty("Content-Length", Integer.toString(params.getBody().length()));
                    con.getOutputStream().write(params.getBody().getBytes("UTF8"));
                }

                response.setCode(con.getResponseCode());
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                jsonStringBuffer = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    jsonStringBuffer.append(inputLine);
                }
                response.setJson(jsonStringBuffer.toString());
            }
            catch (Exception ex) {
                response.setException(ex);
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        response.setException(ex);
                    }
                }
                if (con != null) {
                    con.disconnect();
                }
            }
        }
        else {
            response.setException(new Exception("params is null"));
        }
        return response;
    }
}

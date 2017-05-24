package pkgWSA;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Marco Wilscher
 */

public final class WebRequestTask extends AsyncTask <RequestParameter, Void, AccessorResponse> {

    private static final int TIMEOUT = 5000;

    private WebRequestTaskListener listener = null;

    @Override
    protected AccessorResponse doInBackground (RequestParameter... params) {
        RequestParameter parameter;
        AccessorResponse response = new AccessorResponse();

        URL url;
        HttpURLConnection con = null;
        StringBuilder fullUri, responseContent;
        BufferedReader reader = null;
        String inputLine = "";

        try {
            if (params != null) {
                if (params[0] != null) {
                    parameter = params[0];
                    fullUri = new StringBuilder();
                    if (parameter.getUri() != null && !parameter.getUri().isEmpty()) {
                        listener = parameter.getListener();
                        fullUri.append(parameter.getUri());
                        if (parameter.getUriPath() != null) {
                            //TODO check for '/'
                            fullUri.append(parameter.getUriPath());
                        }

                        if (parameter.getUriQuery() != null && !parameter.getUriQuery().isEmpty()) {
                            fullUri.append("?");
                            fullUri.append(parameter.getUriQuery());
                        }

                        url = new URL(fullUri.toString());
                        con = (HttpURLConnection) url.openConnection();

                        con.setConnectTimeout(TIMEOUT);
                        con.setRequestMethod(parameter.getHttpMethod().toString());
                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Accept", "application/json; charset=UTF-8");
                        con.setRequestProperty("Charset", "UTF-8");

                        if (parameter.getRequestBody() != null && !parameter.getRequestBody().isEmpty()) {
                            con.setRequestProperty("Content-Length", Integer.toString(parameter.getRequestBody().getBytes("UTF-8").length));
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                            writer.write(parameter.getRequestBody());
                            writer.flush();
                            writer.close();
                        }
                        response.setCode(con.getResponseCode());
                        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        responseContent = new StringBuilder();
                        while ((inputLine = reader.readLine()) != null) {
                            responseContent.append(inputLine);
                        }
                        response.setJson(responseContent.toString());
                    }
                    else {
                        response.setException(new Exception("uri required"));
                    }
                }
                else {
                    response.setException(new Exception("params[0] is null"));
                }
            }
            else {
                response.setException(new Exception("params is null"));
            }
        }
        catch (Exception ex) {
            response.setException(ex);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                response.setException(ex);
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute (AccessorResponse accessorResponse) {
        if (listener != null) {
            listener.done(accessorResponse);
        }
    }
}

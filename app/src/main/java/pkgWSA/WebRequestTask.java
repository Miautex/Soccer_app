package pkgWSA;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import pkgMisc.StringEscape;

/**
 * @author Marco Wilscher
 */

public final class WebRequestTask extends AsyncTask <RequestParameter, Void, AccessorResponse> {

    private static final int TIMEOUT = 5000;
    private static final Charset CHARSET = Charset.forName("UTF-8");

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
                        con.setRequestProperty("Content-Type", "application/json; Charset=" + getCharsetName());
                        con.setRequestProperty("Accept", "application/json; Charset=" + getCharsetName());
                        con.setRequestProperty("Charset", getCharsetName());

                        if (parameter.getRequestBody() != null && !parameter.getRequestBody().isEmpty()) {
                            String bodyData = StringEscape.escapeString(parameter.getRequestBody());
                            con.setRequestProperty("Content-Length", Integer.toString(bodyData.getBytes(CHARSET).length));
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), CHARSET));
                            writer.write(bodyData);
                            writer.flush();
                            writer.close();
                        }
                        response.setCode(con.getResponseCode());
                        reader = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET));
                        responseContent = new StringBuilder();
                        while ((inputLine = reader.readLine()) != null) {
                            responseContent.append(inputLine);
                        }
                        response.setJson(StringEscape.unescapeString(responseContent.toString()));
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

    private String getCharsetName() {
        return CHARSET.toString();
    }
}

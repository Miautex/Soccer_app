package pkgTasks;

import android.os.AsyncTask;

import pkgWSA.Accessor;
import pkgWSA.AccessorResponse;
import pkgWSA.HttpMethod;

public class UpdatePlayerTask extends AsyncTask<String, Void, AccessorResponse> {

    @Override
    protected AccessorResponse doInBackground(String... args) {
        AccessorResponse response = null;

        try {
            //response = Accessor.requestJSON(HttpMethod.PUT, "player", args[0]);

            if (response == null) {
                throw new Exception("Could not reach webservice");
            }

        } catch (Exception e) {
            response = new AccessorResponse(500, e.getMessage());
        }

        return response;
    }
}
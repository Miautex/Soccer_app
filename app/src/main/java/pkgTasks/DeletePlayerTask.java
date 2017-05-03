package pkgTasks;


import android.os.AsyncTask;

import pkgWSA.AccessorResponse;

public class DeletePlayerTask extends AsyncTask<String, Void, AccessorResponse> {

    @Override
    protected AccessorResponse doInBackground(String... args) {
        AccessorResponse response = null;

        try {
            //response = Accessor.requestJSON(HttpMethod.DELETE, "player/" + args[0], null, null);

            if (response == null) {
                throw new Exception("Could not reach webservice");
            }

        } catch (Exception e) {
            response = new AccessorResponse(500, e.getMessage());
        }

        return response;
    }
}
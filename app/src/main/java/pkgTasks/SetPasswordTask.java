package pkgTasks;

import android.os.AsyncTask;

import pkgWSA.AccessorResponse;

public class SetPasswordTask extends AsyncTask<String, Void, AccessorResponse> {
    @Override
    protected AccessorResponse doInBackground(String... args) {
        AccessorResponse response = null;

        try {
            //response = Accessor.requestJSON(HttpMethod.GET, "player/security/" + args[0], null, null);

            if (response == null) {
                throw new Exception("Could not reach webservice");
            }
        }
        catch (Exception ex) {
            response = new AccessorResponse(500, ex.getMessage());
        }

        return response;
    }
}

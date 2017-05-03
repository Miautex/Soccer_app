package pkgTasks;

import android.os.AsyncTask;
import pkgWSA.AccessorResponse;


public class LoadAllPlayersTask extends AsyncTask<String, Void, AccessorResponse> {
    @Override
    protected AccessorResponse doInBackground(String... args) {
        AccessorResponse response = null;

        /*try {
            //response = Accessor.requestJSON(HttpMethod.GET, "player", null, null);

            if (response == null) {
                throw new Exception("Could not reach webservice");
            }
        }
        catch (Exception ex) {
            response = new AccessorResponse(500, ex.getMessage());
        }*/

        response = new AccessorResponse(202, "{\n" +
                "   \"type\": \"playerResult\",\n" +
                "   \"success\": true,\n" +
                "   \"content\":    [\n" +
                "            {\n" +
                "         \"admin\": false,\n" +
                "         \"goalDifference\": 56,\n" +
                "         \"id\": 1,\n" +
                "         \"name\": \"Martin\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 1,\n" +
                "         \"username\": \"martin\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": false,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 2,\n" +
                "         \"name\": \"Eliass\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"elias\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 3,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 4,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 5,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 6,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 7,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 8,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 9,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 10,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 11,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 12,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 13,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 14,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 15,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 16,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      }\n" +
                "   ]\n" +
                "}\n");

        return response;
    }
}
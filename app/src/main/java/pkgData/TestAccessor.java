package pkgData;

/**
 * Created by Elias on 24.04.2017.
 */

public class TestAccessor {
    public static String getAllGames() {
        return "{   \"type\": \"gameResult\",\n" +
                "   \"success\": true,\n" +
                "   \"content\":    [\n" +
                "            {\n" +
                "         \"date\": \"2017-03-16T00:00:00+01:00\",\n" +
                "         \"id\": 1,\n" +
                "         \"scoreTeamA\": 56,\n" +
                "         \"scoreTeamB\": 0\n" +
                "      },\n" +
                "            {\n" +
                "         \"date\": \"2017-03-15T00:00:00+01:00\",\n" +
                "         \"id\": 2,\n" +
                "         \"scoreTeamA\": 0,\n" +
                "         \"scoreTeamB\": 0\n" +
                "      },\n" +
                "            {\n" +
                "         \"date\": \"2017-03-14T00:00:00+01:00\",\n" +
                "         \"id\": 3,\n" +
                "         \"scoreTeamA\": 0,\n" +
                "         \"scoreTeamB\": 0\n" +
                "      },\n" +
                "            {\n" +
                "         \"date\": \"2017-03-12T00:00:00+01:00\",\n" +
                "         \"id\": 4,\n" +
                "         \"scoreTeamA\": 0,\n" +
                "         \"scoreTeamB\": 0\n" +
                "      }\n" +
                "   ]\n" +
                "}\n";
    }

    public static String insertPlayer(boolean isSuccess) {
        String retVal = null;

        if (isSuccess) {
            retVal = "{\n" +
                    "   \"type\": \"singlePlayerResult\",\n" +
                    "   \"success\": true,\n" +
                    "   \"content\":    {\n" +
                    "      \"admin\": false,\n" +
                    "      \"goalDifference\": 0,\n" +
                    "      \"id\": 11,\n" +
                    "      \"name\": \"jerome\",\n" +
                    "      \"numDefeats\": 0,\n" +
                    "      \"numDraws\": 0,\n" +
                    "      \"numWins\": 0,\n" +
                    "      \"username\": \"guina\"\n" +
                    "   }\n" +
                    "}\n";
        }
        else {
            retVal = "{\n" +
                    "   \"type\": \"singlePlayerResult\",\n" +
                    "   \"error\":    {\n" +
                    "      \"errorCode\": 0,\n" +
                    "      \"errorMessage\": \"com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'guina' for key 'USERNAME'\"\n" +
                    "   },\n" +
                    "   \"success\": false\n" +
                    "}\n";
        }

        return retVal;
    }

    public static String updatePlayer(boolean isSuccess) {
        String retVal = null;

        if (isSuccess) {
            retVal = "{\"success\": true}";
        }
        else {
            retVal = "{\"success\": false}";
        }

        return retVal;
    }

    public static String removePlayer(boolean isSuccess) {
        String retVal = null;

        if (isSuccess) {
            retVal = "{\"success\": true}";
        }
        else {
            retVal = "{\"success\": false}";
        }

        return retVal;
    }

    public static String getPlayerByUsername(String username) {
        return "{\n" +
                "   \"type\": \"singlePlayerResult\",\n" +
                "   \"success\": true,\n" +
                "   \"content\":    {\n" +
                "      \"admin\": false,\n" +
                "      \"goalDifference\": 56,\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Martinii\",\n" +
                "      \"numDefeats\": 0,\n" +
                "      \"numDraws\": 0,\n" +
                "      \"numWins\": 1,\n" +
                "      \"username\": \"" + username + "\"\n" +
                "   }\n" +
                "}\n";
    }

    public static String getPassword() {
        return "21232f297a57a5a743894a0e4a801fc3";
    }
}

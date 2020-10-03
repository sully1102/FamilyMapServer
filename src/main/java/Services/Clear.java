package Services;

import XPOJOS.Response.ClearResponse;
import SQLObjects.*;

public class Clear {

    private Database db;

    public Clear() {
        db = new Database();
    }

    public ClearResponse clearAll() {

        ClearResponse response = new ClearResponse();

        try {
            db.openConnection();
            db.reloadTables();

            response.setMessage("Clear succeeded");
            response.setSuccess(true);

            db.closeConnection(true);

        } catch(DataAccessException e) {
            response.setMessage("Internal server error");
            response.setSuccess(false);

            try {
                db.closeConnection(false);
            } catch(DataAccessException f) {
                f.printStackTrace();
            }
        }

        return response;
    }
}

package step.gallery.app.util;

import org.json.JSONObject;
import step.gallery.app.models.Picture;

import java.sql.*;
import java.util.ArrayList;

public class Db {
    final private static String SUFFIX = "_11";
    private static Connection connection;

    public static boolean setConnection(JSONObject connectionData){
        if(connectionData == null){ connection = null; return false; }

        String connectionString;

        try{
            connectionString = String.format(
                    "jdbc:oracle:thin:%s/%s@%s:%d/XE",
                    connectionData.get("user"),
                    connectionData.get("pass"),
                    connectionData.get("host"),
                    connectionData.get("port")
            );

            DriverManager.registerDriver(
                    new oracle.jdbc.OracleDriver()
            );

            connection =
                    DriverManager.getConnection(connectionString);
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            return false;
        }
        return true;
    }
    public static Connection getConnection() { return connection; }
    public static void closeConnection() {
        if (connection != null)
            try { connection.close();} catch (Exception ignored) { }
    }
    public static void createGallery() {
        if (connection == null) return;

        String query = null;
        try (Statement statement = connection.createStatement()) {
            query = "CREATE TABLE Pictures" + SUFFIX +
                    "(Id          RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, " +
                    " Name        NVARCHAR2(256) NOT NULL, " +
                    " Description NVARCHAR2(400) NULL, " +
                    " PicturePath NVARCHAR2(256) NULL, " +
                    " Moment      DATE DEFAULT CURRENT_TIMESTAMP )";

            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.err.println("createGallery: " + ex.getMessage() + " " + query);
        }
    }
    public static ArrayList<Picture> getPictures() {
        ArrayList<Picture> res = null;

        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Pictures" + SUFFIX + " ORDER BY MOMENT DESC";
            ResultSet answer = statement.executeQuery(query);
            res = new ArrayList<>();

            while (answer.next()) {
                res.add(new Picture(
                        answer.getString("ID"),
                        answer.getString("NAME"),
                        answer.getString("DESCRIPTION"),
                        answer.getString("PICTUREPATH"),
                        answer.getString("MOMENT")
                ));
            }
        } catch (Exception ex) {
            System.err.println("getPictures: " + ex.getMessage());
        }

        return res;
    }
    public static Picture getPictureById(String id) {
        if (connection == null) return null;

        try (PreparedStatement prep = connection.prepareStatement("SELECT * FROM Pictures" + SUFFIX + " WHERE Id = ?")) {
            prep.setString(1, id);
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                return new Picture(
                        res.getString("ID"),
                        res.getString("NAME"),
                        res.getString("DESCRIPTION"),
                        res.getString("PICTUREPATH"),
                        res.getString("MOMENT")
                );
            } else return null;
        } catch (SQLException ex) {
            System.err.println("getPictureById: "
                    + ex.getMessage());

            return null;
        }
    }
    public static boolean addPicture(Picture pic) {
        if (connection == null) return false;

        String query = "INSERT INTO Pictures" + SUFFIX + "(Name, Description, PicturePath) VALUES(?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pic.getName());
            statement.setString(2, pic.getDescription());
            statement.setString(3, pic.getPicturePath());
            statement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            System.err.println("addPicture: " + ex.getMessage() + " " + query);

            return false;
        }
    }
    public static boolean deletePictureById(String id) {
        if (connection == null) return false;

        String query = "DELETE FROM Pictures" + SUFFIX + " WHERE id = ?";

        try(PreparedStatement prep = connection.prepareStatement(query)) {
            prep.setString(1, id);
            prep.executeUpdate();

            return true;
        } catch (SQLException ex){
            System.err.println("deletePictureById: " + ex.getMessage() + " " + query );

            return false;
        }
    }
    public static boolean updatePicture(Picture pic) {
        if (connection == null
                || pic == null
                || pic.getId() == null) {
            return false;
        }

        // Validate Id
        if (!pic.getId().matches("^[0-9A-F]+$")) {
            System.err.println("updatePicture: Id error " + pic.getId());
            return false;
        }

        String query = "UPDATE Pictures" + SUFFIX + " SET ";
        boolean needComma = false;

        if (pic.getName() != null) {
            query += " Name = '" +
                    pic.getName().replace("'", "''") + "'";
            needComma = true;
        }
        if (pic.getDescription() != null) {
            if (needComma) query += ", ";

            query += " Description = '" + pic.getDescription().replace("'", "''") + "'";
            needComma = true;
        }

        if (pic.getPicturePath() != null) {
            if (needComma) query += ", ";

            query += " PicturePath = '" + pic.getPicturePath().replace("'", "''") + "'";
            needComma = true;
        }

        if (pic.getMoment() != null) {
            if (needComma) query += ", ";

            query += " Moment = '" + pic.getMoment().replace("'", "''") + "'";
            needComma = true;
        }

        if (!needComma) {
            // No fields were added
            return false;
        }

        query += " WHERE Id = '" + pic.getId() + "'";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);

            return true;
        } catch (SQLException ex) {
            System.err.println("updatePicture: " + ex.getMessage() + " " + query);

            return false;
        }
    }
    public static boolean isTableExists(String tableName){

        final String query = "SELECT COUNT(*) FROM USER_TABLES T WHERE T.TABLE_NAME = '" + tableName + SUFFIX + "'";
        try(
                ResultSet res = connection
                        .createStatement()
                        .executeQuery(query))
        {
            if(res.next()){
                return res.getInt(1) == 1;
            }
        } catch (SQLException ex){
            System.err.println("BookOrm.isTableExists(" + tableName + "): "
                    + ex.getMessage());
            return false;
        }

        return false;
    }

}

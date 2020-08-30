package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.proxy.AutoControlProxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO extends AbstractDAO {
    private static AutoDAO dao = null;

    private AutoDAO() {
    }

    public static AutoDAO getInstance() {
        if (dao == null) {
            dao = new AutoDAO();
        }
        return dao;
    }

    //Erzeugt die Autos, die ein Vertriebler erstellt hat
    public List<AutoDTO> getAutoList(UserDTO userDTO) throws SQLException {
        String sql = "SELECT id_anzeige, beschreibung, art, name, zeitraum, branche, studiengang, ort " +
                "FROM collhbrs.stellenanzeige " +
                "WHERE id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        List<AutoDTO> list = new ArrayList<>();
        assert rs != null;
        buildList(rs, list);
        return list;
    }


    //Erstellt ein neues Auto in der Datenbank
    public boolean createAuto(AutoDTO auto, UserDTO userDTO) {
        String sql = "INSERT INTO carlook.auto(marke, baujahr, beschreibung, id)" +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setString(1, auto.getMarke());
            statement.setInt(2, auto.getBaujahr());
            statement.setString(3, auto.getBeschreibung());
            statement.setInt(4, userDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    //Verändert ein bestehendes Auto in der Datenbank
    public boolean updateAuto(AutoDTO auto) {
        String sql = "UPDATE collhbrs.auto " +
                "SET marke = ?, art = ?,  baujahr = ?, beschreibung = ?" +
                "WHERE carlook.auto.auto_id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, auto.getMarke());
            statement.setInt(2, auto.getBaujahr());
            statement.setString(3, auto.getBeschreibung());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }


    //Löscht ein Auto aus der Datenbank
    public boolean deleteAuto(AutoDTO auto) {
        String sql = "DELETE " +
                "FROM carlook.auto " +
                "WHERE carlook.auto.auto_id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, auto.getAuto_id());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException {
        filter = filter.toLowerCase();
        PreparedStatement statement;
        ResultSet rs = null;
        if (suchtext.equals("")) {
            String sql = "SELECT auto_id, marke, baujahr, beschreibung" +
                    "FROM carlook.auto ; ";
            statement = this.getPreparedStatement(sql);
            try {
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        } else {
            String sql = "SELECT auto_id, marke, baujahr, beschreibung" +
                    "FROM carlook.auto44 " +
                    "WHERE " + filter + " like ? ;";
            statement = this.getPreparedStatement(sql);


            try {
                statement.setString(1, "%" + suchtext + "%");
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        }

        List<AutoDTO> list = new ArrayList<>();

        assert rs != null;
        buildList(rs, list);
        return list;
    }

    //Zeigt alle Stellenanzeigen an, auf die sich ein Student beworben hat (***Benz***)
    public List<AutoDTO> reserviereAuto(EndkundeDTO endkundeDTO) throws SQLException {
        String sql = "SELECT auto_id, marke, baujahr, beschreibung" +
                "FROM carlook.auto " +
                "WHERE auto_id = ( SELECT auto_id " +
                "FROM carlook.reservierung_to_auto " +
                "WHERE reservierungs_id = ?);";
        PreparedStatement statement = this.getPreparedStatement(sql);
        List<ReservierungDTO> list = ReservierungDAO.getInstance().getReservierungForEndkunde(endkundeDTO);
        List<AutoDTO> listAuto = new ArrayList<>();
        ResultSet rs = null;
        for (ReservierungDTO reservierungDTO : list) {
            int id_bewerbung = reservierungDTO.getId();
            try {
                statement.setInt(1, id_bewerbung);
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
            assert rs != null;
            buildList(rs, listAuto);
        }

        return listAuto;
    }

    private void buildList(ResultSet rs, List<AutoDTO> listStellenanzeige) throws SQLException {

        AutoDTO autoDTO;
        try {
            while (rs.next()) {

                autoDTO = new AutoDTO();
                autoDTO.setMarke(rs.getString(1));
                autoDTO.setBaujahr(rs.getInt(2));
                autoDTO.setBeschreibung(rs.getString(3));

               //Brauchen wir die Anzahl der Reservierungen für ein Auto?? (***Mentor***)
                try {

                    autoDTO.setAnzahl_res(AutoControlProxy.getInstance().getAnzahlRes(autoDTO));

                } catch (DatabaseException e) {

                    Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte informieren Sie einen Administrator!");

                }
                listStellenanzeige.add(autoDTO);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein schwerer SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally{
            assert rs != null;
            rs.close();
        }
    }


}



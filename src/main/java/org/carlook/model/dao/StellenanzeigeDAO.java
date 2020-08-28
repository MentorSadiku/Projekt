package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.objects.dto.BewerbungDTO;
import org.carlook.model.objects.dto.StellenanzeigeDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.proxy.StellenanzeigeControlProxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StellenanzeigeDAO extends AbstractDAO {
    private static StellenanzeigeDAO dao = null;

    private StellenanzeigeDAO() {
    }

    public static StellenanzeigeDAO getInstance() {
        if (dao == null) {
            dao = new StellenanzeigeDAO();
        }
        return dao;
    }

    //Erzeugt die Stellenanezeigen, die ein Unternehmen erstellt hat
    public List<StellenanzeigeDTO> getStellenanzeigenForUnternehmen(UserDTO userDTO) throws SQLException {
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
        List<StellenanzeigeDTO> list = new ArrayList<>();
        assert rs != null;
        buildList(rs, list);
        return list;
    }


    //Erstellt eine neue Stellenanzeige in der Datenbank
    public boolean createStellenanzeige(StellenanzeigeDTO stellenanzeige, UserDTO userDTO) {
        String sql = "INSERT INTO collhbrs.stellenanzeige(id, beschreibung, art, name, zeitraum, branche, studiengang, ort)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setInt(1, userDTO.getId());
            statement.setString(2, stellenanzeige.getBeschreibung());
            statement.setString(3, stellenanzeige.getArt());
            statement.setString(4, stellenanzeige.getName());
            statement.setObject(5, stellenanzeige.getZeitraum());
            statement.setString(6, stellenanzeige.getBranche());
            statement.setString(7, stellenanzeige.getStudiengang());
            statement.setString(8, stellenanzeige.getOrt());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    //Verändert eine bestehende Stellenanzeige in der Datenbank
    public boolean updateStellenanzeige(StellenanzeigeDTO stellenanzeige) {
        String sql = "UPDATE collhbrs.stellenanzeige " +
                "SET beschreibung = ?, art = ?,  name = ?, zeitraum = ?, branche = ?, studiengang = ?, ort = ?  " +
                "WHERE collhbrs.stellenanzeige.id_anzeige = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, stellenanzeige.getBeschreibung());
            statement.setString(2, stellenanzeige.getArt());
            statement.setString(3, stellenanzeige.getName());
            statement.setObject(4, stellenanzeige.getZeitraum());
            statement.setString(5, stellenanzeige.getBranche());
            statement.setString(6, stellenanzeige.getStudiengang());
            statement.setString(7, stellenanzeige.getOrt());
            statement.setInt(8, stellenanzeige.getId_anzeige());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }


    //Löscht eine Stellenanzeige aus der Datenbank
    public boolean deleteStellenanzeige(StellenanzeigeDTO stellenanzeige) {
        String sql = "DELETE " +
                "FROM collhbrs.stellenanzeige " +
                "WHERE collhbrs.stellenanzeige.id_anzeige = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, stellenanzeige.getId_anzeige());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public List<StellenanzeigeDTO> getStellenanzeigenForSearch(String suchtext, String filter) throws SQLException {
        filter = filter.toLowerCase();
        PreparedStatement statement;
        ResultSet rs = null;
        if (suchtext.equals("")) {
            String sql = "SELECT id_anzeige, beschreibung, art, name, zeitraum, branche, studiengang, ort " +
                    "FROM collhbrs.stellenanzeige ;";
            statement = this.getPreparedStatement(sql);
            try {
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        } else {
            String sql = "SELECT id_anzeige, beschreibung, art, name, zeitraum, branche, studiengang, ort " +
                    "FROM collhbrs.stellenanzeige " +
                    "WHERE " + filter + " like ? ;";
            statement = this.getPreparedStatement(sql);


            try {
                statement.setString(1, "%" + suchtext + "%");
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        }

        List<StellenanzeigeDTO> list = new ArrayList<>();

        assert rs != null;
        buildList(rs, list);
        return list;
    }

    //Zeigt alle Stellenanzeigen an, auf die sich ein Student beworben hat
    public List<StellenanzeigeDTO> getStellenanzeigeforStudent(EndkundeDTO endkundeDTO) throws SQLException {
        String sql = "SELECT id_anzeige, beschreibung, art, name, zeitraum, branche, studiengang, ort " +
                "FROM collhbrs.stellenanzeige " +
                "WHERE id_anzeige = ( SELECT id_anzeige " +
                "FROM collhbrs.bewerbung_to_stellenanzeige " +
                "WHERE id_bewerbung = ? );";
        PreparedStatement statement = this.getPreparedStatement(sql);
        List<BewerbungDTO> list = BewerbungDAO.getInstance().getBewerbungenForStudent(endkundeDTO);
        List<StellenanzeigeDTO> listStellenanzeige = new ArrayList<>();
        ResultSet rs = null;
        for (BewerbungDTO bewerbungDTO : list) {
            int id_bewerbung = bewerbungDTO.getId();
            try {
                statement.setInt(1, id_bewerbung);
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
            assert rs != null;
            buildList(rs, listStellenanzeige);
        }

        return listStellenanzeige;
    }

    private void buildList(ResultSet rs, List<StellenanzeigeDTO> listStellenanzeige) throws SQLException {

        StellenanzeigeDTO stellenanzeigeDTO;
        try {
            while (rs.next()) {

                stellenanzeigeDTO = new StellenanzeigeDTO();
                stellenanzeigeDTO.setId_anzeige(rs.getInt(1));
                stellenanzeigeDTO.setBeschreibung(rs.getString(2));
                stellenanzeigeDTO.setArt(rs.getString(3));
                stellenanzeigeDTO.setName(rs.getString(4));
                stellenanzeigeDTO.setZeitraum(rs.getDate(5).toLocalDate());
                stellenanzeigeDTO.setBranche(rs.getString(6));
                stellenanzeigeDTO.setStudiengang(rs.getString(7));
                stellenanzeigeDTO.setOrt(rs.getString(8));
                try {

                    stellenanzeigeDTO.setAnzahl_bewerber(StellenanzeigeControlProxy.getInstance().getAnzahlBewerber(stellenanzeigeDTO));

                } catch (DatabaseException e) {

                    Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte informieren Sie einen Administrator!");

                }
                listStellenanzeige.add(stellenanzeigeDTO);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein schwerer SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally{
            assert rs != null;
            rs.close();
        }
    }


}



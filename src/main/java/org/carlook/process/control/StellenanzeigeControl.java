package org.carlook.process.control;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.dao.StellenanzeigeDAO;
import org.carlook.model.objects.dto.StellenanzeigeDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.StellenanzeigeControlInterface;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.StellenanzeigeException;
import org.carlook.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StellenanzeigeControl implements StellenanzeigeControlInterface {
    private static StellenanzeigeControl search = null;

    public static StellenanzeigeControl getInstance() {
        if (search == null) {
            search = new StellenanzeigeControl();
        }
        return search;
    }

    private StellenanzeigeControl() {

    }

    public List<StellenanzeigeDTO> getAnzeigenForUnternehmen(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return StellenanzeigeDAO.getInstance().getStellenanzeigenForUnternehmen(vertrieblerDTO);
    }

    public List<StellenanzeigeDTO> getAnzeigenForStudent(EndkundeDTO endkundeDTO) throws SQLException {
        return StellenanzeigeDAO.getInstance().getStellenanzeigeforStudent(endkundeDTO);

    }

    public void createStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO) throws StellenanzeigeException {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        boolean result = StellenanzeigeDAO.getInstance().createStellenanzeige(stellenanzeigeDTO, userDTO);
        if (result) {
            return;
        }
        throw new StellenanzeigeException();
    }

    public void updateStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO) throws StellenanzeigeException {
        boolean result = StellenanzeigeDAO.getInstance().updateStellenanzeige(stellenanzeigeDTO);
        if (result) {
            return;
        }
        throw new StellenanzeigeException();
    }

    public void deleteStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO) throws StellenanzeigeException {
        boolean result = StellenanzeigeDAO.getInstance().deleteStellenanzeige(stellenanzeigeDTO);
        if (result) {
            return;
        }
        throw new StellenanzeigeException();
    }

    public List<StellenanzeigeDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException {
        return StellenanzeigeDAO.getInstance().getStellenanzeigenForSearch(suchtext, filter);
    }

    public int getAnzahlBewerber(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseException, SQLException {
        int anzahl_bewerber = 0;
        String sql = "SELECT count(id_bewerbung) " +
                "FROM collhbrs.bewerbung_to_stellenanzeige " +
                "WHERE id_anzeige = ? ;";
        ResultSet rs;
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, stellenanzeigeDTO.getId_anzeige());
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            throw new DatabaseException("Fehler im SQL-Befehl: Bitte den Programmierer informieren!");
        }
        try {
            if (rs.next()) {
                anzahl_bewerber = rs.getInt(1);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        } finally {
            //JDBCConnection.getInstance().closeConnection();
            rs.close();
        }

        return anzahl_bewerber;
    }
}

package org.carlook.process.control;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.dao.AutoDAO;
import org.carlook.model.objects.dto.AutoDTO;
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

    public List<AutoDTO> getAnzeigenForUnternehmen(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return AutoDAO.getInstance().getAutoList(vertrieblerDTO);
    }

    public List<AutoDTO> getAnzeigenForStudent(EndkundeDTO endkundeDTO) throws SQLException {
        return AutoDAO.getInstance().reserviereAuto(endkundeDTO);

    }

    public void createStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        boolean result = AutoDAO.getInstance().createAuto(autoDTO, userDTO);
        if (result) {
            return;
        }
        throw new StellenanzeigeException();
    }

    public void updateStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException {
        boolean result = AutoDAO.getInstance().updateAuto(autoDTO);
        if (result) {
            return;
        }
        throw new StellenanzeigeException();
    }

    public void deleteStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException {
        boolean result = AutoDAO.getInstance().deleteAuto(autoDTO);
        if (result) {
            return;
        }
        throw new StellenanzeigeException();
    }

    public List<AutoDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException {
        return AutoDAO.getInstance().getAutoForSearch(suchtext, filter);
    }

    public int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException {
        int anzahl_bewerber = 0;
        String sql = "SELECT count(reservierungs_id) " +
                "FROM carlook.reservierung_to_auto" +
                "WHERE reservierungs_id = ? ;";
        ResultSet rs;
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, autoDTO.getBaujahr());
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

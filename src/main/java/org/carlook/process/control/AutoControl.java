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
import org.carlook.process.exceptions.AutoException;
import org.carlook.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AutoControl implements StellenanzeigeControlInterface {
    private static AutoControl search = null;

    public static AutoControl getInstance() {
        if (search == null) {
            search = new AutoControl();
        }
        return search;
    }

    private AutoControl() {

    }

    public List<AutoDTO> getAutoForVertriebler(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return AutoDAO.getInstance().getAutoList(vertrieblerDTO);
    }

    public List<AutoDTO> getAutoForEndkunde(EndkundeDTO endkundeDTO) throws SQLException {
        return AutoDAO.getInstance().reserviereAuto(endkundeDTO);

    }

    public void createAuto(AutoDTO autoDTO) throws AutoException {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        boolean result = AutoDAO.getInstance().createAuto(autoDTO, userDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public void updateAuto(AutoDTO autoDTO) throws AutoException {
        boolean result = AutoDAO.getInstance().updateAuto(autoDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public void deleteAuto(AutoDTO autoDTO) throws AutoException {
        boolean result = AutoDAO.getInstance().deleteAuto(autoDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException {
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

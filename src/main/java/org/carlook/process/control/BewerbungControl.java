package org.carlook.process.control;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.carlook.model.dao.ReservierungDAO;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.BewerbungControlInterface;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.services.db.JDBCConnection;
import org.carlook.services.util.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BewerbungControl implements BewerbungControlInterface {
    private static BewerbungControl bewerbungControl = null;

    private BewerbungControl() {

    }

    public static BewerbungControl getInstance() {
        if (bewerbungControl == null) {
            bewerbungControl = new BewerbungControl();
        }
        return bewerbungControl;
    }

    public int getLatestApply(UserDTO userDTO) throws DatabaseException, SQLException {
        int id_bewerbung = 0;
        String sql = "SELECT max(id_bewerbung) " +
                "FROM collhbrs.bewerbung " +
                "WHERE id = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();
            if (rs.next()) {
                id_bewerbung = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
        } finally {
            assert rs != null;
            rs.close();
        }
        return id_bewerbung;
    }

    public void applyForStellenanzeige(AutoDTO stellenanzeige, int id_bewerbung) throws DatabaseException {
        String sql = "INSERT INTO collhbrs.bewerbung_to_stellenanzeige (id_bewerbung, id_anzeige) " +
                "VALUES (?, ?);";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, id_bewerbung);
            statement.setInt(2, stellenanzeige.getBaujahr());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
        }
    }

    public void applyingIsAllowed() throws DatabaseException, SQLException, ReservierungException {
        String sql = "SELECT sichtbar " +
                "FROM collhbrs.stellenanzeige_on_off";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            rs = statement.executeQuery();
            if ( rs.next() ) {
                if (rs.getBoolean(1)) {
                    return;
                }
                throw new ReservierungException();
            }
        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
        } finally {
            assert rs != null;
            rs.close();
        }
    }

    public void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws DatabaseException, SQLException, ReservierungException {
        EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
        List<ReservierungDTO> list = ReservierungDAO.getInstance().getReservierungForEndkunde(endkundeDTO);
        String sql = "SELECT id_anzeige " +
                "FROM collhbrs.bewerbung_to_stellenanzeige " +
                "WHERE id_bewerbung = ? " +
                "AND id_anzeige = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (ReservierungDTO reservierungDTO : list) {
            int id_bewerbung = reservierungDTO.getId();
            try {
                statement.setInt(1, id_bewerbung);
                statement.setInt(2, autoDTO.getBaujahr());
                rs = statement.executeQuery();
                if (rs.next()) {
                    throw new ReservierungException();
                }
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
            } finally {
                assert rs != null;
                rs.close();
            }
        }

    }
    public void checkAllowed(AutoDTO stellenanzeige, UserDTO userDTO, Button bewerbenButton) {
        if (userDTO == null || !userDTO.hasRole(Roles.ENDKUNDE)) {
            bewerbenButton.setVisible(false);
            return;
        }
        try {
            applyingIsAllowed();
            checkAlreadyApplied(stellenanzeige, userDTO);
        } catch (DatabaseException e) {
            Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
        } catch (ReservierungException e) {
            bewerbenButton.setVisible(false);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
        }
    }

    public void createBewerbung(String bewerbungstext, UserDTO userDTO) throws ReservierungException {
        EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
        boolean result = ReservierungDAO.getInstance().createReservierung(endkundeDTO);
        if (!result) {
            throw new ReservierungException();
        }
    }

    public ReservierungDTO getBewerbungForStellenanzeige(AutoDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException {
        List<ReservierungDTO> list = getBewerbungenForStudent(endkundeDTO);
        ReservierungDTO reservierungDTO = new ReservierungDTO();
        String sql = "SELECT id_bewerbung " +
                "FROM collhbrs.bewerbung_to_stellenanzeige " +
                "WHERE id_anzeige = ? " +
                "AND id_bewerbung = ? ";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (ReservierungDTO bewerbung :list ) {
            try {
                statement.setInt(1, selektiert.getBaujahr());
                statement.setInt(2, bewerbung.getId());
                rs = statement.executeQuery();
                if ( rs.next() ) {
                    reservierungDTO = bewerbung;
                    break;
                }
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
            } finally{
                assert rs != null;
                rs.close();
            }
        }
        return reservierungDTO;
    }

    public List<ReservierungDTO> getBewerbungenForStudent(EndkundeDTO endkundeDTO) throws SQLException {
        return ReservierungDAO.getInstance().getReservierungForEndkunde(endkundeDTO);
    }

    public void deleteBewerbung(ReservierungDTO reservierungDTO) throws ReservierungException {
        boolean result = ReservierungDAO.getInstance().deleteReservierung(reservierungDTO);
        if (result) {
            return;
        }
        throw new ReservierungException();
    }
}

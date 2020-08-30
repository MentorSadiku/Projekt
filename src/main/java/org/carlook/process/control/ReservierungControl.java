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

public class ReservierungControl implements BewerbungControlInterface {
    private static ReservierungControl reservierungControl = null;

    private ReservierungControl() {

    }

    public static ReservierungControl getInstance() {
        if (reservierungControl == null) {
            reservierungControl = new ReservierungControl();
        }
        return reservierungControl;
    }

    public int getLatestApply(UserDTO userDTO) throws DatabaseException, SQLException {
        int reservierungs_id = 0;
        String sql = "SELECT max(reservierungs_id) " +
                "FROM coarlook.reservierung " +
                "WHERE id = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();
            if (rs.next()) {
                reservierungs_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
        } finally {
            assert rs != null;
            rs.close();
        }
        return reservierungs_id;
    }

    public void applyForStellenanzeige(AutoDTO stellenanzeige, int reservierungs_id) throws DatabaseException {
        String sql = "INSERT INTO carlook.reservierung_to_auto (reservierungs_id, auto_id) " +
                "VALUES (?, ?);";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, reservierungs_id);
            statement.setInt(2, stellenanzeige.getAuto_id());
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
        String sql = "SELECT auto_id " +
                "FROM collhbrs.reservierung_to_auto " +
                "WHERE reservierungs_id = ? " +
                "AND auto_id = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (ReservierungDTO reservierungDTO : list) {
            int reservierungs_id = reservierungDTO.getId();
            try {
                statement.setInt(1, reservierungs_id);
                statement.setInt(2, autoDTO.getAuto_id());
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
    public void checkAllowed(AutoDTO autoDTO, UserDTO userDTO, Button bewerbenButton) {
        if (userDTO == null || !userDTO.hasRole(Roles.ENDKUNDE)) {
            bewerbenButton.setVisible(false);
            return;
        }
        try {
            applyingIsAllowed();
            checkAlreadyApplied(autoDTO, userDTO);
        } catch (DatabaseException e) {
            Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
        } catch (ReservierungException e) {
            bewerbenButton.setVisible(false);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
        }
    }

    public void createBewerbung(UserDTO userDTO) throws ReservierungException {
        EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
        boolean result = ReservierungDAO.getInstance().createReservierung(endkundeDTO);
        if (!result) {
            throw new ReservierungException();
        }
    }

    public ReservierungDTO getBewerbungForStellenanzeige(AutoDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException {
        List<ReservierungDTO> list = getBewerbungenForStudent(endkundeDTO);
        ReservierungDTO reservierungDTO = new ReservierungDTO();
        String sql = "SELECT reservierungs_id " +
                "FROM carlook.reservierung_to_auto " +
                "WHERE auto_id = ? " +
                "AND reservierungs_id = ? ";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (ReservierungDTO reservierung :list ) {
            try {
                statement.setInt(1, selektiert.getAuto_id());
                statement.setInt(2, reservierung.getId());
                rs = statement.executeQuery();
                if ( rs.next() ) {
                    reservierungDTO = reservierung;
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

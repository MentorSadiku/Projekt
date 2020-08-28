package org.carlook.process.control;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.carlook.model.dao.BewerbungDAO;
import org.carlook.model.objects.dto.BewerbungDTO;
import org.carlook.model.objects.dto.StellenanzeigeDTO;
import org.carlook.model.objects.dto.StudentDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.BewerbungControlInterface;
import org.carlook.process.exceptions.BewerbungException;
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
            Logger.getLogger((BewerbungDAO.class.getName())).log(Level.SEVERE, null, ex);
        } finally {
            assert rs != null;
            rs.close();
        }
        return id_bewerbung;
    }

    public void applyForStellenanzeige(StellenanzeigeDTO stellenanzeige, int id_bewerbung) throws DatabaseException {
        String sql = "INSERT INTO collhbrs.bewerbung_to_stellenanzeige (id_bewerbung, id_anzeige) " +
                "VALUES (?, ?);";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, id_bewerbung);
            statement.setInt(2, stellenanzeige.getId_anzeige());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger((BewerbungDAO.class.getName())).log(Level.SEVERE, null, ex);
        }
    }

    public void applyingIsAllowed() throws DatabaseException, SQLException, BewerbungException {
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
                throw new BewerbungException();
            }
        } catch (SQLException ex) {
            Logger.getLogger((BewerbungDAO.class.getName())).log(Level.SEVERE, null, ex);
        } finally {
            assert rs != null;
            rs.close();
        }
    }

    public void checkAlreadyApplied(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO) throws DatabaseException, SQLException, BewerbungException {
        StudentDTO studentDTO = new StudentDTO(userDTO);
        List<BewerbungDTO> list = BewerbungDAO.getInstance().getBewerbungenForStudent(studentDTO);
        String sql = "SELECT id_anzeige " +
                "FROM collhbrs.bewerbung_to_stellenanzeige " +
                "WHERE id_bewerbung = ? " +
                "AND id_anzeige = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (BewerbungDTO bewerbungDTO : list) {
            int id_bewerbung = bewerbungDTO.getId();
            try {
                statement.setInt(1, id_bewerbung);
                statement.setInt(2, stellenanzeigeDTO.getId_anzeige());
                rs = statement.executeQuery();
                if (rs.next()) {
                    throw new BewerbungException();
                }
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
            } finally {
                assert rs != null;
                rs.close();
            }
        }

    }
    public void checkAllowed(StellenanzeigeDTO stellenanzeige, UserDTO userDTO, Button bewerbenButton) {
        if (userDTO == null || !userDTO.hasRole(Roles.STUDENT)) {
            bewerbenButton.setVisible(false);
            return;
        }
        try {
            applyingIsAllowed();
            checkAlreadyApplied(stellenanzeige, userDTO);
        } catch (DatabaseException e) {
            Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
        } catch (BewerbungException e) {
            bewerbenButton.setVisible(false);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
        }
    }

    public void createBewerbung(String bewerbungstext, UserDTO userDTO) throws BewerbungException {
        StudentDTO studentDTO = new StudentDTO(userDTO);
        boolean result = BewerbungDAO.getInstance().createBewerbung(bewerbungstext, studentDTO);
        if (!result) {
            throw new BewerbungException();
        }
    }

    public BewerbungDTO getBewerbungForStellenanzeige(StellenanzeigeDTO selektiert, StudentDTO studentDTO) throws SQLException, DatabaseException {
        List<BewerbungDTO> list = getBewerbungenForStudent(studentDTO);
        BewerbungDTO bewerbungDTO = new BewerbungDTO();
        String sql = "SELECT id_bewerbung " +
                "FROM collhbrs.bewerbung_to_stellenanzeige " +
                "WHERE id_anzeige = ? " +
                "AND id_bewerbung = ? ";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (BewerbungDTO bewerbung :list ) {
            try {
                statement.setInt(1, selektiert.getId_anzeige());
                statement.setInt(2, bewerbung.getId());
                rs = statement.executeQuery();
                if ( rs.next() ) {
                    bewerbungDTO = bewerbung;
                    break;
                }
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
            } finally{
                assert rs != null;
                rs.close();
            }
        }
        return bewerbungDTO;
    }

    public List<BewerbungDTO> getBewerbungenForStudent(StudentDTO studentDTO) throws SQLException {
        return BewerbungDAO.getInstance().getBewerbungenForStudent(studentDTO);
    }

    public void deleteBewerbung(BewerbungDTO bewerbungDTO) throws BewerbungException {
        boolean result = BewerbungDAO.getInstance().deleteBewerbung(bewerbungDTO);
        if (result) {
            return;
        }
        throw new BewerbungException();
    }
}

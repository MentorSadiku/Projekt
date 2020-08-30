package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.factory.ReservierungDTOFactory;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservierungDAO extends AbstractDAO {
    private static ReservierungDAO reservierungDAO = null;

    private ReservierungDAO() {

    }

    public static ReservierungDAO getInstance() {
        if (reservierungDAO == null) {
            reservierungDAO = new ReservierungDAO();
        }
        return reservierungDAO;
    }

    public ReservierungDTO getReservierung(int reservierungs_id) throws DatabaseException, SQLException {
        String sql = "SELECT reservierungs_id" +
                "FROM carlook.reservierung" +
                "WHERE reservierungs_id = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        ReservierungDTO reservierungDTO = null;
        try {
            statement.setInt(1, reservierungs_id);
            rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                reservierungDTO = ReservierungDTOFactory.createReservierungDTO(id);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally {
            assert rs != null;
            rs.close();
        }
        return reservierungDTO;
    }

    public List<ReservierungDTO> getReservierungForEndkunde(EndkundeDTO endkundeDTO) throws SQLException {
        String sql = "SELECT reservierungs_id" +
                "FROM carlook.reservierung " +
                "WHERE id = ? ;";
        List<ReservierungDTO> list = new ArrayList<>();
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, endkundeDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        ReservierungDTO reservierungDTO;
        try {
            while (true) {
                assert rs != null;
                if (!rs.next()) break;
                int id = rs.getInt(1);
                reservierungDTO = ReservierungDTOFactory.createReservierungDTO(id);
                list.add(reservierungDTO);

            }
        } catch (SQLException ex) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        finally{
            assert rs != null;
            rs.close();
        }
        return list;
    }

    public boolean createReservierung(EndkundeDTO endkundeDTO) {
        String sql = "INSERT INTO carlook.reservierung (id) " +
                "VALUES (?); ";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, endkundeDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public boolean deleteReservierung(ReservierungDTO reservierungDTO) {
        String sql = "DELETE " +
                "FROM carlook.reservierung " +
                "WHERE reservierungs_id = ?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, reservierungDTO.getId());
            statement.executeUpdate();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

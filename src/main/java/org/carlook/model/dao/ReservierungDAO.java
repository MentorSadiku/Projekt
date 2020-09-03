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
            Notification.show("11 Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally {
            assert rs != null;
            rs.close();
        }
        return reservierungDTO;
    }

    public List<ReservierungDTO> getReservierungForEndkunde(EndkundeDTO endkundeDTO) throws SQLException {
        String sql = "SELECT reservierungs_id " +
                "FROM carlook.reservierung " +
                "WHERE endkunde_id = ? ;";
        List<ReservierungDTO> list = new ArrayList<>();
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, endkundeDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Notification.show("16 Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
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
            Notification.show("18 Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        finally{
            assert rs != null;
            rs.close();
        }
        return list;
    }

    public int getMaxID() throws SQLException {
        String sql = "SELECT max(reservierungs_id) " +
                "FROM carlook.reservierung ;";
        PreparedStatement statement = getPreparedStatement(sql);
        ResultSet rs = null;

        try {
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            System.out.println("Fehler 1 bei addReservierung");
        }

        int currentValue = 0;

        try {
            assert rs != null;
            rs.next();
            currentValue = rs.getInt(1);
        } catch (SQLException throwables) {
            System.out.println("Fehler 2 bei addReservierung");
        } finally {
            assert rs != null;
            rs.close();
        }
        return currentValue;
    }

    public int getId (){
        return reservierungDAO.getId();
    }

    public int createReservierung(EndkundeDTO endkundeDTO) {
        String sql = "INSERT INTO carlook.reservierung (endkunde_id, reservierungs_id) " +
                "VALUES (?,?); ";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            int x=reservierungDAO.getMaxID()+1;
            statement.setInt(1, endkundeDTO.getId());
            statement.setInt(2, reservierungDAO.getMaxID()+1);
            statement.executeUpdate();
            return x;
        } catch (SQLException ex) {
            return -1;
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

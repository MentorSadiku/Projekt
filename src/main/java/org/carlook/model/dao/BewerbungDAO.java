package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.factory.BewerbungDTOFactory;
import org.carlook.model.objects.dto.BewerbungDTO;
import org.carlook.model.objects.dto.StudentDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BewerbungDAO extends AbstractDAO {
    private static BewerbungDAO bewerbungDAO = null;

    private BewerbungDAO() {

    }

    public static BewerbungDAO getInstance() {
        if (bewerbungDAO == null) {
            bewerbungDAO = new BewerbungDAO();
        }
        return bewerbungDAO;
    }

    public BewerbungDTO getBewerbung(int id_bewerbung) throws DatabaseException, SQLException {
        String sql = "SELECT id_bewerbung, freitext " +
                "FROM collhbrs.bewerbung " +
                "WHERE id_bewerbung = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        BewerbungDTO bewerbungDTO = null;
        try {
            statement.setInt(1, id_bewerbung);
            rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String text = rs.getString(2);
                bewerbungDTO = BewerbungDTOFactory.createBewerbungDTO(id, text);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally {
            assert rs != null;
            rs.close();
        }
        return bewerbungDTO;
    }

    public List<BewerbungDTO> getBewerbungenForStudent(StudentDTO studentDTO) throws SQLException {
        String sql = "SELECT id_bewerbung, freitext " +
                "FROM collhbrs.bewerbung " +
                "WHERE id = ? ;";
        List<BewerbungDTO> list = new ArrayList<>();
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, studentDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        BewerbungDTO bewerbungDTO;
        try {
            while (true) {
                assert rs != null;
                if (!rs.next()) break;
                int id = rs.getInt(1);
                String text = rs.getString(2);
                bewerbungDTO = BewerbungDTOFactory.createBewerbungDTO(id, text);
                list.add(bewerbungDTO);

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

    public boolean createBewerbung(String text, StudentDTO studentDTO) {
        String sql = "INSERT INTO collhbrs.bewerbung (id, freitext) " +
                "VALUES (?, ?); ";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, studentDTO.getId());
            statement.setString(2, text);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public boolean deleteBewerbung(BewerbungDTO bewerbungDTO) {
        String sql = "DELETE " +
                "FROM collhbrs.bewerbung " +
                "WHERE id_bewerbung = ?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, bewerbungDTO.getId());
            statement.executeUpdate();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger((BewerbungDAO.class.getName())).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

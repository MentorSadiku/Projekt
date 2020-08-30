package org.carlook.model.dao;

import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VertrieblerDAO extends AbstractDAO {

    private static VertrieblerDAO dao = null;

    private VertrieblerDAO() {

    }

    public static VertrieblerDAO getInstance() {
        if (dao == null) {
            dao = new VertrieblerDAO();
        }
        return dao;
    }

    public boolean updateVertriebler(VertrieblerDTO vertrieblerDTO) {
        String sql = "UPDATE carlook.vertriebler " +
                "SET name= ?, email = ?, passwort = ?, stadt = ?" +
                "WHERE carlook.vertriebler.id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, vertrieblerDTO.getName());
            statement.setString(2, vertrieblerDTO.getEmail());
            statement.setString(3, vertrieblerDTO.getPassword());
            statement.setString(4, vertrieblerDTO.getStadt());
            statement.setInt(5, vertrieblerDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public VertrieblerDTO getAllDataVertriebler(UserDTO userDTO) throws SQLException {
        String sql = "SELECT * " +
                "FROM carlook.unternehmen " +
                "WHERE carlook.unternehmen.id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs;

        try {
            statement.setInt(1,userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((VertrieblerDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }
        VertrieblerDTO un = new VertrieblerDTO(userDTO);
        try {
            while (rs.next()) {

                un.setName(rs.getString(2));
                un.setEmail(rs.getString(3));
                un.setPassword(rs.getString(4));
                un.setStadt(rs.getString(5));


            }

        } catch (SQLException ex) {
            Logger.getLogger((VertrieblerDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            rs.close();
        }
        return un;
    }
}

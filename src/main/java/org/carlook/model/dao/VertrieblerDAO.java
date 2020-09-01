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
        String sql = "UPDATE carlook.user " +
                "SET email = ?, name= ? " +
                "WHERE carlook.user.id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, vertrieblerDTO.getEmail());
            statement.setString(2, vertrieblerDTO.getName());
            statement.setInt(3, vertrieblerDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public VertrieblerDTO getAllDataVertriebler(UserDTO userDTO) throws SQLException {
        String sql = "SELECT * " +
                "FROM carlook.vertriebler " +
                "WHERE carlook.vertriebler.vertriebler_id = ? ;";
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

                un.setName(rs.getString(3));
                un.setEmail(rs.getString(1));
                un.setPassword(rs.getString(2));
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

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

    public boolean updateUnternehmen(VertrieblerDTO vertrieblerDTO) {
        String sql = "UPDATE collhbrs.unternehmen " +
                "SET name_unternehmen = ?, ansprechpartner = ?, strasse = ?, plz = ?, " +
                "hausnr = ?, zusatz = ?, ort = ?, branche = ? " +
                "WHERE collhbrs.unternehmen.id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, vertrieblerDTO.getName());
           /* statement.setString(2, vertrieblerDTO.getAnsprechpartner());
            statement.setString(3, vertrieblerDTO.getStrasse());
            statement.setInt(4, vertrieblerDTO.getPlz());
            statement.setInt(5, vertrieblerDTO.getHaus_nr());
            statement.setString(6, vertrieblerDTO.getZusatz());
            statement.setString(7, vertrieblerDTO.getOrt());
            statement.setString(8, vertrieblerDTO.getBranche());
            */statement.setInt(9, vertrieblerDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public VertrieblerDTO getAllDataUnternehmen(UserDTO userDTO) throws SQLException {
        String sql = "SELECT * " +
                "FROM collhbrs.unternehmen " +
                "WHERE collhbrs.unternehmen.id = ? ;";
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
                /*un.setAnsprechpartner(rs.getString(3));
                un.setStrasse(rs.getString(4));
                un.setPlz(rs.getInt(5));
                un.setHaus_nr(rs.getInt(6));
                un.setZusatz(rs.getString(7));
                un.setOrt(rs.getString(8));
                un.setBranche(rs.getString(9));
                */

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

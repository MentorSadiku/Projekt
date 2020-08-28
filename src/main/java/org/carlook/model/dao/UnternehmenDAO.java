package org.carlook.model.dao;

import org.carlook.model.objects.dto.UnternehmenDTO;
import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnternehmenDAO extends AbstractDAO {

    private static UnternehmenDAO dao = null;

    private UnternehmenDAO() {

    }

    public static UnternehmenDAO getInstance() {
        if (dao == null) {
            dao = new UnternehmenDAO();
        }
        return dao;
    }

    public boolean updateUnternehmen(UnternehmenDTO unternehmenDTO) {
        String sql = "UPDATE collhbrs.unternehmen " +
                "SET name_unternehmen = ?, ansprechpartner = ?, strasse = ?, plz = ?, " +
                "hausnr = ?, zusatz = ?, ort = ?, branche = ? " +
                "WHERE collhbrs.unternehmen.id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, unternehmenDTO.getName());
            statement.setString(2, unternehmenDTO.getAnsprechpartner());
            statement.setString(3, unternehmenDTO.getStrasse());
            statement.setInt(4, unternehmenDTO.getPlz());
            statement.setInt(5, unternehmenDTO.getHaus_nr());
            statement.setString(6, unternehmenDTO.getZusatz());
            statement.setString(7, unternehmenDTO.getOrt());
            statement.setString(8, unternehmenDTO.getBranche());
            statement.setInt(9, unternehmenDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public UnternehmenDTO getAllDataUnternehmen(UserDTO userDTO) throws SQLException {
        String sql = "SELECT * " +
                "FROM collhbrs.unternehmen " +
                "WHERE collhbrs.unternehmen.id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs;

        try {
            statement.setInt(1,userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((UnternehmenDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }
        UnternehmenDTO un = new UnternehmenDTO(userDTO);
        try {
            while (rs.next()) {

                un.setName(rs.getString(2));
                un.setAnsprechpartner(rs.getString(3));
                un.setStrasse(rs.getString(4));
                un.setPlz(rs.getInt(5));
                un.setHaus_nr(rs.getInt(6));
                un.setZusatz(rs.getString(7));
                un.setOrt(rs.getString(8));
                un.setBranche(rs.getString(9));
            }

        } catch (SQLException ex) {
            Logger.getLogger((UnternehmenDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            rs.close();
        }
        return un;
    }
}

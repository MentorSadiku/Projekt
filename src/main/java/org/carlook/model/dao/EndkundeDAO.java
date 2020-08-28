package org.carlook.model.dao;

import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndkundeDAO extends AbstractDAO {

    private static EndkundeDAO dao = null;

    private EndkundeDAO() {

    }

    public static EndkundeDAO getInstance() {
        if (dao == null) {
            dao = new EndkundeDAO();
        }
        return dao;
    }

    public boolean updateStudent(EndkundeDTO endkundeDTO) {
        String sql = "UPDATE collhbrs.student " +
                "SET anrede = ?, vorname = ?, name = ?, hochschule = ?, " +
                "semester = ?, gebdatum = ?, kenntnisse = ?, studiengang = ?" +
                "WHERE collhbrs.student.id = ?;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            //statement.setString(1, endkundeDTO.getAnrede());
           // statement.setString(2, endkundeDTO.getVorname());
            statement.setString(3, endkundeDTO.getName());
            /*statement.setString(4, endkundeDTO.getHochschule());
            statement.setInt(5, endkundeDTO.getSemester());
            statement.setObject(6, endkundeDTO.getGebDatum());
            statement.setString(7, endkundeDTO.getKenntnisse());
            statement.setString(8, endkundeDTO.getStudiengang());
            */statement.setInt(9, endkundeDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public EndkundeDTO getAllDataStudent(UserDTO userDTO) throws SQLException {
        String sql = "SELECT * " +
                "FROM collhbrs.student " +
                "WHERE collhbrs.student.id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs;
        try {
            statement.setInt(1,userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((EndkundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
            while (rs.next()) {
                //endkundeDTO.setAnrede(rs.getString(2));
                //endkundeDTO.setVorname(rs.getString(3));
                endkundeDTO.setName(rs.getString(4));
                /*endkundeDTO.setHochschule(rs.getString(5));
                endkundeDTO.setSemester(rs.getInt(6));
                endkundeDTO.setGebDatum(rs.getDate(7).toLocalDate());
                endkundeDTO.setKenntnisse(rs.getString(8));
                endkundeDTO.setStudiengang(rs.getString(9));

                 */
            }
            return endkundeDTO;
        } catch (SQLException ex) {
            Logger.getLogger((EndkundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            rs.close();
        }


    }

}

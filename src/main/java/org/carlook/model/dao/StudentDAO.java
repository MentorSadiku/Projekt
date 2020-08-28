package org.carlook.model.dao;

import org.carlook.model.objects.dto.StudentDTO;
import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAO extends AbstractDAO {

    private static StudentDAO dao = null;

    private StudentDAO() {

    }

    public static StudentDAO getInstance() {
        if (dao == null) {
            dao = new StudentDAO();
        }
        return dao;
    }

    public boolean updateStudent(StudentDTO studentDTO) {
        String sql = "UPDATE collhbrs.student " +
                "SET anrede = ?, vorname = ?, name = ?, hochschule = ?, " +
                "semester = ?, gebdatum = ?, kenntnisse = ?, studiengang = ?" +
                "WHERE collhbrs.student.id = ?;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, studentDTO.getAnrede());
            statement.setString(2, studentDTO.getVorname());
            statement.setString(3, studentDTO.getName());
            statement.setString(4, studentDTO.getHochschule());
            statement.setInt(5, studentDTO.getSemester());
            statement.setObject(6, studentDTO.getGebDatum());
            statement.setString(7, studentDTO.getKenntnisse());
            statement.setString(8, studentDTO.getStudiengang());
            statement.setInt(9, studentDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public StudentDTO getAllDataStudent(UserDTO userDTO) throws SQLException {
        String sql = "SELECT * " +
                "FROM collhbrs.student " +
                "WHERE collhbrs.student.id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs;
        try {
            statement.setInt(1,userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((StudentDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            StudentDTO studentDTO = new StudentDTO(userDTO);
            while (rs.next()) {
                studentDTO.setAnrede(rs.getString(2));
                studentDTO.setVorname(rs.getString(3));
                studentDTO.setName(rs.getString(4));
                studentDTO.setHochschule(rs.getString(5));
                studentDTO.setSemester(rs.getInt(6));
                studentDTO.setGebDatum(rs.getDate(7).toLocalDate());
                studentDTO.setKenntnisse(rs.getString(8));
                studentDTO.setStudiengang(rs.getString(9));
            }
            return studentDTO;
        } catch (SQLException ex) {
            Logger.getLogger((StudentDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            rs.close();
        }


    }

}

package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.BewerbungDTO;
import org.carlook.model.objects.dto.StudentDTO;
import org.carlook.model.objects.dto.UnternehmenDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;
import java.util.List;

public interface ProfileControlInterface {

    void updateStudentData(StudentDTO studentDTO) throws ProfileException;

    void updateUnternehmenData(UnternehmenDTO unternehmenDTO) throws ProfileException;

    StudentDTO getStudent(UserDTO userDTO) throws SQLException;

    UnternehmenDTO getUnternehmen(UserDTO userDTO) throws SQLException;

    void setBewerbung(String text, StudentDTO studentDTO) throws ProfileException;

    List<BewerbungDTO> getBewerbung(StudentDTO studentDTO) throws SQLException;
}

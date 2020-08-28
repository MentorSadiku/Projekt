package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.BewerbungDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;
import java.util.List;

public interface ProfileControlInterface {

    void updateStudentData(EndkundeDTO endkundeDTO) throws ProfileException;

    void updateUnternehmenData(VertrieblerDTO vertrieblerDTO) throws ProfileException;

    EndkundeDTO getStudent(UserDTO userDTO) throws SQLException;

    VertrieblerDTO getUnternehmen(UserDTO userDTO) throws SQLException;

    void setBewerbung(String text, EndkundeDTO endkundeDTO) throws ProfileException;

    List<BewerbungDTO> getBewerbung(EndkundeDTO endkundeDTO) throws SQLException;
}

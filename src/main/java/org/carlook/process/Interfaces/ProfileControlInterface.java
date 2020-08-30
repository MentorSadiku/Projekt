package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;
import java.util.List;

public interface ProfileControlInterface {

    void updateEndkundeData(EndkundeDTO endkundeDTO) throws ProfileException;

    void updateVertrieblerData(VertrieblerDTO vertrieblerDTO) throws ProfileException;

    EndkundeDTO getEndkunde(UserDTO userDTO) throws SQLException;

    VertrieblerDTO getVertriebler(UserDTO userDTO) throws SQLException;

    void setReservierung(EndkundeDTO endkundeDTO) throws ProfileException;

    List<ReservierungDTO> getReservierung(EndkundeDTO endkundeDTO) throws SQLException;
}

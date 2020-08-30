package org.carlook.process.Interfaces;

import com.vaadin.ui.Button;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.process.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

public interface ReservierungControlInterface {

    int getLatestReservation(UserDTO userDTO) throws DatabaseException, SQLException;

    void reserveACar(AutoDTO autoDTO, int id_bewerbung) throws DatabaseException;

    void reservingIsAllowed() throws DatabaseException, ReservierungException, SQLException;

    void checkAlreadyReserved(AutoDTO autoDTO, UserDTO userDTO) throws ReservierungException, DatabaseException, SQLException;

    void checkAllowed(AutoDTO autoDTO, UserDTO userDTO, Button bewerbenButton);

    void createReservation(UserDTO userDTO) throws ReservierungException;

    ReservierungDTO getReservationForAuto(AutoDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException;

    List<ReservierungDTO> getReservationForEndkunde(EndkundeDTO endkundeDTO) throws SQLException;

    void deleteReservierung(ReservierungDTO reservierungDTO) throws ReservierungException;
}

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

public interface BewerbungControlInterface {

    int getLatestApply(UserDTO userDTO) throws DatabaseException, SQLException;

    void applyForStellenanzeige(AutoDTO stellenanzeige, int id_bewerbung) throws DatabaseException;

    void applyingIsAllowed() throws DatabaseException, ReservierungException, SQLException;

    void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws ReservierungException, DatabaseException, SQLException;

    void checkAllowed(AutoDTO stellenanzeige, UserDTO userDTO, Button bewerbenButton);

    void createBewerbung(String bewerbungstext, UserDTO userDTO) throws ReservierungException;

    ReservierungDTO getBewerbungForStellenanzeige(AutoDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException;

    List<ReservierungDTO> getBewerbungenForStudent(EndkundeDTO endkundeDTO) throws SQLException;

    void deleteBewerbung(ReservierungDTO reservierungDTO) throws ReservierungException;
}

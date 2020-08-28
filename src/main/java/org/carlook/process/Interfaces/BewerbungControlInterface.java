package org.carlook.process.Interfaces;

import com.vaadin.ui.Button;
import org.carlook.model.objects.dto.BewerbungDTO;
import org.carlook.model.objects.dto.StellenanzeigeDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.BewerbungException;
import org.carlook.process.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

public interface BewerbungControlInterface {

    int getLatestApply(UserDTO userDTO) throws DatabaseException, SQLException;

    void applyForStellenanzeige(StellenanzeigeDTO stellenanzeige, int id_bewerbung) throws DatabaseException;

    void applyingIsAllowed() throws DatabaseException, BewerbungException, SQLException;

    void checkAlreadyApplied(StellenanzeigeDTO stellenanzeigeDTO, UserDTO userDTO) throws BewerbungException, DatabaseException, SQLException;

    void checkAllowed(StellenanzeigeDTO stellenanzeige, UserDTO userDTO, Button bewerbenButton);

    void createBewerbung(String bewerbungstext, UserDTO userDTO) throws BewerbungException;

    BewerbungDTO getBewerbungForStellenanzeige(StellenanzeigeDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException;

    List<BewerbungDTO> getBewerbungenForStudent(EndkundeDTO endkundeDTO) throws SQLException;

    void deleteBewerbung(BewerbungDTO bewerbungDTO) throws BewerbungException;
}

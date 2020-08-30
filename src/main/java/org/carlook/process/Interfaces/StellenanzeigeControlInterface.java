package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.StellenanzeigeException;

import java.sql.SQLException;
import java.util.List;

public interface StellenanzeigeControlInterface {

    List<AutoDTO> getAnzeigenForUnternehmen(VertrieblerDTO vertrieblerDTO) throws SQLException;

    List<AutoDTO> getAnzeigenForStudent(EndkundeDTO endkundeDTO) throws SQLException;

    void createStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException;

    void updateStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException;

    void deleteStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException;

    List<AutoDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException;

    int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException;
}

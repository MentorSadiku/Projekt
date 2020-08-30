package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.StellenanzeigeException;

import java.sql.SQLException;
import java.util.List;

public interface StellenanzeigeControlInterface {

    List<AutoDTO> getAutoForVertriebler(VertrieblerDTO vertrieblerDTO) throws SQLException;

    List<AutoDTO> getAutoForEndkunde(EndkundeDTO endkundeDTO) throws SQLException;

    void createAuto(AutoDTO autoDTO) throws StellenanzeigeException;

    void updateAuto(AutoDTO autoDTO) throws StellenanzeigeException;

    void deleteAuto(AutoDTO autoDTO) throws StellenanzeigeException;

    List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException;

    int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException;
}

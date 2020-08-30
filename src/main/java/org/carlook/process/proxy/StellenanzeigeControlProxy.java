package org.carlook.process.proxy;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.Interfaces.StellenanzeigeControlInterface;
import org.carlook.process.control.AutoControl;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.AutoException;

import java.sql.SQLException;
import java.util.List;

public class StellenanzeigeControlProxy implements StellenanzeigeControlInterface {
    private static StellenanzeigeControlProxy search = null;

    public static StellenanzeigeControlProxy getInstance() {
        if (search == null) {
            search = new StellenanzeigeControlProxy();
        }
        return search;
    }

    private StellenanzeigeControlProxy() {

    }

    public List<AutoDTO> getAutoForVertriebler(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return AutoControl.getInstance().getAutoForVertriebler(vertrieblerDTO);
    }

    public List<AutoDTO> getAutoForEndkunde(EndkundeDTO endkundeDTO) throws SQLException {
        return AutoControl.getInstance().getAutoForEndkunde(endkundeDTO);
    }
    public void createAuto(AutoDTO autoDTO) throws AutoException {
        AutoControl.getInstance().createAuto(autoDTO);
    }
    public void updateAuto(AutoDTO autoDTO) throws AutoException {
        AutoControl.getInstance().updateAuto(autoDTO);
    }

    public void deleteAuto(AutoDTO autoDTO) throws AutoException {
        AutoControl.getInstance().deleteAuto(autoDTO);
    }

    public List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException {
        return AutoControl.getInstance().getAutoForSearch(suchtext, filter);
    }

    public int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException {
        return AutoControl.getInstance().getAnzahlRes(autoDTO);
    }
}

package org.carlook.process.proxy;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.Interfaces.StellenanzeigeControlInterface;
import org.carlook.process.control.StellenanzeigeControl;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.StellenanzeigeException;

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

    public List<AutoDTO> getAnzeigenForUnternehmen(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return StellenanzeigeControl.getInstance().getAnzeigenForUnternehmen(vertrieblerDTO);
    }

    public List<AutoDTO> getAnzeigenForStudent(EndkundeDTO endkundeDTO) throws SQLException {
        return StellenanzeigeControl.getInstance().getAnzeigenForStudent(endkundeDTO);
    }
    public void createStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException {
        StellenanzeigeControl.getInstance().createStellenanzeige(autoDTO);
    }
    public void updateStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException {
        StellenanzeigeControl.getInstance().updateStellenanzeige(autoDTO);
    }

    public void deleteStellenanzeige(AutoDTO autoDTO) throws StellenanzeigeException {
        StellenanzeigeControl.getInstance().deleteStellenanzeige(autoDTO);
    }

    public List<AutoDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException {
        return StellenanzeigeControl.getInstance().getAnzeigenForSearch(suchtext, filter);
    }

    public int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException {
        return StellenanzeigeControl.getInstance().getAnzahlRes(autoDTO);
    }
}

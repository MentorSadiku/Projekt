package org.carlook.process.proxy;

import org.carlook.model.objects.dto.StellenanzeigeDTO;
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

    public List<StellenanzeigeDTO> getAnzeigenForUnternehmen(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return StellenanzeigeControl.getInstance().getAnzeigenForUnternehmen(vertrieblerDTO);
    }

    public List<StellenanzeigeDTO> getAnzeigenForStudent(EndkundeDTO endkundeDTO) throws SQLException {
        return StellenanzeigeControl.getInstance().getAnzeigenForStudent(endkundeDTO);
    }
    public void createStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO) throws StellenanzeigeException {
        StellenanzeigeControl.getInstance().createStellenanzeige(stellenanzeigeDTO);
    }
    public void updateStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO) throws StellenanzeigeException {
        StellenanzeigeControl.getInstance().updateStellenanzeige(stellenanzeigeDTO);
    }

    public void deleteStellenanzeige(StellenanzeigeDTO stellenanzeigeDTO) throws StellenanzeigeException {
        StellenanzeigeControl.getInstance().deleteStellenanzeige(stellenanzeigeDTO);
    }

    public List<StellenanzeigeDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException {
        return StellenanzeigeControl.getInstance().getAnzeigenForSearch(suchtext, filter);
    }

    public int getAnzahlBewerber(StellenanzeigeDTO stellenanzeigeDTO) throws DatabaseException, SQLException {
        return StellenanzeigeControl.getInstance().getAnzahlBewerber(stellenanzeigeDTO);
    }
}

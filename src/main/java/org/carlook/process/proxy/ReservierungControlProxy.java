package org.carlook.process.proxy;

import com.vaadin.ui.Button;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.BewerbungControlInterface;
import org.carlook.process.control.ReservierungControl;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.process.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

public class ReservierungControlProxy implements BewerbungControlInterface {
    private static ReservierungControlProxy bewerbungControl = null;

    private ReservierungControlProxy() {

    }

    public static ReservierungControlProxy getInstance() {
        if (bewerbungControl == null) {
            bewerbungControl = new ReservierungControlProxy();
        }
        return bewerbungControl;
    }

    public int getLatestApply(UserDTO userDTO) throws DatabaseException, SQLException {
        return ReservierungControl.getInstance().getLatestApply(userDTO);
    }

    public void applyForStellenanzeige(AutoDTO stellenanzeige, int id_bewerbung) throws DatabaseException {
        ReservierungControl.getInstance().applyForStellenanzeige(stellenanzeige, id_bewerbung);
    }

    public void applyingIsAllowed() throws DatabaseException, ReservierungException, SQLException {
        ReservierungControl.getInstance().applyingIsAllowed();
    }

    public void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws ReservierungException, DatabaseException, SQLException {
        ReservierungControl.getInstance().checkAlreadyApplied(autoDTO, userDTO);

    }
    public void checkAllowed(AutoDTO stellenanzeige, UserDTO userDTO, Button bewerbenButton) {
        ReservierungControl.getInstance().checkAllowed(stellenanzeige, userDTO, bewerbenButton);
    }

    public void createBewerbung( UserDTO userDTO) throws ReservierungException {
        ReservierungControl.getInstance().createBewerbung(userDTO);
    }

    public ReservierungDTO getBewerbungForStellenanzeige(AutoDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException {
        return ReservierungControl.getInstance().getBewerbungForStellenanzeige(selektiert, endkundeDTO);
    }

    public List<ReservierungDTO> getBewerbungenForStudent(EndkundeDTO endkundeDTO) throws SQLException {
        return ReservierungControl.getInstance().getBewerbungenForStudent(endkundeDTO);
    }

    public void deleteBewerbung(ReservierungDTO reservierungDTO) throws ReservierungException {
        ReservierungControl.getInstance().deleteBewerbung(reservierungDTO);
    }
}

package org.carlook.process.proxy;

import com.vaadin.ui.Button;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.ReservierungControlInterface;
import org.carlook.process.control.ReservierungControl;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.process.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

public class ReservierungControlProxy implements ReservierungControlInterface {
    private static ReservierungControlProxy bewerbungControl = null;

    private ReservierungControlProxy() {

    }

    public static ReservierungControlProxy getInstance() {
        if (bewerbungControl == null) {
            bewerbungControl = new ReservierungControlProxy();
        }
        return bewerbungControl;
    }

    public int getLatestReservation(UserDTO userDTO) throws DatabaseException, SQLException {
        return ReservierungControl.getInstance().getLatestReservation(userDTO);
    }

    public void reserveACar(AutoDTO autoDTO, int id_bewerbung) throws DatabaseException {
        ReservierungControl.getInstance().reserveACar(autoDTO, id_bewerbung);
    }

    /*public void reservingIsAllowed() throws DatabaseException, ReservierungException, SQLException {
        ReservierungControl.getInstance().reservingIsAllowed();
    }*/

    public void checkAlreadyReserved(AutoDTO autoDTO, UserDTO userDTO) throws ReservierungException, DatabaseException, SQLException {
        ReservierungControl.getInstance().checkAlreadyReserved(autoDTO, userDTO);

    }
    public void checkAllowed(AutoDTO autoDTO, UserDTO userDTO, Button bewerbenButton) {
        ReservierungControl.getInstance().checkAllowed(autoDTO, userDTO, bewerbenButton);
    }

    public void createReservation(UserDTO userDTO) throws ReservierungException {
        ReservierungControl.getInstance().createReservation(userDTO);
    }

    public ReservierungDTO getReservationForAuto(AutoDTO selektiert, EndkundeDTO endkundeDTO) throws SQLException, DatabaseException {
        return ReservierungControl.getInstance().getReservationForAuto(selektiert, endkundeDTO);
    }

    public List<ReservierungDTO> getReservationForEndkunde(EndkundeDTO endkundeDTO) throws SQLException {
        return ReservierungControl.getInstance().getReservationForEndkunde(endkundeDTO);
    }

    public void deleteReservierung(ReservierungDTO reservierungDTO) throws ReservierungException {
        ReservierungControl.getInstance().deleteReservierung(reservierungDTO);
    }
}

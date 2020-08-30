package org.carlook.process.control;

import org.carlook.model.dao.ReservierungDAO;
import org.carlook.model.dao.EndkundeDAO;
import org.carlook.model.dao.VertrieblerDAO;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.ProfileControlInterface;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;
import java.util.List;

public class ProfileControl implements ProfileControlInterface {
    private static ProfileControl profileControl = null;

    private ProfileControl() {
    }

    public static ProfileControl getInstance() {
        if (profileControl == null) {
            profileControl = new ProfileControl();
        }
        return profileControl;
    }


    public void updateEndkundeData(EndkundeDTO endkundeDTO) throws ProfileException {
        boolean result =  EndkundeDAO.getInstance().updateEndkunde(endkundeDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public void updateVertrieblerData(VertrieblerDTO vertrieblerDTO) throws ProfileException {
        boolean result = VertrieblerDAO.getInstance().updateVertriebler(vertrieblerDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public EndkundeDTO getEndkunde(UserDTO userDTO) throws SQLException {
        return EndkundeDAO.getInstance().getAllDataEndkunde(userDTO);
    }

    public VertrieblerDTO getVertriebler(UserDTO userDTO) throws SQLException {
        return VertrieblerDAO.getInstance().getAllDataVertriebler(userDTO);
    }

    public void setReservierung(String text, EndkundeDTO endkundeDTO) throws ProfileException {
        boolean result =  ReservierungDAO.getInstance().createReservierung(endkundeDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public List<ReservierungDTO> getReservierung(EndkundeDTO endkundeDTO) throws SQLException {
        return ReservierungDAO.getInstance().getReservierungForEndkunde(endkundeDTO);
    }
}

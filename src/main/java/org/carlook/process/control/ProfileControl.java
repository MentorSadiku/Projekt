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


    public void updateStudentData(EndkundeDTO endkundeDTO) throws ProfileException {
        boolean result =  EndkundeDAO.getInstance().updateEndkunde(endkundeDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public void updateUnternehmenData(VertrieblerDTO vertrieblerDTO) throws ProfileException {
        boolean result = VertrieblerDAO.getInstance().updateVertriebler(vertrieblerDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public EndkundeDTO getStudent(UserDTO userDTO) throws SQLException {
        return EndkundeDAO.getInstance().getAllDataEndkunde(userDTO);
    }

    public VertrieblerDTO getUnternehmen(UserDTO userDTO) throws SQLException {
        return VertrieblerDAO.getInstance().getAllDataVertriebler(userDTO);
    }

    public void setBewerbung(String text, EndkundeDTO endkundeDTO) throws ProfileException {
        boolean result =  ReservierungDAO.getInstance().createReservierung(text, endkundeDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public List<ReservierungDTO> getBewerbung(EndkundeDTO endkundeDTO) throws SQLException {
        return ReservierungDAO.getInstance().getReservierungForEndkunde(endkundeDTO);
    }
}

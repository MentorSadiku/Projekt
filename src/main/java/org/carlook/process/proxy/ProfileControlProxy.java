package org.carlook.process.proxy;

import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.ProfileControlInterface;
import org.carlook.process.control.ProfileControl;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;
import java.util.List;

public class ProfileControlProxy implements ProfileControlInterface {
    private static ProfileControlProxy profileControl = null;

    private ProfileControlProxy() {
    }

    public static ProfileControlProxy getInstance() {
        if (profileControl == null) {
            profileControl = new ProfileControlProxy();
        }
        return profileControl;
    }


    public void updateStudentData(EndkundeDTO endkundeDTO) throws ProfileException {
        ProfileControl.getInstance().updateStudentData(endkundeDTO);
    }

    public void updateUnternehmenData(VertrieblerDTO vertrieblerDTO) throws ProfileException {
        ProfileControl.getInstance().updateUnternehmenData(vertrieblerDTO);
    }

    public EndkundeDTO getStudent(UserDTO userDTO) throws SQLException {
        return ProfileControl.getInstance().getStudent(userDTO);
    }

    public VertrieblerDTO getUnternehmen(UserDTO userDTO) throws SQLException {
        return ProfileControl.getInstance().getUnternehmen(userDTO);
    }

    public void setBewerbung(String text, EndkundeDTO endkundeDTO) throws ProfileException {
        ProfileControl.getInstance().setBewerbung(text, endkundeDTO);
    }

    public List<ReservierungDTO> getBewerbung(EndkundeDTO endkundeDTO) throws SQLException {
        return ProfileControl.getInstance().getBewerbung(endkundeDTO);
    }
}

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


    public void updateEndkundeData(EndkundeDTO endkundeDTO) throws ProfileException {
        ProfileControl.getInstance().updateEndkundeData(endkundeDTO);
    }

    public void updateVertrieblerData(VertrieblerDTO vertrieblerDTO) throws ProfileException {
        ProfileControl.getInstance().updateVertrieblerData(vertrieblerDTO);
    }

    public EndkundeDTO getEndkunde(UserDTO userDTO) throws SQLException {
        return ProfileControl.getInstance().getEndkunde(userDTO);
    }

    public VertrieblerDTO getVertriebler(UserDTO userDTO) throws SQLException {
        return ProfileControl.getInstance().getVertriebler(userDTO);
    }

    public void setReservierung(String text, EndkundeDTO endkundeDTO) throws ProfileException {
        ProfileControl.getInstance().setReservierung(text, endkundeDTO);
    }

    public List<ReservierungDTO> getReservierung(EndkundeDTO endkundeDTO) throws SQLException {
        return ProfileControl.getInstance().getReservierung(endkundeDTO);
    }
}

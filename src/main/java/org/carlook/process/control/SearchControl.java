package org.carlook.process.control;

import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.SearchControlInterface;
import org.carlook.services.util.Roles;

import java.sql.SQLException;
import java.util.List;

public class SearchControl implements SearchControlInterface {
    private static SearchControl search = null;

    public static SearchControl getInstance() {
        if (search == null) {
            search = new SearchControl();
        }
        return search;
    }

    private SearchControl() {

    }

    public List<AutoDTO> getAnzeigenForUser() throws SQLException {
        UserDTO userDTO = ( (MyUI)UI.getCurrent() ).getUserDTO();
        if (userDTO.hasRole(Roles.STUDENT)) {
            EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
            return StellenanzeigeControl.getInstance().getAnzeigenForStudent(endkundeDTO);
        }
        VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
        return StellenanzeigeControl.getInstance().getAnzeigenForUnternehmen(vertrieblerDTO);
    }

    public List<AutoDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException {
        if (filter == null) {
            filter = "name";
        }
        return StellenanzeigeControl.getInstance().getAnzeigenForSearch(suchtext, filter);
    }
}

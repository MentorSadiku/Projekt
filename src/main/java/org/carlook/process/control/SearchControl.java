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

    public List<AutoDTO> getAutoForUser() throws SQLException {
        UserDTO userDTO = ( (MyUI)UI.getCurrent() ).getUserDTO();
        if (userDTO.hasRole(Roles.ENDKUNDE)) {
            EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
            return AutoControl.getInstance().getAutoForEndkunde(endkundeDTO);
        }
        VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
        return AutoControl.getInstance().getAutoForVertriebler(vertrieblerDTO);
    }

    public List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException {
        if (filter == null) {
            filter = "marke";
        }
        return AutoControl.getInstance().getAutoForSearch(suchtext, filter);
    }
}

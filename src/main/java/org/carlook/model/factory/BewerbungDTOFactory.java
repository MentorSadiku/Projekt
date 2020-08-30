package org.carlook.model.factory;

import org.carlook.model.objects.dto.ReservierungDTO;

public class BewerbungDTOFactory {

    public static ReservierungDTO createBewerbungDTO(int id, String text) {
        ReservierungDTO reservierungDTO = new ReservierungDTO();
        reservierungDTO.setId(id);
        reservierungDTO.setFreitext(text);
        return reservierungDTO;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Dieses Object kann nicht geclont werden!");
    }
}

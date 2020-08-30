package org.carlook.model.factory;

import org.carlook.model.objects.dto.ReservierungDTO;

public class ReservierungDTOFactory {

    public static ReservierungDTO createReservierungDTO(int id) {
        ReservierungDTO reservierungDTO = new ReservierungDTO();
        reservierungDTO.setId(id);
        return reservierungDTO;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Dieses Object kann nicht geclont werden!");
    }
}

package org.carlook.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.process.exceptions.BewerbungException;
import org.carlook.process.proxy.BewerbungControlProxy;
import org.carlook.services.util.Views;

public class DeleteReservierungWindow extends DeleteWindow {
    //Window zum Löschen von Bewerbungen auf Stellenanzeigen

    public DeleteReservierungWindow(ReservierungDTO reservierungDTO) {
        this.setText("Sind Sie sicher, dass Sie Die Reservierung auf dieses Auto rückgängig machen wollen? <br>" +
                "Dieser Vorgang ist unumkehrbar!");
        this.setDto(reservierungDTO);
        this.setListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    BewerbungControlProxy.getInstance().deleteBewerbung(reservierungDTO);
                } catch (BewerbungException e) {
                    Notification.show("DB-Fehler", "Löschen war nicht erfolgreich!", Notification.Type.ERROR_MESSAGE);
                }
                Notification.show("Reservierung gelöscht!", Notification.Type.HUMANIZED_MESSAGE);
                UI.getCurrent().getNavigator().navigateTo(Views.BEWERBUNG);
                for (Window w : UI.getCurrent().getWindows()) {
                    w.close();
                }
            }
        });
    }
}


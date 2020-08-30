package org.carlook.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.process.exceptions.StellenanzeigeException;
import org.carlook.process.proxy.StellenanzeigeControlProxy;
import org.carlook.services.util.Views;

public class DeleteAutoWindow extends DeleteWindow{
    //Window zum Löschen von Stellenanzeigen

    public DeleteAutoWindow(AutoDTO autoDTO) {
        this.setText("Sind Sie sicher, dass Sie das Auto entfernen wollen? <br>" +
                "Dieser Vorgang ist unumkehrbar!");
        this.setDto(autoDTO);
        this.setListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    StellenanzeigeControlProxy.getInstance().deleteAuto(autoDTO);
                } catch (StellenanzeigeException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().getNavigator().navigateTo(Views.STELLENANZEIGE);
                for (Window w : UI.getCurrent().getWindows()) {
                    w.close();
                }
            }
        });
    }
}


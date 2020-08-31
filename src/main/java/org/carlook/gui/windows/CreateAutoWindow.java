package org.carlook.gui.windows;

import com.vaadin.ui.*;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.AutoException;
import org.carlook.process.proxy.AutoControlProxy;

import java.sql.SQLException;
import java.util.List;

public class CreateAutoWindow extends Window {

    public CreateAutoWindow(AutoDTO stellenanzeige, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super("Ihre Autos");
        center();

        //Art
        TextField marke = new TextField("Automarke");
        marke.setValue(stellenanzeige.getMarke());

        //Baujahr
        TextField baujahr = new TextField("Baujahr");
        int x=stellenanzeige.getBaujahr();
        String s=String.valueOf(x);
        baujahr.setValue(s);

        //Beschreibung
        TextArea beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(stellenanzeige.getBeschreibung());


        //saveButton Config
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                stellenanzeige.setMarke(marke.getValue());
                stellenanzeige.setBaujahr(x);
                stellenanzeige.setBeschreibung(beschreibung.getValue());

                try {
                    AutoControlProxy.getInstance().createAuto(stellenanzeige);
                } catch (AutoException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Auto erfolgreich gespeichert"));
                List<AutoDTO> list = null;
                try {
                    list = AutoControlProxy.getInstance().getAutoForVertriebler(vertrieblerDTO);
                } catch (SQLException e) {
                    Notification.show("15 Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
                }
                grid.setItems();
                grid.setItems(list);
                close();
            }
        });

        //abortButton Config
        Button abortButton = new Button("Abbrechen");
        abortButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(saveButton);
        horizontalLayout.addComponent(abortButton);

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(marke);
        verticalLayout.addComponent(baujahr);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        setContent(verticalLayout);
    }
}

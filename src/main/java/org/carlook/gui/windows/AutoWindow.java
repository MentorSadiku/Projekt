package org.carlook.gui.windows;

import com.vaadin.ui.*;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.StellenanzeigeException;
import org.carlook.process.proxy.BewerbungControlProxy;
import org.carlook.process.proxy.StellenanzeigeControlProxy;

import java.sql.SQLException;
import java.util.List;

public class AutoWindow extends Window {
    private TextField marke;
    private TextField baujahr;
    private TextArea beschreibung;

    public AutoWindow(AutoDTO auto, UserDTO userDTO) {

        super(auto.getMarke());
        center();

        //Marke
        marke = new TextField("Marke");
        marke.setValue(auto.getMarke());
        marke.setReadOnly(true);

        //Baujahr
        baujahr = new TextField("Baujahr");
        int x=auto.getBaujahr();
        String s=String.valueOf(x);
        baujahr.setValue(s);
        baujahr.setReadOnly(true);

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(auto.getBeschreibung());
        beschreibung.setReadOnly(true);

        //OkButton
        Button okButton = new Button("Ok");
        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        //BewerbenButton
        Button bewerbenButton = new Button("Reservieren");
        BewerbungControlProxy.getInstance().checkAllowed(auto, userDTO, bewerbenButton);
        bewerbenButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
            //   UI.getCurrent().addWindow(new FreitextWindow(auto, userDTO));

                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(bewerbenButton);

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout = this.buildVerticalLayout(verticalLayout, marke, baujahr, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }

    public AutoWindow(AutoDTO auto, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super(auto.getMarke());
        center();

        //Marke
        marke = new TextField("Marke");
        marke.setValue(auto.getMarke());

        //Baujahr
        baujahr = new TextField("Baujahr");
        int x=auto.getBaujahr();
        String s=String.valueOf(x);
        baujahr.setValue(s);

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(auto.getBeschreibung());

        //SaveButton
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                auto.setMarke(marke.getValue());
                auto.setBaujahr(x);
                auto.setBeschreibung(beschreibung.getValue());

                try {
                    StellenanzeigeControlProxy.getInstance().updateStellenanzeige(auto);
                } catch (StellenanzeigeException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Auto erfolgreich gespeichert"));
                List<AutoDTO> list = null;
                try {
                    list = StellenanzeigeControlProxy.getInstance().getAnzeigenForUnternehmen(vertrieblerDTO);
                } catch (SQLException e) {
                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
                }
                grid.setItems();
                grid.setItems(list);
                close();
            }
        });

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
        verticalLayout = this.buildVerticalLayout(verticalLayout, marke, baujahr, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }
    public VerticalLayout buildVerticalLayout(VerticalLayout verticalLayout, TextField marke, TextField baujahr,
                                               TextArea beschreibung, HorizontalLayout horizontalLayout ){
        verticalLayout.addComponent(marke);
        verticalLayout.addComponent(baujahr);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        return verticalLayout;
    }
}

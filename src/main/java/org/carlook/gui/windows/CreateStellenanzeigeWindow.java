package org.carlook.gui.windows;

import com.vaadin.ui.*;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.StellenanzeigeException;
import org.carlook.process.proxy.StellenanzeigeControlProxy;

import java.sql.SQLException;
import java.util.List;

public class CreateStellenanzeigeWindow extends Window {

    public CreateStellenanzeigeWindow(AutoDTO stellenanzeige, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super("Ihre Stellenanzeige");
        center();

        //Name
        TextField name = new TextField("Titel");
        name.setValue(stellenanzeige.getName());

        //Art
        TextField art = new TextField("Art der Anstellung");
        art.setValue(stellenanzeige.getMarke());

        //Branche
        TextField branche = new TextField("Branche");
        branche.setValue(stellenanzeige.getBranche());

        //Studiengang
        TextField studiengang = new TextField("Studiengang");
        studiengang.setValue(stellenanzeige.getStudiengang());

        //Ort
        TextField ort = new TextField("Ort");
        ort.setValue(stellenanzeige.getOrt());

        //Zeitraum
        DateField zeitraum = new DateField("Ende der Ausschreibung");
        zeitraum.setValue(stellenanzeige.getZeitraum());

        //Beschreibung
        TextArea beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(stellenanzeige.getBeschreibung());


        //saveButton Config
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                stellenanzeige.setName(name.getValue());
                stellenanzeige.setMarke(art.getValue());
                stellenanzeige.setBranche(branche.getValue());
                stellenanzeige.setStudiengang(studiengang.getValue());
                stellenanzeige.setOrt(ort.getValue());
                stellenanzeige.setZeitraum(zeitraum.getValue());
                stellenanzeige.setBeschreibung(beschreibung.getValue());

                try {
                    StellenanzeigeControlProxy.getInstance().createStellenanzeige(stellenanzeige);
                } catch (StellenanzeigeException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Stellenanzeige erfolgreich gespeichert"));
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
        verticalLayout.addComponent(name);
        verticalLayout.addComponent(art);
        verticalLayout.addComponent(branche);
        verticalLayout.addComponent(studiengang);
        verticalLayout.addComponent(ort);
        verticalLayout.addComponent(zeitraum);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        setContent(verticalLayout);
    }
}

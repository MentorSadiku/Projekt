package org.carlook.gui.windows;

import com.vaadin.ui.*;
import org.carlook.model.objects.dto.StellenanzeigeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.StellenanzeigeException;
import org.carlook.process.proxy.BewerbungControlProxy;
import org.carlook.process.proxy.StellenanzeigeControlProxy;

import java.sql.SQLException;
import java.util.List;

public class StellenanzeigeWindow extends Window {
    private TextField name;
    private TextField art;
    private TextField branche;
    private TextField studiengang;
    private TextField ort;
    private TextArea beschreibung;

    public StellenanzeigeWindow(StellenanzeigeDTO stellenanzeige, UserDTO userDTO) {
        super(stellenanzeige.getName());
        center();

        //Name
        name = new TextField("Titel");
        name.setValue(stellenanzeige.getName());
        name.setReadOnly(true);

        //Art
        art = new TextField("Art");
        art.setValue(stellenanzeige.getArt());
        art.setReadOnly(true);

        //Branche
        branche = new TextField("Branche");
        branche.setValue(stellenanzeige.getBranche());
        branche.setReadOnly(true);

        //Studiengang
        studiengang = new TextField("Studiengang");
        studiengang.setValue(stellenanzeige.getStudiengang());
        studiengang.setReadOnly(true);

        //Ort
        ort = new TextField("Ort");
        ort.setValue(stellenanzeige.getOrt());
        ort.setReadOnly(true);

        //Zeitraum
        DateField zeitraum = new DateField("Ende der Ausschreibung");
        zeitraum.setValue(stellenanzeige.getZeitraum());
        zeitraum.setReadOnly(true);

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(stellenanzeige.getBeschreibung());
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
        Button bewerbenButton = new Button("Bewerben");
        BewerbungControlProxy.getInstance().checkAllowed(stellenanzeige, userDTO, bewerbenButton);
        bewerbenButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new FreitextWindow(stellenanzeige, userDTO));
                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(bewerbenButton);

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout = this.buildVerticalLayout(verticalLayout, name, art, branche, studiengang, ort, zeitraum, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }

    public StellenanzeigeWindow(StellenanzeigeDTO stellenanzeige, Grid<StellenanzeigeDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super(stellenanzeige.getName());
        center();

        //Name
        name = new TextField("Titel");
        name.setValue(stellenanzeige.getName());

        //Art
        art = new TextField("Art");
        art.setValue(stellenanzeige.getArt());

        //Branche
        branche = new TextField("Branche");
        branche.setValue(stellenanzeige.getBranche());

        //Studiengang
        studiengang = new TextField("Studiengang");
        studiengang.setValue(stellenanzeige.getStudiengang());

        //Ort
        ort = new TextField("Ort");
        ort.setValue(stellenanzeige.getOrt());

        //Zeitraum
        DateField zeitraum = new DateField("Ende der Ausschreibung");
        zeitraum.setValue(stellenanzeige.getZeitraum());

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(stellenanzeige.getBeschreibung());

        //SaveButton
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                stellenanzeige.setName(name.getValue());
                stellenanzeige.setArt(art.getValue());
                stellenanzeige.setBranche(branche.getValue());
                stellenanzeige.setStudiengang(studiengang.getValue());
                stellenanzeige.setOrt(ort.getValue());
                stellenanzeige.setZeitraum(zeitraum.getValue());
                stellenanzeige.setBeschreibung(beschreibung.getValue());

                try {
                    StellenanzeigeControlProxy.getInstance().updateStellenanzeige(stellenanzeige);
                } catch (StellenanzeigeException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Stellenanzeige erfolgreich gespeichert"));
                List<StellenanzeigeDTO> list = null;
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
        verticalLayout = this.buildVerticalLayout(verticalLayout, name, art, branche, studiengang, ort, zeitraum, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }
    public VerticalLayout buildVerticalLayout(VerticalLayout verticalLayout, TextField name, TextField art, TextField branche, TextField studiengang,
                                              TextField ort, DateField zeitraum, TextArea beschreibung, HorizontalLayout horizontalLayout ){
        verticalLayout.addComponent(name);
        verticalLayout.addComponent(art);
        verticalLayout.addComponent(branche);
        verticalLayout.addComponent(studiengang);
        verticalLayout.addComponent(ort);
        verticalLayout.addComponent(zeitraum);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        return verticalLayout;
    }
}

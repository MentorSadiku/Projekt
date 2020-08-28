package org.carlook.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.gui.ui.MyUI;
import org.carlook.gui.windows.ConfirmationWindow;
import org.carlook.gui.windows.DeleteProfileWindow;
import org.carlook.gui.windows.DeleteWindow;
import org.carlook.model.objects.dto.StudentDTO;
import org.carlook.model.objects.dto.UnternehmenDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.ProfileException;
import org.carlook.process.proxy.ProfileControlProxy;
import org.carlook.services.util.Roles;

import java.sql.SQLException;
import java.time.LocalDate;

public class ProfileView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        this.setUp();
    }

    private void setUp() {
        //Top Layer
        this.addComponent(new TopPanel());
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();
        this.addStyleName("schrift-profil");
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        setStyleName("schrift-profil");
        //Felder Student erzeugen

        final NativeSelect<String> anrede = new NativeSelect<>("Anrede");
        anrede.setItems("Herr", "Frau");

        final TextField vorname = new TextField("Vorname");
        vorname.setPlaceholder("Max");

        TextField name = new TextField("Name");
        name.setPlaceholder("Mustermann");

        TextField hochschule = new TextField("Hochschule");
        hochschule.setPlaceholder("HBRS");

        TextField semester = new TextField("Semester");
        semester.setPlaceholder("1");

        DateField gebDatum = new DateField("Geburtsdatum");
        gebDatum.setValue(LocalDate.now());

        TextField kenntnisse = new TextField("Kenntnisse");
        kenntnisse.setPlaceholder("Java, SQL");

        TextField studiengang = new TextField("Studiengang");
        studiengang.setPlaceholder("Informatik");
        //Upload profilbild = new Upload("Profilbild", reciever);

        Label meinProfil = new Label("Mein Profil");
        //Felder Unternehmen erzeugen
        TextField firmenname = new TextField("Firmenname");
        firmenname.setPlaceholder("Firma Exa");

        TextField ansprechpartner = new TextField("Ansprechpartner");
        ansprechpartner.setPlaceholder("Herr Max Mustermann");

        TextField strasse = new TextField("Strasse");
        strasse.setPlaceholder("Beispielweg");

        TextField haus_nr = new TextField("Hausnummer");
        haus_nr.setPlaceholder("1");

        TextField zusatz = new TextField("Zusatz");
        zusatz.setPlaceholder("a");

        TextField ort = new TextField("Ort");
        ort.setPlaceholder("Bonn");

        TextField plz = new TextField("Plz");
        plz.setPlaceholder("53123");

        TextField branche = new TextField("Branche");
        branche.setPlaceholder("IT");

        Label meinUnternehmen = new Label("Mein Unternehmensprofil");

        if (userDTO.hasRole(Roles.STUDENT)) {
            //Werte einsetzen
            StudentDTO studentDTO = new StudentDTO(userDTO);
            try {
                studentDTO = ProfileControlProxy.getInstance().getStudent(userDTO);
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
            }
            if (studentDTO.getAnrede() != null) {
                anrede.setValue(studentDTO.getAnrede());
            }
            if (studentDTO.getVorname() != null) {
                vorname.setValue(studentDTO.getVorname());
            }
            if (studentDTO.getName() != null) {
                name.setValue(studentDTO.getName());
            }
            if (studentDTO.getHochschule() != null) {
                hochschule.setValue(studentDTO.getHochschule());
            }
            if (studentDTO.getSemester() != 0) {
                semester.setValue(String.valueOf(studentDTO.getSemester()));
            }
            if (studentDTO.getGebDatum() != null) {
                gebDatum.setValue(studentDTO.getGebDatum());
            }
            if (studentDTO.getKenntnisse() != null) {
                kenntnisse.setValue(studentDTO.getKenntnisse());
            }
            if (studentDTO.getStudiengang() != null) {
                studiengang.setValue(studentDTO.getStudiengang());
            }
        } else {
            //Werte Setzen
            UnternehmenDTO unternehmenDTO = new UnternehmenDTO(userDTO);
            try {
                unternehmenDTO = ProfileControlProxy.getInstance().getUnternehmen(userDTO);
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
            }
            if (unternehmenDTO.getName() != null) {
                firmenname.setValue(unternehmenDTO.getName());
            }
            if (unternehmenDTO.getAnsprechpartner() != null) {
                ansprechpartner.setValue(unternehmenDTO.getAnsprechpartner());
            }
            if (unternehmenDTO.getStrasse() != null) {
                strasse.setValue(unternehmenDTO.getStrasse());
            }
            if (unternehmenDTO.getPlz() != null) {
                plz.setValue(String.valueOf(unternehmenDTO.getPlz()));
            }
            if (unternehmenDTO.getHaus_nr() != null) {
                haus_nr.setValue(String.valueOf(unternehmenDTO.getHaus_nr()));
            }
            if (unternehmenDTO.getZusatz() != null) {
                zusatz.setValue(unternehmenDTO.getZusatz());
            }
            if (unternehmenDTO.getOrt() != null) {
                ort.setValue(unternehmenDTO.getOrt());
            }
            if (unternehmenDTO.getBranche() != null) {
                branche.setValue(unternehmenDTO.getBranche());
            }
        }

        //Event Nutzer löschen
        Button deleteButton = new Button("Profil löschen");
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DeleteProfileWindow deleteProfileWindow = new DeleteProfileWindow();
                UI.getCurrent().addWindow( new DeleteWindow(deleteProfileWindow) );
            }
        });

        //Event Nutzerdaten updaten
        Button overwriteBtn = new Button("Daten aktualisieren");
        overwriteBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //UI.getCurrent().addWindow(new ConfirmationWindow("Sollen alle Daten aktualisiert werden?"));
                if (userDTO.hasRole(Roles.STUDENT)) {
                    StudentDTO studentDTO = new StudentDTO(userDTO);
                    studentDTO.setAnrede(anrede.getValue());
                    studentDTO.setVorname(vorname.getValue());
                    studentDTO.setName(name.getValue());
                    studentDTO.setHochschule(hochschule.getValue());
                    try {
                        studentDTO.setSemester(Integer.valueOf(semester.getValue()));
                    } catch (NumberFormatException e) {
                        studentDTO.setSemester(0);
                    }
                    studentDTO.setGebDatum(gebDatum.getValue());
                    studentDTO.setKenntnisse(kenntnisse.getValue());
                    studentDTO.setStudiengang(studiengang.getValue());

                    try {
                        ProfileControlProxy.getInstance().updateStudentData(studentDTO);
                        UI.getCurrent().addWindow(new ConfirmationWindow("Ihr Profil wurde erfolgreich aktualisiert!"));
                    } catch (ProfileException e) {
                        Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                    }

                } else {
                    UnternehmenDTO unternehmenDTO = new UnternehmenDTO(userDTO);
                    unternehmenDTO.setName(firmenname.getValue());
                    unternehmenDTO.setAnsprechpartner(ansprechpartner.getValue());
                    unternehmenDTO.setStrasse(strasse.getValue());
                    unternehmenDTO.setPlz(Integer.valueOf(plz.getValue()));
                    unternehmenDTO.setHaus_nr(Integer.valueOf(haus_nr.getValue()));
                    unternehmenDTO.setZusatz(zusatz.getValue());
                    unternehmenDTO.setBranche(branche.getValue());
                    unternehmenDTO.setOrt(ort.getValue());

                    try {
                        ProfileControlProxy.getInstance().updateUnternehmenData(unternehmenDTO);
                        UI.getCurrent().addWindow(new ConfirmationWindow("Ihr Profil wurde erfolgreich aktualisiert!"));
                    } catch (ProfileException e) {
                        Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                    }

                }
            }
        });

        // Horizontal Strasse
        HorizontalLayout horizontalLayoutStrasse = new HorizontalLayout();
        horizontalLayoutStrasse.addComponent(strasse);
        horizontalLayoutStrasse.addComponent(haus_nr);
        horizontalLayoutStrasse.addComponent(zusatz);

        //Horizontal Ort
        HorizontalLayout horizontalLayoutOrt = new HorizontalLayout();
        horizontalLayoutOrt.addComponent(ort);
        horizontalLayoutOrt.addComponent(plz);

        //Horizontal Name
        HorizontalLayout horizontalLayoutName = new HorizontalLayout();
        horizontalLayoutName.addComponent(vorname);
        horizontalLayoutName.addComponent(name);

        //horizontal Uni
        HorizontalLayout horizontalLayoutUni = new HorizontalLayout();
        horizontalLayoutUni.addComponent(hochschule);
        horizontalLayoutUni.addComponent(studiengang);
        horizontalLayoutUni.addComponent(semester);

        if (userDTO.hasRole(Roles.STUDENT)) {
            this.addComponent(meinProfil);
            this.addComponent(anrede);
            this.addComponent(horizontalLayoutName);
            this.addComponent(horizontalLayoutUni);
            this.addComponent(kenntnisse);
            this.addComponent(gebDatum);
            this.addComponent(overwriteBtn);
            this.addComponent(deleteButton);
        } else {
            this.addComponent(meinUnternehmen);
            this.addComponent(firmenname);
            this.addComponent(ansprechpartner);
            this.addComponent(horizontalLayoutStrasse);
            this.addComponent(horizontalLayoutOrt);
            this.addComponent(branche);
            this.addComponent(overwriteBtn);
            this.addComponent(deleteButton);
        }
    }
}

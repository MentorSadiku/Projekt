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
import org.carlook.model.dao.EndkundeDAO;
import org.carlook.model.dao.UserDAO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.ProfileException;
import org.carlook.process.proxy.ProfileControlProxy;
import org.carlook.services.util.Roles;

import java.sql.SQLException;

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

        //Endkunde Namen hinzufügen
        TextField name = new TextField("Name");
        name.setPlaceholder("Max Mustermann");

        Label meinProfil = new Label("Mein Profil");


        //Vertriebler Namen hinzufügen
        TextField vertrieblername = new TextField("Name");
            vertrieblername.setPlaceholder("Max Mustermann");


        Label meinVertrieb = new Label("Mein Vertriebsprofil");

        if (userDTO.hasRole(Roles.ENDKUNDE)) {
            //Werte einsetzen
            EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);
            try {
                endkundeDTO = ProfileControlProxy.getInstance().getEndkunde(userDTO);
            } catch (SQLException e) {
                Notification.show("3 Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
            }

            if (endkundeDTO.getName() != null) {
                name.setValue(endkundeDTO.getName());
            }

        }
        else {
            //Werte Setzen
            VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
            try {
                vertrieblerDTO = ProfileControlProxy.getInstance().getVertriebler(userDTO);
            } catch (SQLException e) {
                Notification.show("8 Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
            }
            if (vertrieblerDTO.getName() != null) {
                vertrieblername.setValue(vertrieblerDTO.getName());
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
                if (userDTO.hasRole(Roles.ENDKUNDE)) {
                    EndkundeDTO endkundeDTO = new EndkundeDTO(userDTO);

                    endkundeDTO.setName(name.getValue());
                    //endkundeDTO.setHochschule(hochschule.getValue());

                    try {
                        ProfileControlProxy.getInstance().updateEndkundeData(endkundeDTO);
                        UI.getCurrent().addWindow(new ConfirmationWindow("Ihr Profil wurde erfolgreich aktualisiert!"));
                    } catch (ProfileException e) {
                        Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                    }

                } else {
                    VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
                    vertrieblerDTO.setName(vertrieblername.getValue());
                    //vertrieblerDTO.setStadt(stadt.getValue());

                    try {
                        ProfileControlProxy.getInstance().updateVertrieblerData(vertrieblerDTO);
                        UI.getCurrent().addWindow(new ConfirmationWindow("Ihr Profil wurde erfolgreich aktualisiert!"));
                    } catch (ProfileException e) {
                        Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                    }

                }
            }
        });

        // Horizontal Strasse
       /* HorizontalLayout horizontalLayoutStrasse = new HorizontalLayout();
        horizontalLayoutStrasse.addComponent(strasse);
        horizontalLayoutStrasse.addComponent(haus_nr);
        horizontalLayoutStrasse.addComponent(zusatz);

        //Horizontal Ort
        HorizontalLayout horizontalLayoutOrt = new HorizontalLayout();
        horizontalLayoutOrt.addComponent(ort);
        horizontalLayoutOrt.addComponent(plz);
        */

        //Horizontal Name
        HorizontalLayout horizontalLayoutName = new HorizontalLayout();
        //horizontalLayoutName.addComponent(vorname);
        horizontalLayoutName.addComponent(name);

        //horizontal Uni
        /*HorizontalLayout horizontalLayoutUni = new HorizontalLayout();
        horizontalLayoutUni.addComponent(hochschule);
        horizontalLayoutUni.addComponent(studiengang);
        horizontalLayoutUni.addComponent(semester);

         */

        if (userDTO.hasRole(Roles.ENDKUNDE)) {
            this.addComponent(meinProfil);
            //this.addComponent(anrede);
            this.addComponent(horizontalLayoutName);
            //this.addComponent(horizontalLayoutUni);
            // this.addComponent(kenntnisse);
           // this.addComponent(gebDatum);
            this.addComponent(overwriteBtn);
            this.addComponent(deleteButton);
        } else {
            this.addComponent(meinVertrieb);
            this.addComponent(vertrieblername);
           // this.addComponent(stadt);
            this.addComponent(overwriteBtn);
            this.addComponent(deleteButton);
        }
    }
}

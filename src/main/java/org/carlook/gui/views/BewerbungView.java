package org.carlook.gui.views;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.gui.ui.MyUI;
import org.carlook.gui.windows.DeleteReservierungWindow;
import org.carlook.gui.windows.DeleteWindow;
import org.carlook.model.objects.dto.ReservierungDTO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.EndkundeDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.proxy.BewerbungControlProxy;
import org.carlook.process.proxy.StellenanzeigeControlProxy;
import org.carlook.services.util.BuildGrid;

import java.sql.SQLException;
import java.util.List;

public class BewerbungView extends VerticalLayout implements View {

    private AutoDTO selektiert;
    private List<AutoDTO> list;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        EndkundeDTO endkundeDTO = new EndkundeDTO( ( (MyUI) UI.getCurrent() ).getUserDTO() );

        this.setUp(endkundeDTO);
    }

    private void setUp(EndkundeDTO endkundeDTO) {

        //Top Layer
        this.addComponent( new TopPanel() );
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();
        setStyleName("schrift-profil");
        //Tabelle
        final Grid<AutoDTO> grid = new Grid<>("Ihre Bewerbungen");
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.UNDEFINED);
        SingleSelect<AutoDTO> selection = grid.asSingleSelect();
        grid.setStyleName("schrift-tabelle");
        //Tabelle füllen
        try {
            list = StellenanzeigeControlProxy.getInstance().getAnzeigenForStudent(endkundeDTO);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        BuildGrid.buildGrid(grid);
        grid.setItems(list);

        //DeleteButton
        Button deleteButton = new Button("Löschen");
        deleteButton.setEnabled(false);

        //Tabellen Select Config
        grid.addSelectionListener(new SelectionListener<AutoDTO>() {
            @Override
            public void selectionChange(SelectionEvent<AutoDTO> event) {
                if (selection.getValue() == null) {
                    deleteButton.setEnabled(false);
                }
                else {
                    selektiert = selection.getValue();
                    deleteButton.setEnabled(true);
                }
            }
        });

        //deleteButton Config
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ReservierungDTO reservierungDTO = null;
                try {
                    reservierungDTO = BewerbungControlProxy.getInstance().getBewerbungForStellenanzeige(selektiert, endkundeDTO);
                } catch (SQLException e) {
                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
                } catch (DatabaseException e) {
                    Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                DeleteReservierungWindow deleteReservierungWindow = new DeleteReservierungWindow(reservierungDTO);
                UI.getCurrent().addWindow( new DeleteWindow(deleteReservierungWindow) );
            }
        });

        //HorizontalLayout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(deleteButton);

        //Darstellung
        addComponent(grid);
        addComponent(horizontalLayout);
        setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
    }
}

package org.carlook.services.util;

import com.vaadin.ui.Grid;
import org.carlook.model.objects.dto.StellenanzeigeDTO;

public class BuildGrid {
    public static void buildGrid(Grid<StellenanzeigeDTO> grid) {
        grid.removeAllColumns();
        grid.addColumn(StellenanzeigeDTO::getName).setCaption("Name");
        grid.addColumn(StellenanzeigeDTO::getArt).setCaption("Art");
        grid.addColumn(StellenanzeigeDTO::getBranche).setCaption("Branche");
        grid.addColumn(StellenanzeigeDTO::getStudiengang).setCaption("Studiengang");
        grid.addColumn(StellenanzeigeDTO::getOrt).setCaption("Ort");
        grid.addColumn(StellenanzeigeDTO::getZeitraum).setCaption("Ende der Ausschreibung");
    }
}

package org.carlook.model.objects.dto;

import java.time.LocalDate;

public class AutoDTO extends AbstractDTO {
    private int auto_id;
    private int baujahr;
    private String beschreibung = "";
    private String marke = "";

    public int getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(int auto_id) {
        this.auto_id = auto_id;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String toString(){
        return "ID: " + this.getAuto_id() + "\n" +
                "ID Anzeige: " + this.getBaujahr() + "\n" +
                "Beschreibung: " + this.getBeschreibung() + "\n" +
                "Art: " + this.getMarke() + "\n";

    }


}

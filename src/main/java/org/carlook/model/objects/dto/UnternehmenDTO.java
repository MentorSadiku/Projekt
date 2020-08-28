package org.carlook.model.objects.dto;

public class UnternehmenDTO extends UserDTO {
    private String ansprechpartner;
    private String strasse;
    private Integer plz;
    private Integer haus_nr;
    private String zusatz;
    private String ort;
    private String branche;

    public UnternehmenDTO(UserDTO userDTO) {
        super(userDTO);
    }

    public String getAnsprechpartner() {
        return ansprechpartner;
    }

    public void setAnsprechpartner(String ansprechpartner) {
        this.ansprechpartner = ansprechpartner;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public Integer getPlz() {
        return plz;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    public Integer getHaus_nr() {
        return haus_nr;
    }

    public void setHaus_nr(Integer haus_nr) {
        this.haus_nr = haus_nr;
    }

    public String getZusatz() {
        return zusatz;
    }

    public void setZusatz(String zusatz) {
        this.zusatz = zusatz;
    }

    public String getBranche() {
        return branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }
}

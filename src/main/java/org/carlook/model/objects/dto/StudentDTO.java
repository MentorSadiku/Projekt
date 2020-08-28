package org.carlook.model.objects.dto;

import java.time.LocalDate;

public class StudentDTO extends UserDTO {
    private String anrede;
    private String hochschule;
    private Integer semester;
    private LocalDate gebDatum = LocalDate.now();
    private String kenntnisse;
    private String studiengang;

    public StudentDTO(UserDTO userDTO) {
        super(userDTO);
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getHochschule() {
        return hochschule;
    }

    public void setHochschule(String hochschule) {
        this.hochschule = hochschule;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public LocalDate getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(LocalDate gebDatum) {
        this.gebDatum = gebDatum;
    }

    public String getKenntnisse() {
        return kenntnisse;
    }

    public void setKenntnisse(String kenntnisse) {
        this.kenntnisse = kenntnisse;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }
}

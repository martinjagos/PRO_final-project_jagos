import java.time.LocalDate;

public class Objednavka {
    private int id;
    private String filament;
    private LocalDate datum;
    private double spotřeba;
    private double energie;
    private boolean vlastniModel;
    // private double naklady;

    public Objednavka(int id, String filament, LocalDate datum, double spotřeba, double energie, boolean vlastniModel) {
        this.id = id;
        this.filament = filament;
        this.datum = datum;
        this.spotřeba = spotřeba;
        this.energie = energie;
        this.vlastniModel = vlastniModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilament() {
        return filament;
    }

    public void setFilament(String filament) {
        this.filament = filament;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public double getSpotřeba() {
        return spotřeba;
    }

    public void setSpotřeba(double spotřeba) {
        this.spotřeba = spotřeba;
    }

    public double getEnergie() {
        return energie;
    }

    public void setEnergie(double energie) {
        this.energie = energie;
    }

    public boolean isVlastniModel() {
        return vlastniModel;
    }

    public void setVlastniModel(boolean vlastniModel) {
        this.vlastniModel = vlastniModel;
    }
}

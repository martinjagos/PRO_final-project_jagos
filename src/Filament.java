import java.io.File;

public class Filament {
    private int id;
    private String nazev;
    private String barva;
    private String material;
    private double hmotnost;
    private String obrazek;
    private int cena;

    private double cenaNaGram;

    public Filament(int id, String nazev, String barva, String material, double hmotnost, String obrazek, int cena, double cenaNaGram) {
        this.id = id;
        this.nazev = nazev;
        this.barva = barva;
        this.material = material;
        this.hmotnost = hmotnost;
        this.obrazek = obrazek;
        this.cena = cena;
        this.cenaNaGram = cenaNaGram;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getBarva() {
        return barva;
    }

    public void setBarva(String barva) {
        this.barva = barva;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getHmotnost() {
        return hmotnost;
    }

    public void setHmotnost(int hmotnost) {
        this.hmotnost = hmotnost;
    }

    public String getObrazek() {
        return obrazek;
    }

    public void setObrazek(String obrazek) {
        this.obrazek = obrazek;
    }

    public void setHmotnost(double hmotnost) {
        this.hmotnost = hmotnost;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public double getCenaNaGram() {
        return cenaNaGram;
    }

    public void setCenaNaGram(double cenaNaGram) {
        this.cenaNaGram = cenaNaGram;
    }
}

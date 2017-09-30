package appbatros.solutions.com.mx.appbatros.objetos;

public class Asiento {
    private int imagen;
    private int tv;
    private int numeroAsiento;
    private String status;

    public Asiento(int imagen, int numeroAsiento, String status,int tv) {
        this.imagen = imagen;
        this.numeroAsiento = numeroAsiento;
        this.status = status;
        this.tv = tv;
    }

    public int getImagen() {
        return imagen;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public String getStatus() {
        return status;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTv() {
        return tv;
    }
}

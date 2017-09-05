package appbatros.solutions.com.mx.appbatros.objetos;


public  class Pasajero {

    private String tipo;
    private int icono;
    private int icon_seleccionado;

    public Pasajero(String tipo, int icono, int icon_seleccionado) {
        this.tipo = tipo;
        this.icono = icono;
        this.icon_seleccionado = icon_seleccionado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public int getIcon_seleccionado() {
        return icon_seleccionado;
    }

    public void setIcon_seleccionado(int icon_seleccionado) {
        this.icon_seleccionado = icon_seleccionado;
    }
}
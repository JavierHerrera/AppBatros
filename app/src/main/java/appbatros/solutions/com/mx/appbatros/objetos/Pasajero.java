package appbatros.solutions.com.mx.appbatros.objetos;


public  class Pasajero {

    private String tipo;
    private int icono;
    private int icon_seleccionado;
    private int numero_asiento;
    private int importe;
    private String nombre;
    private String referencia;

    public Pasajero(String tipo, int icono, int icon_seleccionado) {
        this.tipo = tipo;
        this.icono = icono;
        this.icon_seleccionado = icon_seleccionado;
        importe = 0;
        nombre = "";
        referencia = "0";
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

    public int getNumero_asiento() {
        return numero_asiento;
    }

    public void setNumero_asiento(int numero_asiento) {
        this.numero_asiento = numero_asiento;
    }

    //Metodo que se utiliza en Camion para poder borrar y asignar asientos
    public Boolean getPasajeroPorNumeroAsiento(int n_siento){
        if ( numero_asiento == n_siento){
            return true;
        }
        return false;
    }

    public int getImporte() {
        return importe;
    }

    public void setImporte(int importe) {
        this.importe = importe;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
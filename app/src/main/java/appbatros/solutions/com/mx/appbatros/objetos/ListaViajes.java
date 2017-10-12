package appbatros.solutions.com.mx.appbatros.objetos;

/**
 * Created by JH on 03/10/2017.
 */

public class ListaViajes {

    public static Viaje viajeIda,viajeRegreso;

    public static int getImporteTotalViajes(){

        if (!viajeIda.redondo){return viajeIda.getImporteTotal();}

       return viajeIda.getImporteTotal() + viajeRegreso .getImporteTotal();
    }

}

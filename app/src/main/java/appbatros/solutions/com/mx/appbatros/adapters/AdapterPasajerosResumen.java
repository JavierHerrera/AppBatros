package appbatros.solutions.com.mx.appbatros.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import appbatros.solutions.com.mx.appbatros.R;
import appbatros.solutions.com.mx.appbatros.objetos.ResumenDelPasajero;

public class AdapterPasajerosResumen extends RecyclerView.Adapter<AdapterPasajerosResumen.PasajerosViewHolder> {
    private List<ResumenDelPasajero> items;

    public AdapterPasajerosResumen(List items)
    {
        this.items = items;
    }


    public static class PasajerosViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView nombre;
        private TextView tipo;
        private TextView asiento;
        private TextView importe;

        public PasajerosViewHolder(View v) {
            super(v);

            nombre = v.findViewById(R.id.textView_pasajeroNombre_recycleResumen);
            tipo = v.findViewById(R.id.textView_pasajeroTipo_recycleResumen);
            asiento = v.findViewById(R.id.textView_pasajeroNumeroAsiento_recycleResumen);
            importe = v.findViewById(R.id.textView_pasajeroImporte_recycleResumen);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public PasajerosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PasajerosViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_pasajero_resumen, parent, false));
    }

    @Override
    public void onBindViewHolder(PasajerosViewHolder holder, int position) {
        ResumenDelPasajero pasajero = items.get(position);

        holder.nombre.setText(pasajero.getNombre());
        holder.tipo.setText(pasajero.getTipo());
        holder.asiento.setText(pasajero.getAsiento());
        holder.importe.setText(pasajero.getImporte());

    }

}
package appbatros.solutions.com.mx.appbatros.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import appbatros.solutions.com.mx.appbatros.R;
import appbatros.solutions.com.mx.appbatros.objetos.Historial;

public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.HistorialViewHolder> {
    private List<Historial> items;

    public AdapterHistorial(List items)
    {
        this.items = items;
    }


    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView id;
        private TextView nombre;
        private TextView tipoPasajero;
        private TextView asiento;
        private TextView importe;
        private TextView origen;
        private TextView destino;
        private TextView fecha;
        private TextView hora;
        private TextView tipoPago;
        private TextView referencia;
        private TextView status;

        public HistorialViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.textView_pasajeroNombre_historial);
            tipoPasajero = (TextView) v.findViewById(R.id.textView_pasajeroTipo_historial);
            asiento = (TextView) v.findViewById(R.id.textView_pasajeroNumeroAsiento_historial);
            importe = (TextView) v.findViewById(R.id.textView_pasajeroImporte_historial);
            origen = (TextView) v.findViewById(R.id.textView_origen_historial);
            destino = (TextView) v.findViewById(R.id.textView_destino_historial);
            fecha = (TextView) v.findViewById(R.id.textView_fechaSalida_historial);
            hora = (TextView) v.findViewById(R.id.textView_horaSalida_historial);
            tipoPago = (TextView) v.findViewById(R.id.textView_tipoPago_historial);
            referencia = (TextView) v.findViewById(R.id.textView_referencia_historial);
            status = (TextView) v.findViewById(R.id.textView_status_historial);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistorialViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_historial, parent, false));
    }

    @Override
    public void onBindViewHolder(HistorialViewHolder holder, int position) {
        Historial historial = items.get(position);

        holder.nombre.setText(historial.getNombre());
        holder.tipoPasajero.setText(historial.getTipoPasajero());
        holder.asiento.setText(historial.getAsiento());
        holder.importe.setText(historial.getImporte());
        holder.origen.setText(historial.getOrigen());
        holder.destino.setText(historial.getDestino());
        holder.origen.setText(historial.getOrigen());
        holder.fecha.setText(historial.getFecha());
        holder.hora.setText(historial.getHora());
        holder.tipoPago.setText(historial.getTipoPago());
        holder.referencia.setText(historial.getReferencia());
        holder.status.setText(historial.getStatus());
    }

}
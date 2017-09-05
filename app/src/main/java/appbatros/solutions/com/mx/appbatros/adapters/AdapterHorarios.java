package appbatros.solutions.com.mx.appbatros.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import appbatros.solutions.com.mx.appbatros.R;
import appbatros.solutions.com.mx.appbatros.objetos.Salidas;
import appbatros.solutions.com.mx.appbatros.interfaces.MyInterfaceActivityHorarios;

/**
 * Created by JH on 24/08/2017.
 */

public class AdapterHorarios extends RecyclerView.Adapter<AdapterHorarios.SalidasViewHolder> {
    private List<Salidas> items;
    private MyInterfaceActivityHorarios mAdapterCallback;

    public static class SalidasViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private TextView horarioSalida;
        private TextView horarioLlegada;
        private TextView tipoCamion;
        private TextView acientosDisponibles;
        private TextView precio;

        public SalidasViewHolder(View v) {
            super(v);
            horarioSalida = (TextView) v.findViewById(R.id.textviewHoraSalidaCardviewHorarios);
            horarioLlegada = (TextView) v.findViewById(R.id.textviewHoraLlegadaCardviewHorarios);
            tipoCamion = (TextView) v.findViewById(R.id.textviewTipoCamionCardviewMain);
            acientosDisponibles = (TextView) v.findViewById(R.id.textviewAsientosDisponiblesCardviewMain);
            precio = (TextView) v.findViewById(R.id.precio);
        }
    }

    public AdapterHorarios(List<Salidas> items, MyInterfaceActivityHorarios myInterface) {
        this.items = items;
        mAdapterCallback = myInterface;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public SalidasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_horarios, viewGroup, false);
        return new SalidasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SalidasViewHolder viewHolder, int i) {
        viewHolder.horarioSalida.setText(items.get(i).getHorarioSalida());
        viewHolder.horarioLlegada.setText(items.get(i).getHorarioLlegada());
        viewHolder.tipoCamion.setText(items.get(i).getTipoCamion());
        viewHolder.acientosDisponibles.setText("Asiento Disponibles: "+ items.get(i).getAcientosDisponibles());
        viewHolder.precio.setText("$ " + items.get(i).getPrecio());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterCallback.foo();

            }
        });

    }
}
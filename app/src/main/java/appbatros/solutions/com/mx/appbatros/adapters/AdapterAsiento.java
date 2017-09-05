package appbatros.solutions.com.mx.appbatros.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import appbatros.solutions.com.mx.appbatros.objetos.Asiento;
import appbatros.solutions.com.mx.appbatros.R;
import appbatros.solutions.com.mx.appbatros.interfaces.MyInterfaceActivityCamion;

public class AdapterAsiento extends RecyclerView.Adapter<AdapterAsiento.AsientoViewHolder> {
    private List<Asiento> items;
    private MyInterfaceActivityCamion mAdapterCallback;

    public static class AsientoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        private View view;
        private ImageView imagen;
        private TextView numeroAsiento;

        public AsientoViewHolder(View v) {
            super(v);
            view = v;
            imagen = (ImageView) v.findViewById(R.id.imagen_asientos);
            numeroAsiento = (TextView) v.findViewById(R.id.numero_asiento);
        }
    }

    public AdapterAsiento(List<Asiento> items, MyInterfaceActivityCamion myInterfaceActivityCamion) {
        this.items = items;
        mAdapterCallback = myInterfaceActivityCamion;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AsientoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.asiento_recycle, viewGroup, false);
        return new AsientoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AsientoViewHolder viewHolder, final int position) {
        final Asiento asientoItem = items.get(position);
        viewHolder.imagen.setImageResource(asientoItem.getImagen());
        int numAsiento = asientoItem.getNumeroAsiento();
        final String asiento = String.valueOf(numAsiento);
        viewHolder.numeroAsiento.setText(asiento);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterCallback.foo(asientoItem,position);
            }
        });
    }
}

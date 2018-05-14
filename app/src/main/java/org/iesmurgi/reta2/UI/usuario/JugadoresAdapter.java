package org.iesmurgi.reta2.UI.usuario;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.iesmurgi.reta2.Data.Objetos.RankingEquipos;
import org.iesmurgi.reta2.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jota on 05/04/2018.
 */

public class JugadoresAdapter extends RecyclerView.Adapter<JugadoresAdapter.ViewHolder> {

    private ArrayList<RankingEquipos> puntosEquipos;
    private Context context;

    public JugadoresAdapter(ArrayList<RankingEquipos> puntosEquipos, Context context) {
        this.puntosEquipos = puntosEquipos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jugadores_item, parent,false);
        return new JugadoresAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nombreEquipo.setText(puntosEquipos.get(position).getNombreEquipo());
        holder.participantes.setText(String.valueOf(puntosEquipos.get(position).getPuntosTotales()));

    }

    @Override
    public int getItemCount() {
        return puntosEquipos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_nombre_equipo_item)
        TextView nombreEquipo;
        @BindView(R.id.tv_participantes_item)
        TextView participantes;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

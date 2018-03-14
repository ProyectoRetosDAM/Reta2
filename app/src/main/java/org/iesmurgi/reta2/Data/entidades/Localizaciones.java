package org.iesmurgi.reta2.Data.entidades;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by usuario on 15/02/18.
 */
//TODO: Si explota es esto
@Entity(tableName = "localizaciones",
        primaryKeys ={"idUsuario","fecha"},
        foreignKeys = @ForeignKey(entity = Usuarios.class,
                parentColumns = "idUsuario",
        childColumns = "idUsuario",onDelete = CASCADE,onUpdate = CASCADE)
        )
public class Localizaciones {

    private int idUsuario;
    @NonNull
    private Date fecha;
    private float localizacionLatitud;
    private float localizacionLongitud;


    public Localizaciones(int idUsuario, Date fecha, float localizacionLatitud, float localizacionLongitud) {
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.localizacionLatitud = localizacionLatitud;
        this.localizacionLongitud = localizacionLongitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getLocalizacionLatitud() {
        return localizacionLatitud;
    }

    public void setLocalizacionLatitud(float localizacionLatitud) {
        this.localizacionLatitud = localizacionLatitud;
    }

    public float getLocalizacionLongitud() {
        return localizacionLongitud;
    }

    public void setLocalizacionLongitud(float localizacionLongitud) {
        this.localizacionLongitud = localizacionLongitud;
    }
}
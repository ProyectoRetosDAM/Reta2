/*

Reta2  Copyright (C) 2018  Alberto Fernández Fernández, Santiago Álvarez Fernández, Joaquín Pérez Rodríguez

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.


Contact us:

fernandez.fernandez.angel@gmail.com
santiago.alvarez.dam@gmail.com
perezrodriguezjoaquin0@gmail.com

*/

package org.iesmurgi.reta2.UI.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import org.iesmurgi.reta2.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Actividad que permite al administrador crear una partida
 * @author Alberto Fernández
 * @author Santiago Álvarez
 * @author Joaquín Pérez
 */
public class CrearPartida extends AppCompatActivity {

    ProgressDialog progressDialog;
    @BindView(R.id.txt_crearPartida_nombre)
    EditText txt_nombre;
    @BindView(R.id.txt_crearPartida_contra)
    EditText txt_contra;
    @BindView(R.id.txt_crearPartida_duracion)
    EditText txt_duracion;
    @BindView(R.id.bt_crearpartida_continuar)
    Button bt_continuar;

    @BindView(R.id.bt_crearpartida_numeroderetos)
    EditText txt_numeroretos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_partida);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(CrearPartida.this);

    }

    /**
     * Método que inserta una partida en la base de datos
     */
    @OnClick(R.id.bt_crearpartida_continuar)
    void BottonInsertarPartida() {
        int nR;
        if (
                !txt_nombre.getText().toString().trim().isEmpty() &&
                        !txt_nombre.getText().toString().trim().isEmpty() &&
                        !txt_nombre.getText().toString().trim().isEmpty() &&
                        !txt_numeroretos.getText().toString().trim().isEmpty()
                ) {
            nR =Integer.parseInt( txt_numeroretos.getText().toString().trim());
            if (nR<=15 && nR>=1){

                progressDialog.setMessage("Creando partida...");
            progressDialog.show();

            final String URL = "http://geogame.ml/api/insertar_partida.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("success")) {
                        Toast.makeText(getApplicationContext(), "Partida creada!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), CrearRetoActivity.class);
                        i.putExtra("partidaNombre", txt_nombre.getText().toString().trim());
                        i.putExtra("partidaContra", txt_contra.getText().toString().trim());
                        i.putExtra("partidaNumerosRetos", txt_numeroretos.getText().toString().trim());

                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Nombre ya esta en uso, ponga otro.", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nombre", txt_nombre.getText().toString().trim());
                    params.put("passwd", txt_contra.getText().toString().trim());
                    params.put("maxDuracion", txt_duracion.getText().toString().trim());
                    return params;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
            }else{
                Toast.makeText(getApplicationContext(), "La cantidad de retos debe ser de 1 a 15", Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(getApplicationContext(), "Tiene que rellenar todos los campos", Toast.LENGTH_LONG).show();
        }


    }//end metodo
}

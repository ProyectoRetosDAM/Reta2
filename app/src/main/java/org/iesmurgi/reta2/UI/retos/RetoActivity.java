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

package org.iesmurgi.reta2.UI.retos;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.iesmurgi.reta2.Data.BasedeDatosApp;
import org.iesmurgi.reta2.Data.DAOS.RespuestasDao;
import org.iesmurgi.reta2.Data.DAOS.RetosDao;
import org.iesmurgi.reta2.Data.entidades.Respuestas;
import org.iesmurgi.reta2.Data.entidades.Retos;
import org.iesmurgi.reta2.R;
import org.iesmurgi.reta2.UI.usuario.LoginModel;

import static org.iesmurgi.reta2.UI.retos.MapPrincActivity.RETO_FINALIZADO;
/**
 * Actividad que permite al usuario jugar un reto
 * @author Alberto Fernández
 * @author Santiago Álvarez
 * @author Joaquín Pérez
 */
public class RetoActivity extends AppCompatActivity {
    CountDownTimer contador;
    @BindView(R.id.rb_reto_opcion1)
    RadioButton rbRetoOpcion1;
    @BindView(R.id.rb_reto_opcion2)
    RadioButton rbRetoOpcion2;
    @BindView(R.id.rb_reto_opcion3)
    RadioButton rbRetoOpcion3;
    @BindView(R.id.rg_reto_rgrupo)
    RadioGroup rgRetoRgrupo;
    @BindView(R.id.txt_reto_descripcion)
    TextView txtRetoDescripcion;
    @BindView(R.id.btn_reto_responder)
    Button btnRetoResponder;
    @BindView(R.id.btn_reto_cancelar)
    Button btnRetoCancelar;
    @BindView(R.id.txt_reto_crono)
    TextView txtRetoCrono;
    @BindView(R.id.txt_reto_nombre)
    TextView txtRetoNombre;
    @BindView(R.id.btn_reto_verVideo)
    Button btnRetoVerVideo;
    @BindView(R.id.et_reto_respuestaUnica)
    EditText etRetoRespuestaUnica;
    @BindView(R.id.btn_reto_subirImagen)
    Button btnRetoSubirImagen;

    ProgressDialog progressDialog;


    private List<Respuestas> misRespuestas = new ArrayList<>();
    private List<Respuestas> misResBorrar = new ArrayList<>();
    private Retos miReto;
    private String resElegida;

    int idUsuario;
    private LoginModel cronoViewModel;
    private long nTiempo;
    private String nombrepartida;
    private RetosDao retosDao;
    private RespuestasDao respuestasDao;
    private int idpartida;
    private int idreto;
    private List<Respuestas> respuestas = new ArrayList<>();
    private boolean salida = false;

    BasedeDatosApp db;

    int[] aux;



    class recRes extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... p) {


            respuestas = db.respuestasDao().getRespuestas(p[0]);
            Log.e("respuestas", "" + respuestas.size());

            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            init();
        }


    }

    class RecReto extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... p) {


            miReto = db.retosDao().getReto_Partida(p[0], p[1]);
            Log.e("retos", "" + miReto.getNombre());

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new recRes().execute(aux[1]);



        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reto);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        progressDialog = new ProgressDialog(RetoActivity.this);

        db = BasedeDatosApp.getAppDatabase(this);
        try {
            aux = getIntent().getExtras().getIntArray("PARTIDAYRETO");
            idUsuario = getIntent().getIntExtra("idUsuario", 0);
            nombrepartida = getIntent().getExtras().getString("NOMBREPARTIDA");
        }catch (NullPointerException ex){



        }
        Log.e("partidanombrereto",nombrepartida);
        Log.e("idpartida", "" + aux[0]);
        Log.e("idreto", "" + aux[1]);

        //rellenado de los radioBtn aleatoriamente

        new RecReto().execute(aux[0], aux[1]);


    }


    /**
     * Método que inicializa la pantalla del reto con los datos obtenidos en la consulta a la base de datos.
     */
    void init() {


        misResBorrar.clear();
        misRespuestas.clear();
        rellenarArray();
        Log.i("COMPROBAR misResBorrar:", misResBorrar.size() + "");
        Log.i("COMPROBAR misRespuesta:", misRespuestas.size() + "");

        int tipoReto = miReto.getTipo();

        String enlaceVideo = miReto.getVideo();

        if (enlaceVideo.isEmpty()){
            btnRetoVerVideo.setVisibility(View.GONE);
        }else{
            btnRetoVerVideo.setVisibility(View.VISIBLE);
        }


        switch (tipoReto){
            //segun el tipo de reto mostrará los campos correspondientes

            case 1:     //tipo multirespuesta

                rbRetoOpcion1.setVisibility(View.VISIBLE);
                rbRetoOpcion2.setVisibility(View.VISIBLE);
                rbRetoOpcion3.setVisibility(View.VISIBLE);
                btnRetoResponder.setVisibility(View.VISIBLE);
                etRetoRespuestaUnica.setVisibility(View.GONE);
                btnRetoSubirImagen.setVisibility(View.GONE);

                int selec = elegirRandom(0, misResBorrar.size() - 1);
                rbRetoOpcion1.setText(misResBorrar.get(selec).getDescripcion());
                misResBorrar.remove(selec);
                int selec2 = elegirRandom(0, misResBorrar.size() - 1);
                rbRetoOpcion2.setText(misResBorrar.get(selec2).getDescripcion());
                misResBorrar.remove(selec2);
                int selec3 = 0;
                rbRetoOpcion3.setText(misResBorrar.get(selec3).getDescripcion());
                misResBorrar.remove(selec3);

                break;

            case 2:     //tipo respuesta unica

                rbRetoOpcion1.setVisibility(View.GONE);
                rbRetoOpcion2.setVisibility(View.GONE);
                rbRetoOpcion3.setVisibility(View.GONE);
                etRetoRespuestaUnica.setVisibility(View.VISIBLE);
                btnRetoResponder.setVisibility(View.VISIBLE);
                btnRetoSubirImagen.setVisibility(View.GONE);
                break;

            case 3:     //tipo subir imagen
                rbRetoOpcion1.setVisibility(View.GONE);
                rbRetoOpcion2.setVisibility(View.GONE);
                rbRetoOpcion3.setVisibility(View.GONE);
                etRetoRespuestaUnica.setVisibility(View.GONE);
                btnRetoResponder.setVisibility(View.GONE);
                btnRetoSubirImagen.setVisibility(View.VISIBLE);
                break;

        }





        txtRetoNombre.setText(miReto.getNombre());
        txtRetoDescripcion.setText(miReto.getDescripcion());


        //este bloque es el control de la cuenta atras
        cronoViewModel = ViewModelProviders.of(this).get(LoginModel.class);

        Log.v("mitiempo", "" + cronoViewModel.getMiTiempo());

        long timeRec = miReto.getMaxDuracion() * 1000*60;
        if (cronoViewModel.getMiTiempo() == null) {
            cronoViewModel.setMiTiempo(timeRec);

            contador = new CountDownTimer(timeRec, 1000) {

                public void onTick(long tiempo) {
                    int mins =(int) (tiempo/(1000*60))%60;
                    int seg = (int) (tiempo/1000)%60;
                    txtRetoCrono.setText("Tiempo: " + mins+":"+seg);
                    cronoViewModel.setMiTiempo(tiempo);
                    Log.v("mitiempo", "" + cronoViewModel.getMiTiempo());
                }

                public void onFinish() {
                    txtRetoCrono.setText("No puntuas!");
                    cronoViewModel.setMiTiempo((long) 0);
                    if (!salida) {
                        salida = true;
                        insertarPuntos(0,"Se acabo el tiempo, puntuas 0");

                    }
                }
            }.start();
        }

        else {

            final long ntiempo = cronoViewModel.getMiTiempo();
            contador =  new CountDownTimer(ntiempo, 1000) {

                public void onTick(long ntiempo) {
                    int mins =(int) (ntiempo/(1000*60))%60;
                    int seg = (int) (ntiempo/1000)%60;
                    txtRetoCrono.setText("Tiempo: " +mins+":"+seg);
                    cronoViewModel.setMiTiempo(ntiempo);
                }

                public void onFinish() {
                    txtRetoCrono.setText("No puntuas!");
                    cronoViewModel.setMiTiempo((long) 0);
                    insertarPuntos(0,"Se acabo el tiempo, puntuas 0");

                    if (!salida) {
                        salida = true;
                        insertarPuntos(0,"Se acabo el tiempo, puntuas 0");

                    }
                }
            }.start();

        }

        Log.v("mitiempo", String.valueOf(cronoViewModel.getMiTiempo()));




        //cuando cambia la seleccion setea la resElegida con el string del radiobuton
        rgRetoRgrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int idBoton) {
                if (idBoton == R.id.rb_reto_opcion1) {

                    resElegida = rbRetoOpcion1.getText().toString().trim();

                } else if (idBoton == R.id.rb_reto_opcion2) {

                    resElegida = rbRetoOpcion2.getText().toString().trim();

                } else if (idBoton == R.id.rb_reto_opcion3) {

                    resElegida = rbRetoOpcion3.getText().toString().trim();
                }
            }
        });


        //fin aqui
        btnRetoResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //segun el tipo de reto controla la respuesta de diferentes maneras
                switch (tipoReto){

                    case 1:     //tipo multirespuesta

                        for (int i = 0; i < respuestas.size(); i++) {
                            if (respuestas.get(i).getDescripcion().equals(resElegida)) {

                                if (respuestas.get(i).getVerdadero() == 1) {

                                    insertarPuntos(puntuacionPorTiempo(),"Acertaste!! puntuas:"+ puntuacionPorTiempo());
                                } else {
                                    insertarPuntos(0,"Has fallado el reto, puntuas 0");
                                }
                            }
                        }

                        break;

                    case 2:     //tipo respuesta unica

                        if (!etRetoRespuestaUnica.getText().toString().trim().isEmpty()){
                            if (etRetoRespuestaUnica.getText().toString().trim().equalsIgnoreCase(respuestas.get(0).getDescripcion().trim())){

                                insertarPuntos(puntuacionPorTiempo(),"Acertaste!! puntuas:"+ puntuacionPorTiempo());
                            }else{
                                insertarPuntos(0,"Has fallado el reto, puntuas 0");
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Introduce una respuesta!!", Toast.LENGTH_SHORT).show();
                        }



                        break;

                    case 3:     //tipo subir imagen

                        break;

                }

            }
        });

        btnRetoVerVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RetoVideoActivity.class);
                i.putExtra("enlaceVideo", enlaceVideo);
                startActivity(i);
            }
        });



        //fin aqui
        btnRetoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertarPuntos(0,"Saltaste el reto, puntuas 0");
            }
        });

        btnRetoSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("partidaretoactivity",nombrepartida);
                startActivityForResult(new Intent(getApplicationContext(),RetoFotoActivity.class).putExtra("PARTIDA", nombrepartida).putExtra("RETO",miReto.getNombre()),RETO_FINALIZADO);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RETO_FINALIZADO) {

            if (resultCode == RESULT_OK) {
                insertarPuntos(0,"La foto sera puntuada por un administrador");


                }else{



                }

            }


        }


    /**
     * Método que rellena los arrayslist con las respuestas del reto
     */
    public void rellenarArray() {

        for (int i = 0; i < respuestas.size(); i++) {
            misRespuestas.add(respuestas.get(i));

            misResBorrar.add(respuestas.get(i));

        }

    }

    /**
     * Método que calcula la puntuacion obtenida en el reto en funcion del tiempo empleado en responderla.
     * @return puntos obtenidos en el reto
     */
    int puntuacionPorTiempo(){
        Log.e("Tiempo tras","tiempo"+tiempotrascurrido());
       int tiempo=(int)((tiempotrascurrido()/60));
        int puntos;
        Log.e("puntos","el tiempo actual es"+tiempo);
        if (tiempo<1){
            puntos=miReto.getPuntuacion();
        }else{
            puntos=miReto.getPuntuacion()-(miReto.getPuntuacion()*tiempo/miReto.getMaxDuracion());
        }
        Log.e("puntos","Los puntos dados son" +puntos);
       return puntos;
    }

    /**
     * Método que devuelve el tiempo en segundos
     * @return tiempo en segundos
     */
     long tiempoensegundos(){
        return (cronoViewModel.getMiTiempo()/1000)-miReto.getMaxDuracion();
    }

    /**
     * Método que devuelve el tiempo transcurrido en el reto.
     * @return
     */
    long tiempotrascurrido(){
        return (miReto.getMaxDuracion()*60)-tiempoensegundos();
    }

    /**
     * Método que inserta los puntos obtenidos del reto en la base de datos
     * @param puntos puntos del reto
     * @param mensaje mensaje que acompaña, para los toast
     */
    void insertarPuntos(int puntos,String mensaje) {
        contador.cancel();

        progressDialog.setMessage("Insertando tu puntuacion ...");
       // progressDialog.setCancelable(false);
        progressDialog.show();
        final String URL = "http://geogame.ml/api/insertar_puntuacion.php";

        Constraints myConstraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                // Many other constraints are available, see the
                // Constraints.Builder reference
                .build();

        Data.Builder builder = new Data.Builder();

        builder.putString("idUsuario",String.valueOf(idUsuario));
        builder.putString("idReto",String.valueOf(miReto.getIdReto()));
        builder.putString("tiempo",String.valueOf(tiempotrascurrido()));
        builder.putString("puntuacion",String.valueOf(puntos));

        OneTimeWorkRequest insertarpoints = new OneTimeWorkRequest.Builder(RetoNormalWorker.class).setInputData(builder.build()).setConstraints(myConstraints).build();
        cronoViewModel.mWorkManager.enqueue(insertarpoints);
        updateNumReto(mensaje);

    }//fin insertarPuntos


    /**
     * Método que actualiza el reto actual por el que va el usuario en la base de datos
     * @param mensaje mensaje para los toast
     */
    void updateNumReto(String mensaje) {


        Constraints myConstraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                // Many other constraints are available, see the
                // Constraints.Builder reference
                .build();
        Data.Builder builder = new Data.Builder();
        builder.putString("idUsuario", String.valueOf(idUsuario));
        builder.putString("idPartida", String.valueOf(miReto.getIdPartida()));
        builder.putString("ultimoReto",String.valueOf(getIntent().getIntExtra("numeroRetoArray",0) ));
        OneTimeWorkRequest updatenumretos = new OneTimeWorkRequest.Builder(RetoUpdateNumRetoWorker.class).setInputData(builder.build()).setConstraints(myConstraints).build();
        cronoViewModel.mWorkManager.enqueue(updatenumretos);


        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
        setResult(MapPrincActivity.RESULT_OK, new Intent(getApplicationContext(), MapPrincActivity.class));
        finish();



    }


    /**
     * Método que devuelve un numero random en un intervalo
     * @param min numero minimo
     * @param max numero maximo
     * @return numero aleatorio en el intervalo
     */
    public int elegirRandom(int min, int max) {

        int rango = (max - min) + 1;
        return (int) (Math.random() * rango) + min;

    }
}

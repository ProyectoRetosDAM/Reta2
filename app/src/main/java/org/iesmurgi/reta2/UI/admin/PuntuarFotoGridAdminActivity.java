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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.iesmurgi.reta2.Chat.Chat;
import org.iesmurgi.reta2.Chat.ChatAdapter;
import org.iesmurgi.reta2.Data.BasedeDatosApp;
import org.iesmurgi.reta2.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que muestra una lista con los equipos que tienen alguna foto de algun reto por puntuar
 * @author Alberto Fernández
 * @author Santiago Álvarez
 * @author Joaquín Pérez
 */

public class PuntuarFotoGridAdminActivity extends AppCompatActivity {

    private String nombrereto;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private String partida,usuario;
    private ArrayList<String> nombres = new ArrayList<>();
    private ArrayList<String> uris = new ArrayList<>();
    private ArrayList<Uri> urisreal = new ArrayList<>();
    private int idpartida;
    StorageReference storageReference;

    @BindView(R.id.recicler_chat_admin)
     RecyclerView recicler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_partidas_admin);
        ButterKnife.bind(this);
         usuario = getIntent().getExtras().getString("USUARIO");
         partida = getIntent().getExtras().getString("PARTIDA");
         idpartida = getIntent().getExtras().getInt("IDPARTIDA");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        if(!nombres.isEmpty()) nombres.clear();
        if(!urisreal.isEmpty()) urisreal.clear();

        myRef.child("Imagenes").child(partida).child(usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    nombres.add(uniqueKeySnapshot.getKey());
                    Log.e("key",""+uniqueKeySnapshot.getKey());
                }

                recicler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recicler.setAdapter(new ChatAdapter(getApplicationContext(),7,nombres,partida,usuario,idpartida,getIntent().getStringExtra("codigoqr")));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }



}

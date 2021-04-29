package com.digitalgeko.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginWelcomeActivity extends AppCompatActivity {
    //esta clase esta bloqueada para que solo funcione en modo portrait desde el manifest
    ImageButton cerrarSesion;
    TextView usuario,correo, telefono;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_welcome);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        cerrarSesion =(ImageButton)findViewById(R.id.cerrSesion);
        usuario = (TextView)findViewById(R.id.nombreW);
        correo = (TextView)findViewById(R.id.correoW);
        telefono = (TextView)findViewById(R.id.telefonoW);

        BotonGlide();
        imprimirDatos();
        cerrarLaSesion();

    }

    private void BotonGlide()
    {
        //Uso de glide para poder mostrar imagen desde URL
        Glide.with(LoginWelcomeActivity.this).load("https://img.icons8.com/material-outlined/24/000000/shutdown.png").centerCrop().placeholder(R.drawable.common_full_open_on_phone).into(cerrarSesion);
    }

    private void imprimirDatos()
    {
        String id = firebaseAuth.getCurrentUser().getUid(); //obtuve el id del usuario para obtener su informacion
        databaseReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) //si existe un nodo entonces obtnego la informacion
                    {
                        String nombreB =  snapshot.child("nombre").getValue().toString();
                        String correoB =  snapshot.child("correo").getValue().toString();
                        String numeroB = snapshot.child("telefono").getValue().toString();

                        usuario.setText(nombreB);
                        correo.setText(correoB);
                        telefono.setText(numeroB);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cerrarLaSesion()
    {


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ////////////////para preguntarle antres de que cierre la sesion
                AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginWelcomeActivity.this);

                dialogo.setTitle("Cerrar Sesión");

                dialogo.setMessage("¿Seguro quieres cerrar sesión?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //////////si acetpto el usuario salir
                        firebaseAuth.signOut(); // se cierra sesion manualmente
                        startActivity( new Intent(LoginWelcomeActivity.this, MainActivity.class));
                        finish();
                        ////////////////////
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //si no acepto salir el usuario
                        dialog.cancel();
                    }
                }).create().show();
                ////////////////

            }
        });
    }

}
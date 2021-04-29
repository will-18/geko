/*Programa Digitalgeko - login
2021 */
package com.digitalgeko.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.engine.Resource;
import com.digitalgeko.login.Internet.Internet;
import com.digitalgeko.login.fragments.IniciarSesion;
import com.digitalgeko.login.fragments.Registrarse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
   //esta clase esta bloqueada para que solo funcione en modo portrait desde el manifest

    FirebaseAuth firebaseAuth;

    Registrarse boton;

FragmentTransaction cambio;
Fragment fragmentIniciar, fragmentRegistrarse;

Button iniciarSesion, registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        fragmentIniciar = new IniciarSesion();
        fragmentRegistrarse = new Registrarse();
        iniciarSesion = (Button)findViewById(R.id.iniciarFrag);
        registrarse = (Button) findViewById(R.id.registrarseFrag);


        //fregment Inicio de sesion es en el que se arrancará
         getSupportFragmentManager().beginTransaction().add(R.id.fragmentBase, fragmentIniciar).commit();
    }

    public void onClick (View view)
    {
        //cambio entre fragments
        cambio= getSupportFragmentManager().beginTransaction();

        switch (view.getId()) //obtuve los ID´s de los fragments
        {
                                        // aqui  cambio de fragments y al mismo tiempo cambio color del boton
            case R.id.registrarseFrag:
                registrarse.setBackgroundColor(getResources().getColor(R.color.verdeseleccionado));
                iniciarSesion.setBackgroundColor(getResources().getColor(R.color.verdepalido));
                cambio.replace(R.id.fragmentBase, fragmentRegistrarse).commit();

                break;
            case R.id.iniciarFrag:
                iniciarSesion.setBackgroundColor(getResources().getColor(R.color.verdeseleccionado));
                registrarse.setBackgroundColor(getResources().getColor(R.color.verdepalido));
                cambio.replace(R.id.fragmentBase, fragmentIniciar).commit();
                break;
        }
    }

    @Override
    protected void onResume() { // en el onResume, tengo la respuesta por si hay internet o no en el dispositiovo
        super.onResume();
        if (Internet.isOnline(MainActivity.this))
        {

        }
        else {
            View view = findViewById(R.id.activity_main); //cambie de Toast a Snakbar documentacion https://developer.android.com/training/snackbar/action?hl=es
            Snackbar snackbar = Snackbar.make(view , "No tienes acceso a internet, intenta más tarde", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }

    @Override
    protected void onStart() { //aqui miro si ya se registro alguin para que lo dirija directamente al LoginWelcomeActivity
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(MainActivity.this, LoginWelcomeActivity.class));
            finish();
        }
    }
}
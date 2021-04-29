package com.digitalgeko.login.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.digitalgeko.login.LoginWelcomeActivity;
import com.digitalgeko.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class IniciarSesion extends Fragment {
EditText corr, contr;
Button iniSes;
String correo, contrasena;
FirebaseAuth firebaseAuth;
ImageButton vercontrasena;

    public IniciarSesion() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_iniciar_sesion, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        corr = (EditText) view.findViewById(R.id.email);
        contr = (EditText)view.findViewById(R.id.pass);
        iniSes = (Button)view.findViewById(R.id.iniciarS);
        vercontrasena = (ImageButton)view.findViewById(R.id.ver);
        verContrasena();
        validacion();
        return view; //retorne view para poder usar (findViewById)


    }

    private void verContrasena()
    {
        vercontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tipo = contr.getInputType(); //obtuve el estado de InputType del EditText (Password)

                //contr.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD); ///aqui me quede  9:35 am

                if (tipo == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) // para cuenado el Input == visible
                {
                    //Toast.makeText(getContext(), "es visible",Toast.LENGTH_SHORT).show();
                    contr.setInputType(129);//para ocultar caracteres
                }
                else
                {
                    contr.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); /// para cuando el Input == ****
                }
            }
        });
    }

    private void validacion()
    {
        iniSes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = corr.getText().toString();
                contrasena = contr.getText().toString();
                if (correo.isEmpty()|| contrasena.isEmpty())
                {
                    Toast.makeText(getContext(), "complete los datos", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    iniciarSesion();
                }

            }


        });

    }
    private void iniciarSesion()
    {
        //inicie  sesion en firebase Documentacion = https://firebase.google.com/docs/auth/android/custom-auth?hl=es
        firebaseAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) // cuando los datos concuerdan lanzo la otra activity
                    {
                        startActivity( new Intent(getContext(), LoginWelcomeActivity.class));
                        getActivity().finish();
                    }
                    else //de lo contrario lanzo un toast para dar a conocer un error
                    {
                        Toast.makeText(getContext(), "No se encontraron coincidencias, compruebe los datos", Toast.LENGTH_LONG).show();

                    }
            }
        });
    }
}
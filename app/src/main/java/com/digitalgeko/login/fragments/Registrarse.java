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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registrarse extends Fragment {
EditText nombreR, numeroR, correoR, contrasenaR;
Button registrarseR;
String nombre, numero, correo, contrasena;
ImageButton vercontrasena;
FirebaseAuth firebaseAuth;
DatabaseReference databaseReference;

    public Registrarse() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_registrarse, container, false);

        firebaseAuth = FirebaseAuth.getInstance(); //instancias de firebase para poder usar sin problema
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nombreR = (EditText)view.findViewById(R.id.nomReg);
        numeroR = (EditText)view.findViewById(R.id.numReg);
        correoR = (EditText)view.findViewById(R.id.corrReg);
        contrasenaR=(EditText)view.findViewById(R.id.contraReg);
        registrarseR = (Button) view.findViewById(R.id.botonReg);
        vercontrasena = (ImageButton)view.findViewById(R.id.ver2);


        verContrasena();

        registrarUsuario();

        return view;
    }

    private void verContrasena()
    {
        vercontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tipo = contrasenaR.getInputType();



                if (tipo == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                {

                    contrasenaR.setInputType(129);//ocuelte caracteres
                }
                else
                {
                    contrasenaR.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
    }

    private void registrarUsuario()
    {
        registrarseR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               nombre = nombreR.getText().toString();
               numero = numeroR.getText().toString();
               correo = correoR.getText().toString();
               contrasena= contrasenaR.getText().toString();

               if (nombre.isEmpty() || numero.isEmpty() || correo.isEmpty()|| contrasena.isEmpty()) // validadcion para ver que esten llenos los Edit text
               {
                   Toast.makeText(getContext(), "complete todos los campos", Toast.LENGTH_SHORT).show();
               }
               else
               {
                    if (contrasena.length() >= 6)  //validacion para que sean mas de 6 caracteres las contraseñas
                    {


                        if (correo.contains("@")) //comparar si hay @ dentro del texto del correo
                        {
                            String minuscula = contrasena.toLowerCase(); //pasar todo a minuscula
                            String mayusculas = contrasena.toUpperCase(); // pasa todo a mayuscula
                            String sinsignos = contrasena.replaceAll("\\p{Punct}", ""); //remplace caracteres por espacios documentacion:https://www.javatpoint.com/java-string-replaceall

                            if (contrasena.equals(minuscula))
                            {
                                Toast.makeText(getContext(), "La contraseña debe tener almenos un caracter mayuscula", Toast.LENGTH_SHORT).show();

                            }
                            else if (contrasena.equals(mayusculas))
                            {
                                Toast.makeText(getContext(), "La contraseña debe tener almenos un caracter minuscula", Toast.LENGTH_SHORT).show();

                            }
                            else if (contrasena.equals(sinsignos))
                            {
                                Toast.makeText(getContext(), "La contraseña debe tener letras y caracteres especiales", Toast.LENGTH_SHORT).show();

                            }
                            else if (numero.length()!= 10)
                            {
                                Toast.makeText(getContext(), "Ingrese un numero telefónico valido", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ///////////////Datos correctos
                                RegistroBaseDatos();
                            }

                        }
                        else
                            {
                                Toast.makeText(getContext(), "Ingrese un correo valido", Toast.LENGTH_SHORT).show(); //para cuando el correo no es correcto
                            }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "La contraseña debe tener almenos 6 caracteres", Toast.LENGTH_SHORT).show();

                    }
               }
            }
        });
    }

    private void RegistroBaseDatos()
    {
        //almacene datos en Firebase Autentiocacion dandole el correo y la contraseña
        firebaseAuth.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) // si se registro correctamente,  guardo los datos en una base de datos llamada "Usuarios".
                    {
                        Map<String, Object>map = new HashMap<>();
                        map.put("nombre", nombre);
                        map.put("correo", correo);
                        map.put("contrasena", contrasena);
                        map.put("telefono", numero);

                        String raiz = firebaseAuth.getCurrentUser().getUid();   //obtengo id de la autenticacion para crear nodo en firebase con identificador unico
                        /*se crea o actualiza la base de datos*/databaseReference.child("Usuarios").child(raiz).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task_1)
                            {
                                if (task_1.isSuccessful()) // si se guardaron correctamente los datos realizo un startActivity para lanzar segunda activity (LoginWelcomeActivity)
                                {
                                    startActivity(new Intent(getContext(),LoginWelcomeActivity.class));
                                    getActivity().finish();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Error al subir los datos, intente nuevamente", Toast.LENGTH_SHORT).show(); // por si surge error durante la creacion de la base de datos, muestro un Toast
                                }

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getContext(), "revise su conexion a internet y vuelva a intentar", Toast.LENGTH_SHORT).show(); // por si falla la autenticacion por distintos motivos
                    }
            }
        });
    }


}
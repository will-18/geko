package com.digitalgeko.login.Internet;

import com.google.firebase.database.FirebaseDatabase;
///esta clase es para la presistencia de datos para modo ofline
public class Persistencia extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

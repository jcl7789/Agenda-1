package com.zeus.agenda.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zeus.agenda.R;
import com.zeus.agenda.Utils.AgendaAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void lanzarAgregarContacto(View v){
        Intent t = new Intent(this, ActivityAgregar.class);
        startActivity(t);

    }

    public void lanzarListarContactos(View v){
        Intent t = new Intent(this, ActivityListar.class);
        startActivity(t);

    }
}

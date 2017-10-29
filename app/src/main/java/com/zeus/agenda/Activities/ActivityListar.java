package com.zeus.agenda.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zeus.agenda.R;
import com.zeus.agenda.Utils.Contacto;
import com.zeus.agenda.Utils.AgendaAdapter;
import com.zeus.agenda.Utils.Contacto;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Juan on 29/10/2017.
 */

public class ActivityListar extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        listView = (ListView) findViewById(R.id.listPhone);
        mostrarContactos();


    }

    public void mostrarContactos() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            ArrayList<Contacto> listaContactos = obtenerContactos();
            AgendaAdapter adapter = new AgendaAdapter(this, listaContactos);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    lanzarVerContacto(null, position);
                }
            });
        }
    }

    public  ArrayList<Contacto> obtenerContactos(){
        ArrayList<Contacto> lista = new ArrayList<>();
        ContentResolver resolver = getContentResolver();

        Cursor contactos =  resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (contactos.moveToNext()){
            Contacto r = new Contacto();
            String id = contactos.getString(contactos.getColumnIndex(ContactsContract.Contacts._ID));
            r.setId(id);
            r.setBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.contacto_default));
            String nombre = contactos.getString(contactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            r.setNombre(nombre);
            Cursor telefonos = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[] { id }, null);
            while (telefonos.moveToNext()){
                String telefono = telefonos.getString(telefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            r.setTelefono(telefono);
            }

            Cursor emails = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ?", new String[] { id }, null);
            String mail;
            while (emails.moveToNext()){
                mail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                r.setMail(mail);
            }

            lista.add(r);

        }

        return lista;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                mostrarContactos();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void lanzarVerContacto(View v, long posicion) {
        Intent t= new Intent(this,ActivityVer.class);
        ArrayList<Contacto> lista = obtenerContactos();
        Contacto c =  lista.get((int)posicion);
        Log.e("Contacto encontrado", c.getNombre());
        t.putExtra("imagen", c.getBitmap());
        t.putExtra("id", c.getId());
        t.putExtra("nombre", c.getNombre());
        t.putExtra("mail", c.getMail());
        t.putExtra("telefono", c.getTelefono());
        startActivity(t);
        this.finish();
    }

}

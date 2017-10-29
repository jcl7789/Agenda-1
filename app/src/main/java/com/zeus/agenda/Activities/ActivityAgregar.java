package com.zeus.agenda.Activities;

/**
 * Created by Juan on 29/10/2017.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zeus.agenda.R;

public class ActivityAgregar extends AppCompatActivity {

    private TextView tvTextoNom;
    private TextView tvTextoApe;
    private TextView tvTextoMail;
    private TextView tvTextoTel;
    private Button bAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        tvTextoNom=(TextView) findViewById(R.id.tfNombre);
        tvTextoApe=(TextView) findViewById(R.id.tfApellido);
        tvTextoMail=(TextView) findViewById(R.id.tfMail);
        tvTextoTel=(TextView) findViewById(R.id.tfTelefono);
        bAgregar=(Button) findViewById(R.id.buttonAgregar);

    }


    public void agregar (View view){
        try {
            String nombre = tvTextoNom.getText().toString() +" "+tvTextoApe.getText().toString();
            String telefono = tvTextoTel.getText().toString();
            String mail = tvTextoMail.getText().toString();
            ContentResolver cr = this.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(ContactsContract.Contacts.DISPLAY_NAME, nombre);
            cv.put(ContactsContract.CommonDataKinds.Phone.NUMBER, telefono);
            cv.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            cv.put(ContactsContract.CommonDataKinds.Email.ADDRESS, mail);
            cv.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
            cr.insert(ContactsContract.RawContacts.CONTENT_URI, cv);
            Toast.makeText(this,"Contacto agregado",Toast.LENGTH_SHORT).show();

        } catch(Exception e) {
            Log.e("ERROR",e.getMessage());
            Toast.makeText(this,"Error al agregar contacto",Toast.LENGTH_SHORT).show();
        }

        this.finish();
    }


}

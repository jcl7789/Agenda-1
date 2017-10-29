package com.zeus.agenda.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeus.agenda.R;
import com.zeus.agenda.Utils.Contacto;

import java.util.ArrayList;


/**
 * Created by Juan on 29/10/2017.
 */

public class ActivityVer extends AppCompatActivity {
    private TextView tvTextoNom;
    private ImageView imgAvatar;
    private TextView tvTextoMail;
    private TextView tvTextoTel;

    private String id;
    private String nombre;
    private Bitmap imagen;
    private String mail;
    private String telefono;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);


        Bundle bundle = getIntent().getExtras();

        this.id = bundle.getString("id");
        this.nombre = bundle.getString("nombre");
        this.imagen = (Bitmap) bundle.get("imagen");
        this.mail = bundle.getString("mail");
        this.telefono = bundle.getString("telefono");

        tvTextoNom = (TextView) findViewById(R.id.tvNombre);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        tvTextoMail = (TextView) findViewById(R.id.tvMail);
        tvTextoTel = (TextView) findViewById(R.id.tvTelefono);

        tvTextoNom.setText(this.nombre);
        imgAvatar.setImageBitmap(this.imagen);
        tvTextoMail.setText(this.mail);
        tvTextoTel.setText(this.telefono);

    }

    public void lanzarModificarContacto(View v) {
        Intent t = new Intent(this, ActivityModificar.class);
        t.putExtra("id", this.id);
        t.putExtra("nombre", this.nombre);
        t.putExtra("imagen", this.imagen);
        t.putExtra("mail", this.mail);
        t.putExtra("telefono", this.telefono);
        startActivity(t);
        this.finish();
    }

    public void borrarElContacto(View v) {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID +" = ?", new String[] { id }, null);
        while (cur.moveToNext()) {
            try {
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                System.out.println("The uri is " + uri.toString());
                cr.delete(uri, null, null);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }


        Toast.makeText(this, "Contacto borrado", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void llamarPorTelefono(View view) {
        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+this.telefono));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(tel);


    }

    public void enviarMail(View view) {
        try {
            Intent mail = new Intent(Intent.ACTION_SEND);
            mail.putExtra(Intent.EXTRA_EMAIL, new String[]{this.mail});
            //mail.putExtra(Intent.EXTRA_SUBJECT,"Hola Carlos");
            //mail.putExtra(Intent.EXTRA_TEXT,"Hola Carlos, como te va?");
            mail.setType("message/rfc822");
            //mail.putExtra(Intent.EXTRA_CC,new String[]{"monicakuhn2@yahoo.com.ar","carlosecimino@gmail.com"});
            //mail.putExtra(Intent.EXTRA_BCC,new String[]{"consultas@profmatiasgarcia.com.ar","carlosecimino@gmail.com"});
            //startActivity(Intent.createChooser(mail,"Elija un email cliente: "));
            startActivity(mail);
        } catch (Exception ex) {
            Log.e("ERROR:", ex.getMessage());
        }
    }

    public void enviarWhatsApp(View view) {
        try {
            Intent whatsApp = new Intent(Intent.ACTION_SEND);
            //whatsApp.putExtra(Intent.EXTRA_TEXT,"El texto que vas a enviar");
            whatsApp.setType("text/plain");
            whatsApp.setPackage("com.whatsapp");
            startActivity(whatsApp);
        } catch (Exception ex) {
            Log.e("ERROR:", ex.getMessage());
        }
    }
}

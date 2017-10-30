package com.zeus.agenda.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zeus.agenda.R;
import com.zeus.agenda.Utils.AgendaAdapter;
import com.zeus.agenda.Utils.Contacto;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Juan on 29/10/2017.
 */

public class ActivityListar extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_CALL = 100;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        listView = findViewById(R.id.listPhone);
        mostrarContactos();


    }

    public void mostrarContactos() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] { Manifest.permission.READ_CONTACTS }, PERMISSIONS_REQUEST_READ_CONTACTS);
            requestPermissions(new String[] { Manifest.permission.CALL_PHONE }, PERMISSIONS_REQUEST_CALL);
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

        Cursor contactos = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        while (contactos.moveToNext()){
            Contacto r = new Contacto();
            String id = contactos.getString(contactos.getColumnIndex(ContactsContract.Contacts._ID));
            r.setId(id);
            // Para activar la carga de fotos del contacto descomentar aca y comentar linea 90
            /*Bitmap bm = obtenerFoto(id);
            if (bm == null) {
                bm = BitmapFactory.decodeResource(getResources(), R.mipmap.contacto_default);
            }*/
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.contacto_default);
            r.setBitmap(getRoundedCornerBitmap(bm, 64));
            String nombre = contactos.getString(contactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String apellido = "";
            Cursor nombre_entero = resolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.DATA1 + " = ?", new String[]{nombre}, null);
            while (nombre_entero.moveToNext()) {
                nombre = nombre_entero.getString(nombre_entero.getColumnIndex(ContactsContract.Data.DATA2));
                apellido = nombre_entero.getString(nombre_entero.getColumnIndex(ContactsContract.Data.DATA3));

            }
            r.setNombre(nombre);
            if (apellido != null) r.setApellido(apellido);
            else r.setApellido("");
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
        t.putExtra("imagen", c.getBitmap());
        t.putExtra("id", c.getId());
        t.putExtra("nombre", c.getNombre());
        t.putExtra("apellido", c.getApellido());
        t.putExtra("mail", c.getMail());
        t.putExtra("telefono", c.getTelefono());
        startActivity(t);
        this.finish();
    }


    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public Bitmap obtenerFoto(String id) {
        Cursor cur = null;
        boolean esNulo = true;
        try {
            cur = this.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (cur.moveToNext())
                {
                    esNulo = false;
                }
            }
        } catch (Exception e) {

        }
        if(!esNulo) {
            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                    .parseLong(id));
            Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), person);
            if (photo_stream != null) {
                BufferedInputStream buf = new BufferedInputStream(photo_stream);
                return BitmapFactory.decodeStream(buf);
            } else return null;

        } else return null;
    }

}

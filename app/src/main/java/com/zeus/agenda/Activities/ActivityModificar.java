package com.zeus.agenda.Activities;

/**
 * Created by Juan on 29/10/2017.
 */

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zeus.agenda.R;

import java.util.ArrayList;

public class ActivityModificar extends AppCompatActivity {

    private TextView tvTextoNom;
    private TextView tvTextoApe;
    private TextView tvTextoMail;
    private TextView tvTextoTel;

    private String id;
    private String nombre;
    private String apellido;
    private String mail;
    private String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00A8C1"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        Bundle bundle = getIntent().getExtras();

        this.id = bundle.getString("id");
        this.nombre = bundle.getString("nombre");
        this.apellido = bundle.getString("apellido");
        this.mail = bundle.getString("mail");
        this.telefono = bundle.getString("telefono");

        tvTextoNom = findViewById(R.id.tfNombre);
        tvTextoApe = findViewById(R.id.tfApellido);
        tvTextoMail = findViewById(R.id.tfMail);
        tvTextoTel = findViewById(R.id.tfTelefono);

        tvTextoNom.setText(this.nombre);
        tvTextoApe.setText(this.apellido);
        tvTextoMail.setText(this.mail);
        tvTextoTel.setText(this.telefono);
    }

    public void modificar (View view)
    {

        String nombre = tvTextoNom.getText().toString();
        String apellido = tvTextoApe.getText().toString();
        String telefono = tvTextoTel.getText().toString();
        String mail = tvTextoMail.getText().toString();

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, apellido)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nombre)
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, telefono)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, mail)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME).build());
        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " = ?", new String[]{id}, null);
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
        Toast.makeText(this, "Contacto modificado", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}


package com.zeus.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listPhone);

        ArrayList<Agenda> listPhoneBook = new ArrayList<Agenda>();
        listPhoneBook.add(new Agenda(BitmapFactory.decodeResource(getResources(),R.mipmap.contacto_default),"Pete Houston", "010-9817-6331", "pete.houston.17187@gmail.com"));
        listPhoneBook.add(new Agenda(BitmapFactory.decodeResource(getResources(),R.mipmap.contacto_default) , "Lina Cheng", "046-7764-1142", "lina.cheng011@sunny.com"));
        listPhoneBook.add(new Agenda(BitmapFactory.decodeResource(getResources(),R.mipmap.contacto_default),"Jenny Nguyen", "0913-223-498", "jenny_in_love98@yahoo.com"));
        AgendaAdapter adapter = new AgendaAdapter(this, listPhoneBook);
        listView.setAdapter(adapter);
    }
}

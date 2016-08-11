package br.net.gamhoupagou.actionmodedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  View.OnLongClickListener
{


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;

    boolean is_in_action_mode = false;
    TextView counter_text_view;

    int img_id[] = {R.drawable.palmeiras1,R.drawable.corinthians,
            R.drawable.fluminense,R.drawable.santos,
            R.drawable.flamengo,R.drawable.internacional,R.drawable.vasco,
            R.drawable.gremio,R.drawable.goias };
    private ArrayList<Contact> contacts;
    private ArrayList<Contact> select_list = new ArrayList<>();
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contacts = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        counter_text_view = (TextView) findViewById(R.id.counter_text);
        counter_text_view.setVisibility(View.GONE);
        String[] name, email;

        name = getResources().getStringArray(R.array.times);
        email = getResources().getStringArray(R.array.email);

        int i = 0;

        for(String NAME: name)
        {
            Contact contact = new Contact(img_id[i],NAME, email[i]);
            contacts.add(contact);
            i++;
        }
        adapter = new ContactAdapter(contacts,MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
        return  true;

    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter_text_view.setVisibility(View.VISIBLE);
        is_in_action_mode = true;
        adapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        return false;
    }

public void prepareSelection(View view ,int position)
{
    if(((CheckBox)view).isChecked())
    {
        select_list.add(contacts.get(position));
        counter = counter+1;
        updateCounter(counter);
    }else{
        select_list.remove(contacts.get(position));
        counter = counter-1;
        updateCounter(counter);
    }
}

    public void updateCounter(int counter){
        if(counter == 0)
        {
            counter_text_view.setText("0 item selected");
        }else {
            counter_text_view.setText(counter+" item selected");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.item_delete)
        {
            is_in_action_mode = false;
            ContactAdapter contactAdapter = (ContactAdapter) adapter;
            contactAdapter.updateAdapter(select_list);
        }else   if(item.getItemId() == android.R.id.home)
        {
            clearActionMode();
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    public void clearActionMode()
    {
        is_in_action_mode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_activity_main);
        counter_text_view.setVisibility(View.GONE);
        counter_text_view.setText("0 item selected");
        counter = 0;
        select_list.clear();
    }

    @Override
    public void onBackPressed() {

       if(is_in_action_mode)
       {
           clearActionMode();
           adapter.notifyDataSetChanged();
       }else{
           super.onBackPressed();
       }


    }
}
package com.example.tp4;

import static java.util.Locale.filter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class ManagingActivity extends AppCompatActivity {

    ListView lv;
    Button b;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b = findViewById(R.id.button);
        lv = findViewById(R.id.list);
        dataBase = new DataBase(this);
        EditText search = findViewById(R.id.search);
        viewData();
        search.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            public void afterTextChanged(android.text.Editable s) {}
        });
        b.setOnClickListener(v -> finish());
        lv.setOnItemClickListener((parent, view, position, id) -> {
            String[] items = {"Modifier", "Supprimer"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Action");
            builder.setItems(items, (dialog, which) -> {
                if (which == 0)
                    showUpdate(this, lv, position);
                else
                    delete(lv, position);
            });
            builder.show();
        });
    }
    public void viewData() {
        Cursor c = dataBase.getAllData();
        ArrayList<String> list = new ArrayList<>();
        if (c.getCount() == 0) {
            Toast.makeText(this, "Base vide", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                list.add(c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " + c.getString(4));
            }
            Collections.sort(list, (a, b) -> {
                String nameA = a.split(" ", 5)[1].toLowerCase();
                String nameB = b.split(" ", 5)[1].toLowerCase();
                return nameA.compareTo(nameB);
            });
            ListAdapter adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
        }
    }
    private void showUpdate(Activity ac, ListView lv, int p) {

        Dialog dialog = new Dialog(ac);
        dialog.setContentView(R.layout.update_content);
        dialog.setTitle("Update");
        EditText name = dialog.findViewById(R.id.nom);
        EditText mail = dialog.findViewById(R.id.mail);
        EditText phone = dialog.findViewById(R.id.phone);
        Button bt = (Button) dialog.findViewById(R.id.save);
        RadioGroup roleGroup = dialog.findViewById(R.id.roleGroupUpdate);
        String[] chaine = lv.getAdapter().getItem(p).toString().split(" ", 5);
        name.setText(chaine[1]);
        mail.setText(chaine[2]);
        phone.setText(chaine[3]);
        String role = chaine[4];
        if (role.equals("Manager")) {
            ((RadioButton) dialog.findViewById(R.id.managerUpdate)).setChecked(true);
        } else if (role.equals("Développeur")) {
            ((RadioButton) dialog.findViewById(R.id.devUpdate)).setChecked(true);
        } else {
            ((RadioButton) dialog.findViewById(R.id.stagiaireUpdate)).setChecked(true);
        }
        int width = (int) (ac.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (ac.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        bt.setOnClickListener(v -> {
            int id = Integer.parseInt(chaine[0]);
            int selectedId = roleGroup.getCheckedRadioButtonId();
            RadioButton rb = dialog.findViewById(selectedId);
            String newRole = rb.getText().toString();
            dataBase.update(name.getText().toString(), mail.getText().toString(), phone.getText().toString(), newRole , id);
            Toast.makeText(ac, "Mise à jour avec succès", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            viewData();
        });
    }

    private void delete(ListView lv, int p) {

        String[] chaine = lv.getAdapter().getItem(p).toString().split(" ");
        int id = Integer.parseInt(chaine[0]);
        dataBase.delete(id);
        Toast.makeText(this, "Suppression avec succès", Toast.LENGTH_SHORT).show();
        viewData();
    }
    private void filter(String text) {
        ArrayList<String> filtered = new ArrayList<>();
        for (int i = 0; i < lv.getAdapter().getCount(); i++) {
            String item = lv.getAdapter().getItem(i).toString();
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, filtered));
    }
}
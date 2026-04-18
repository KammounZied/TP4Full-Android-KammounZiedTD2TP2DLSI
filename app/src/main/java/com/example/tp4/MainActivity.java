package com.example.tp4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button b1, b2;
    private EditText nom, mail, phone;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.save);
        b2 = (Button) findViewById(R.id.base);
        nom = (EditText) findViewById(R.id.nom);
        mail = (EditText) findViewById(R.id.mail);
        phone = (EditText) findViewById(R.id.phone);
        RadioGroup roleGroup = findViewById(R.id.roleGroup);
        db = new DataBase(this);
        b1.setOnClickListener(v -> {
            String n = nom.getText().toString();
            String m = mail.getText().toString();
            String p = phone.getText().toString();
            if (n.isEmpty() || m.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(m)) {
                Toast.makeText(this, "Email invalide", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidPhone(p)) {
                Toast.makeText(this, "Téléphone invalide", Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedId = roleGroup.getCheckedRadioButtonId();
            RadioButton rb = findViewById(selectedId);
            String role = rb.getText().toString();
            boolean inserted = db.insertData(n, m, p, role);
            if (inserted)
                Toast.makeText(this, "Insertion avec succès", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Echec d'insertion", Toast.LENGTH_SHORT).show();
        });
        b2.setOnClickListener(v -> {
        Intent int1 = new Intent(MainActivity.this, ManagingActivity.class);
        startActivity(int1);
    });
}
    private boolean isValidEmail(String email) {return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();}
    private boolean isValidPhone(String phone) {return phone.matches("\\+216[0-9]{8}");}
}
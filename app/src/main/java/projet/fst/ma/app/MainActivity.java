package projet.fst.ma.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqliteanadroid.R;
import projet.fst.ma.app.classes.Etudiant;
import projet.fst.ma.app.service.EtudiantService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText nom;
    private EditText prenom;
    private Button add;

    private EditText id;
    private Button rechercher;
    private Button supprimer;
    private Button afficherTous;
    private TextView res;

    void clear() {
        nom.setText("");
        prenom.setText("");
        id.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EtudiantService es = new EtudiantService(this);

        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        add = (Button) findViewById(R.id.bn);

        id = (EditText) findViewById(R.id.id);
        rechercher = (Button) findViewById(R.id.load);
        supprimer = (Button) findViewById(R.id.delete);
        afficherTous = (Button) findViewById(R.id.btn_list);
        res = (TextView) findViewById(R.id.res);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = nom.getText().toString().trim();
                String p = prenom.getText().toString().trim();
                
                if(n.isEmpty() || p.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                es.create(new Etudiant(n, p));
                clear();
                Toast.makeText(MainActivity.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        afficherTous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Etudiant> list = es.findAll();
                if (list.isEmpty()) {
                    res.setText("Aucun étudiant dans la base.");
                    return;
                }
                
                StringBuilder sb = new StringBuilder("Liste des étudiants :\n\n");
                for (Etudiant e : list) {
                    sb.append("ID: ").append(e.getId())
                      .append(" - ").append(e.getNom())
                      .append(" ").append(e.getPrenom()).append("\n");
                }
                res.setText(sb.toString());
            }
        });

        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = id.getText().toString().trim();
                if (txt.isEmpty()) {
                    res.setText("");
                    Toast.makeText(MainActivity.this, "Saisir un ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                Etudiant e = es.findById(Integer.parseInt(txt));
                if (e == null) {
                    res.setText("Étudiant avec l'ID " + txt + " introuvable.");
                    Toast.makeText(MainActivity.this, "Introuvable", Toast.LENGTH_SHORT).show();
                    return;
                }

                res.setText("Résultat :\nID: " + e.getId() + "\nNom: " + e.getNom() + "\nPrénom: " + e.getPrenom());
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = id.getText().toString().trim();
                if (txt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Saisir un ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                Etudiant e = es.findById(Integer.parseInt(txt));
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Aucun étudiant trouvé pour cet ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                es.delete(e);
                res.setText("Étudiant ID " + txt + " supprimé.");
                id.setText("");
                Toast.makeText(MainActivity.this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

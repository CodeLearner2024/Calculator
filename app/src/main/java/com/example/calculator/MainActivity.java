package com.example.calculator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    // Vues
    private EditText tvResultat;
    private TextView tvExpression;

    // Logique
    private Calculator calculator = new Calculator();
    private String currentInput = "";
    private double premierNombre = 0;
    private String operateur = "";
    private boolean operateurClique = false;
    private String expression = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvResultat = findViewById(R.id.tvResultat);
        tvExpression = findViewById(R.id.tvExpression);

        // Boutons chiffres
        findViewById(R.id.btn0).setOnClickListener(v -> onChiffreClique("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> onChiffreClique("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> onChiffreClique("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> onChiffreClique("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> onChiffreClique("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> onChiffreClique("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> onChiffreClique("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> onChiffreClique("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> onChiffreClique("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> onChiffreClique("9"));

        // Boutons opérateurs
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperateurClique("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> onOperateurClique("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> onOperateurClique("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> onOperateurClique("/"));

        // Boutons = et C
        findViewById(R.id.btnEqual).setOnClickListener(v -> onEgalClique());
        findViewById(R.id.btnClear).setOnClickListener(v -> onClearClique());

        // Boutons +/- et parenthèses
        findViewById(R.id.btn_vide1).setOnClickListener(v -> onSigneClique());
        findViewById(R.id.btnParenOuv).setOnClickListener(v -> onParentheseClique("("));
        findViewById(R.id.btnParenFerm).setOnClickListener(v -> onParentheseClique(")"));

        // Bouton backspace — clic simple = un caractère, clic long = tout effacer
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackspaceClique());
        findViewById(R.id.btnBack).setOnLongClickListener(v -> {
            onClearClique();
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void onChiffreClique(String chiffre) {
        int cursor = tvResultat.getSelectionStart();
        if (cursor < 0) cursor = expression.length();

        expression = expression.substring(0, cursor) + chiffre + expression.substring(cursor);
        currentInput = chiffre;
        tvExpression.setText("");
        tvResultat.setText(expression);

        // remet le curseur après le chiffre inséré
        tvResultat.setSelection(cursor + 1);
    }

    private void onOperateurClique(String op) {
        if (!currentInput.isEmpty()) {
            premierNombre = Double.parseDouble(currentInput);
            operateur = op;
            currentInput = "";
            expression += " " + op + " "; // ← ajoute l'opérateur dans l'affichage
            tvResultat.setText(expression);
        }
    }
    private void onEgalClique() {
        if (!expression.isEmpty()) {
            double resultat = calculator.calculer(expression);
            if (Double.isNaN(resultat)) {
                tvResultat.setText("Erreur");
                tvExpression.setText(expression);
            } else {
                tvExpression.setText(expression);        // expression monte en petit
                tvResultat.setText(String.valueOf(resultat)); // résultat en grand
                currentInput = String.valueOf(resultat);
                expression = currentInput;
                operateur = "";
            }
        }
    }

    private void onClearClique() {
        currentInput = "";
        premierNombre = 0;
        operateur = "";
        operateurClique = false;
        expression = "";
        tvResultat.setText("0");
        tvExpression.setText(""); // ← efface aussi l'expression
    }

    private void onPourcentageClique() {
        if (!currentInput.isEmpty()) {
            double valeur = Double.parseDouble(currentInput);
            valeur = valeur / 100;
            currentInput = String.valueOf(valeur);
            expression = expression.substring(0, expression.length() - currentInput.length()) + currentInput;
            tvResultat.setText(expression);
        }
    }

    private void onSigneClique() {
        if (!currentInput.isEmpty()) {
            double valeur = Double.parseDouble(currentInput);
            valeur = valeur * -1;
            currentInput = String.valueOf(valeur);
            expression = expression.substring(0, expression.length() - currentInput.length()) + currentInput;
            tvResultat.setText(expression);
        }
    }

    private void onParentheseClique(String paren) {
        expression += paren;
        tvResultat.setText(expression);
    }

    private void onBackspaceClique() {
        int cursor = tvResultat.getSelectionStart();
        if (cursor > 0 && !expression.isEmpty()) {
            expression = expression.substring(0, cursor - 1) + expression.substring(cursor);
            tvResultat.setText(expression.isEmpty() ? "0" : expression);
            tvResultat.setSelection(cursor - 1); // recule le curseur
            if (expression.isEmpty()) currentInput = "";
        }
    }
}
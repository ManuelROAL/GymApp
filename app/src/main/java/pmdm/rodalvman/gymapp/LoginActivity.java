package pmdm.rodalvman.gymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LoginActivity extends AppCompatActivity implements TarefaDescargaXML.Cliente{

    private static final int LOGIN = 1;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("gymapp", MODE_PRIVATE);
    }

    public void confirmarLogin(View v) {
        String login;
        EditText etLoginUsuario = (EditText) findViewById(R.id.etLoginUser);
        EditText etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        TarefaDescargaXML tdx = new TarefaDescargaXML( LoginActivity.this, LOGIN);
        login = etLoginUsuario.getText().toString();
        String password = etLoginPassword.getText().toString();

        //Gardamos en SharedPreferences o login e a password co que facemos este
        //intento de login
        // O valor de credenciaisValidadas a false indica que non se sabe aínda
        // se esta combinación login e password son válidas
        sp.edit().putString("login", login)
                .putString("password", password)
                .putBoolean("credenciaisValidadas", false).commit();

        tdx.execute(Servizo.urlLogin(login, password));
    }

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        //Comprobar que resultado non é null, se o é avisamos ó usuario e non continuamos
        if (resultado == null) {
            Toast.makeText(this, "Problemas de conexión", Toast.LENGTH_SHORT).show();
            return;
        }

        Element raiz = resultado.getDocumentElement();

        if (tipoDescarga == LOGIN) {
            if (raiz.getTagName().equalsIgnoreCase("resultado")) {
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();

                if (raiz.getTextContent().equalsIgnoreCase("Login correcto")) {
                    //Anoto en SharedPreferences que as credenciais son válidas
                    sp.edit().putBoolean("credenciaisValidadas", true).commit();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Se o login non foi correcto, volvemos a pedir as credenciais ó usuario
                    // pero antes temos que borrar o login e password utilizados neste intento
                    // e que están gardados en SharedPreferences
                    //sp.edit().remove("login").remove("password").commit();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}

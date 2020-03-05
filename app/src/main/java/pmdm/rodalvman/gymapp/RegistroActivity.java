package pmdm.rodalvman.gymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RegistroActivity extends AppCompatActivity implements TarefaDescargaXML.Cliente {

    private static final int REGISTRO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void btRegistroOK_OnClick(View v) {
        EditText etRegistroUsuario = (EditText) findViewById(R.id.etRegistroUser);
        EditText etRegistroPassword = (EditText) findViewById(R.id.etRegistroPassword);
        TarefaDescargaXML tdx = new TarefaDescargaXML( RegistroActivity.this, REGISTRO);
        String registro = etRegistroUsuario.getText().toString();
        String password = etRegistroPassword.getText().toString();
        if (registro.equals("") || registro.equals("")) {
            Toast.makeText(this, "Usuario o password incompletos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        } else {
            tdx.execute(Servizo.urlRegistro(registro, password));
        }
    }

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        //Comprobar que resultado non é null, se o é avisamos ó usuario e non continuamos
        if (resultado == null) {
            Toast.makeText(this, "Problemas de conexión", Toast.LENGTH_SHORT).show();
            return;
        }

        Element raiz = resultado.getDocumentElement();

        if (tipoDescarga == REGISTRO) {
            if (raiz.getTagName().equalsIgnoreCase("resultado")) {
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();
                if (raiz.getTextContent().equalsIgnoreCase("Registro realizado correctamente")) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, RegistroActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}

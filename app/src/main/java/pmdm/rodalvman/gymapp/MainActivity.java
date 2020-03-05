package pmdm.rodalvman.gymapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.CookieManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TarefaDescargaXML.Cliente {

    private static final int LOGIN = 1;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sp = getSharedPreferences("gymapp", MODE_PRIVATE);

        CookieManager.setDefault(new CookieManager());

        login();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void login() {
        String login = sp.getString("login", null);
        boolean credenciaisValidadas = sp.getBoolean("credenciaisValidadas", false);

        // Se login é null teño que pedir nome de usuario e password ó usuario
        // lanzando o correspondente Activity


        if (login == null || !credenciaisValidadas) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
             //Inicio sesión co login e password que están en SharedPreferences
            String password = sp.getString("password", null);
            TarefaDescargaXML tdx = new TarefaDescargaXML(MainActivity.this, LOGIN);
            tdx.execute(Servizo.urlLogin(login, password));
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

        if (tipoDescarga == LOGIN) {
            if (raiz.getTagName().equalsIgnoreCase("resultado")) {
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();

                if (raiz.getTextContent().equalsIgnoreCase("Login correcto")) {
                    //Anoto en SharedPreferences que as credenciais son válidas
                    sp.edit().putBoolean("credenciaisValidadas", true).commit();
                } else {
                    // Se o login non foi correcto, volvemos a pedir as credenciais ó usuario
                    // pero antes temos que borrar o login e password utilizados neste intento
                    // e que están gardados en SharedPreferences
                    //sp.edit().remove("login").remove("password").commit();

                    login();
                }
            }
        }
    }
}


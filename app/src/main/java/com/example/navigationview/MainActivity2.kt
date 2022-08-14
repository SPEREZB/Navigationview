package com.example.navigationview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity2 : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    var drawerLayout: DrawerLayout? = null
    var  toolbar: Toolbar? = null

    lateinit var nV: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        toolbar = findViewById<Toolbar>(R.id.toolbar);
        val bundle = this.getIntent().getExtras();

        //urls
        var urluser="https://apimocha.com/13811500/cuentas";
        var urldatos="https://apimocha.com/13811500/datos";

        //header
        nV = findViewById(R.id.nav_view)
        var header = nV.getHeaderView(0);
        var imagen=header.findViewById<CircleImageView>(R.id.ic_cuenta)
        var nombre=header.findViewById<TextView>(R.id.nombre);
        var header_fondo=header.findViewById<ImageView>(R.id.fondo_header);


        //datos
        var menu=nV.getMenu();
        var tipo_cuenta="";
        var ingreso=false;

        //volley
        val q = Volley.newRequestQueue(this)
        val q2 = Volley.newRequestQueue(this)
        // JSON
        val jsonObj = JsonObjectRequest(
            Request.Method.GET, urluser, null,
            { response ->
                for (i in 0 until response.length()) {
                    var objeto=response.getJSONArray((i+1).toString()).getJSONObject(0)
                    if(bundle?.getString("user")==objeto.getString("user") && bundle?.getString("password")==objeto.getString("password")){

                        nombre.setText(objeto.getString("user"));
                        Picasso.get().load(objeto.getString("imagen")).into(imagen);
                        toolbar!!.title=objeto.getString("user").toUpperCase()
                        setSupportActionBar(toolbar);
                        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_menu)
                        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

                        //SE VERIFICA CON QUE TIPO DE CUENTA ESTA INGRESANDO
                        ingreso=true;
                        if(bundle?.getString("user")=="admin"){
                            tipo_cuenta="datos_admin"
                            header_fondo.setImageResource(R.drawable.fheader)
                        }else if(bundle?.getString("user")=="client"){
                            tipo_cuenta="datos_client"
                            header_fondo.setImageResource(R.drawable.fheader2)
                        }
                        //SE MODIFICARA LOS ITEMS DEL MENU DEPENDIENDO CON
                        //LA CUENTA QUE HAYA INGRESADO

                        val jsonObjectRequest = JsonObjectRequest(
                            Request.Method.GET, urldatos, null,
                            { response ->
                                var item=response.getJSONArray(tipo_cuenta)
                                var objeto=item.getJSONObject(0)
                                for (i in 0 until objeto.length()){
                                    var item=objeto.getString("item"+(i+1))
                                    menu.add(item)
                                }
                            },
                            { error ->

                            }
                        )
                        q2.add(jsonObjectRequest);
                    }
                }
                //EN CASO DE NO INGRESAR LAS CREDENCIALES CORRECTAS SE QUEDARA EN
                //EL MAINACTIVITY Y NO IRA AL MAINACTIVITY2
                if(ingreso==false){
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                }
            },
            { error ->

            }
        )
        q.add(jsonObj);
        nV.setNavigationItemSelectedListener(this);
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.title) {
            "Salir" ->{
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }
        }
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

            item.setChecked(true)
            getSupportActionBar()?.setTitle(item.getTitle());
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
                drawerLayout?.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
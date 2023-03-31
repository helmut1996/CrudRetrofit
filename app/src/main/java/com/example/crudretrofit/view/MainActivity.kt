package com.example.crudretrofit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudretrofit.Adapter.OnItemClicked
import com.example.crudretrofit.Adapter.UsuarioAdapter
import com.example.crudretrofit.R
import com.example.crudretrofit.api.RetrofitCliente
import com.example.crudretrofit.databinding.ActivityMainBinding
import com.example.crudretrofit.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnItemClicked {
    lateinit var binding: ActivityMainBinding
    lateinit var adaptador:UsuarioAdapter

    var listaUsuarios = arrayListOf<Usuario>()
    var usuario= Usuario(-1,"","")
    var isEditable= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clientesList.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()
        obtenerUsuarios()

        binding.btnAgregar.setOnClickListener {
            var isValido = validarCampos()
            if (isValido){
                if (!isEditable){
                    agregarUsuario()
                }else{
                    actualizarUsuario()
                }
            }
        }

        binding.btnBuscar.addTextChangedListener{ userFilter->
          val listaUsuariosFilter= listaUsuarios.filter { user-> user.nombre.lowercase().contains(userFilter.toString().lowercase()) }
            adaptador.filterUsuarios(listaUsuariosFilter as ArrayList<Usuario>)
        }
    }



    private fun validarCampos(): Boolean {
        return !(binding.tvnombre.text.isNullOrEmpty() || binding.tvemail.text.isNullOrEmpty())
    }

    private fun obtenerUsuarios() {
        CoroutineScope(Dispatchers.IO).launch {
            val calL= RetrofitCliente.webService.obtenerUsuarios()
            runOnUiThread{
                if (calL.isSuccessful){
                    listaUsuarios = calL.body()!!.listaUsuarios
                    setupRecyclerView()
                }else{
                    Toast.makeText(this@MainActivity,"ERROR AL CONSULTAR LISTA DE USUARIOS",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun agregarUsuario() {
        this.usuario.nombre= binding.tvnombre.text.toString()
        this.usuario.email= binding.tvemail.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitCliente.webService.agregarUsuario(usuario)
            runOnUiThread {
                if (call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                    obtenerUsuarios()
                    limpiarCampos()
                    limpiarObjecto()
                }else{
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun actualizarUsuario(){
        this.usuario.nombre= binding.tvnombre.text.toString()
        this.usuario.email= binding.tvemail.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitCliente.webService.actualizarUsuario(usuario.idUsuario, usuario)
            runOnUiThread {
                if (call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                    obtenerUsuarios()
                    limpiarCampos()
                    limpiarObjecto()
                    binding.btnAgregar.setText("Agregar Usuario")
                    binding.btnAgregar.backgroundTintList = resources.getColorStateList(R.color.green)
                    isEditable= false
                }else{
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun limpiarObjecto() {
        this.usuario.idUsuario = -1
        this.usuario.nombre =""
        this.usuario.email =""
    }

    private fun limpiarCampos() {
        binding.tvnombre.setText("")
        binding.tvemail.setText("")
    }

    private fun setupRecyclerView() {
        adaptador= UsuarioAdapter(this, listaUsuarios)
        adaptador.setOnClick(this@MainActivity)
        binding.clientesList.adapter = adaptador
    }

    override fun editarUsuario(usuario: Usuario) {
        binding.tvnombre.setText(usuario.nombre)
        binding.tvemail.setText(usuario.email)
        binding.btnAgregar.backgroundTintList = resources.getColorStateList(R.color.purple_500)
        this.usuario = usuario
        isEditable = true
    }

    override fun eliminarUsuario(idUsuario: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitCliente.webService.borrarUsuario(idUsuario)
            runOnUiThread {
                if (call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                    obtenerUsuarios()
                }
            }
        }

    }
}
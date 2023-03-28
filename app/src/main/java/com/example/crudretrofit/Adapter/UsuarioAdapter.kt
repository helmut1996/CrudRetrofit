package com.example.crudretrofit.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.crudretrofit.R
import com.example.crudretrofit.model.Usuario

class UsuarioAdapter(
    var context:Context,
    var listaUsuarios:ArrayList<Usuario>
):RecyclerView.Adapter<UsuarioAdapter.UsuarioHolder>() {

    private var onClick:OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_client,parent,false)
        return UsuarioHolder(view)
    }

    override fun getItemCount(): Int = listaUsuarios.size

    override fun onBindViewHolder(holder: UsuarioHolder, position: Int) {
        val usuario = listaUsuarios.get(position)
        holder.tvIdusuario.text= usuario.idUsuario.toString()
        holder.tvname.text= usuario.nombre
        holder.tvemail.text= usuario.email

        holder.btnEditar.setOnClickListener {
            onClick?.editarUsuario(usuario)
        }
        holder.btnEliminar.setOnClickListener {
            onClick?.eliminarUsuario(usuario.idUsuario)
        }
    }
    inner class UsuarioHolder(itemView: View):ViewHolder(itemView) {
        val tvIdusuario = itemView.findViewById(R.id.tvIdUsuario) as TextView
        val tvname = itemView.findViewById(R.id.name) as TextView
        val tvemail = itemView.findViewById(R.id.email) as TextView
        val btnEditar = itemView.findViewById(R.id.btn_editar) as Button
        val  btnEliminar = itemView.findViewById(R.id.btn_eliminar) as Button
    }

    fun setOnClick(onClick:OnItemClicked?){
        this.onClick = onClick
    }

    fun filterUsuarios( listaUsuarios:ArrayList<Usuario>){
        this.listaUsuarios = listaUsuarios
        notifyDataSetChanged()

    }
}
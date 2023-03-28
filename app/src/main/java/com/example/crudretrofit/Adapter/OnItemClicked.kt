package com.example.crudretrofit.Adapter

import com.example.crudretrofit.model.Usuario

interface OnItemClicked {
    fun editarUsuario(usuario: Usuario)

    fun eliminarUsuario(idUsuario: Int)
}

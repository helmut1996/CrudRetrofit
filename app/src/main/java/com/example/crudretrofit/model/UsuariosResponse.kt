package com.example.crudretrofit.model

import com.google.gson.annotations.SerializedName

data class UsuariosResponse(
    @SerializedName("listaUsuarios") var listaUsuarios: ArrayList<Usuario>
)

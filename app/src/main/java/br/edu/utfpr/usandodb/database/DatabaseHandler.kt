package br.edu.utfpr.usandodb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import br.edu.utfpr.usandodb.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class DatabaseHandler(context: Context) {

    private val banco = Firebase.firestore

    companion object {
        private const val TABLE_NAME = "cadastro"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val TELEFONE = "telefone"
    }

    suspend fun incluir(cadastro: Cadastro) {

        val registro = hashMapOf(
            NOME to cadastro.nome,
            TELEFONE to cadastro.telefone
        )

        banco
            .collection( TABLE_NAME )
            .document(cadastro.id.toString())
            .set( registro )
            .await()
    }

    suspend fun alterar(cadastro: Cadastro) {

        val registro = hashMapOf(
            NOME to cadastro.nome,
            TELEFONE to cadastro.telefone
        )

        banco
            .collection( TABLE_NAME )
            .document(cadastro.id.toString())
            .set( registro )
            .await()

    }


    suspend fun excluir(id: Int) {

        banco
            .collection( TABLE_NAME )
            .document(id.toString())
            .delete()
            .await()

    }

    suspend fun pesquisar(id: Int): Cadastro? {

        val documento = banco
            .collection(TABLE_NAME)
            .document(id.toString())
            .get()
            .await()

        if (documento.exists()) {
            val cadastro = Cadastro(
                id,
                documento.get(NOME).toString(),
                documento.get(TELEFONE).toString()
            )
            return cadastro
        } else {
            return null
        }

    }

    suspend fun listar(): MutableList<Cadastro> {

        val documentos = banco
            .collection(TABLE_NAME)
            .get()
            .await()

        val lista = mutableListOf<Cadastro>()

        for (documento in documentos) {
            val cadastro = Cadastro(
                documento.id.toInt(),
                documento.get(NOME).toString(),
                documento.get(TELEFONE).toString()
            )
            lista.add(cadastro)
        }

        return lista

    }

} //fim do DatabaseHandler
package com.example.myshoppinglist.ui.lists.items

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.example.myshoppinglist.TAG
import com.example.myshoppinglist.models.Item
import com.example.myshoppinglist.models.ListItems
import com.example.myshoppinglist.repositories.ItemRepository

data class AddItemState(
    val name : String = "",
    val qtd : Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AddItemViewModel : ViewModel(){

    var state = mutableStateOf(AddItemState())
        private set

    fun onNameChange(name: String) {
        state.value = state.value.copy(name = name)
    }

    fun onQttChange(qtd: Double) {
        state.value = state.value.copy(qtd = qtd)
    }

    fun addItem(listId: String){

        val item = Item(
            "",
            state.value.name,
            state.value.qtd
        )

        ItemRepository.addItem(listId, item)
    }

}
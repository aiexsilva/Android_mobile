package com.example.myshoppinglist.ui.lists

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.example.myshoppinglist.TAG
import com.example.myshoppinglist.models.ListItems
import com.example.myshoppinglist.repositories.ListItemsRepository

data class AddListState(
    val name : String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AddListViewModel : ViewModel(){

    var state = mutableStateOf(AddListState())
        private set

    fun onNameChange(name: String) {
        state.value = state.value.copy(name = name)
    }

    fun addList(){

        val listItems = ListItems(
            "",
            state.value.name,
            null,
        )

        ListItemsRepository.addList(listItems)
    }

}
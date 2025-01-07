package com.example.myshoppinglist.ui.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.example.myshoppinglist.TAG
import com.example.myshoppinglist.models.ListItems
import com.example.myshoppinglist.models.User
import com.example.myshoppinglist.repositories.ListItemsRepository
import com.example.myshoppinglist.repositories.UserRepository

data class ProfileState(
    val user : User? =  null,
    var name : String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel(){

    var state = mutableStateOf(ProfileState())
        private set

    fun onNameChange(name: String) {
        state.value = state.value.copy(name = name)
    }

    fun getUser(){
        UserRepository.get { user ->
            state.value = state.value.copy(
                user = user,
                name = user.name
            )
        }
    }

    fun saveUser(){
        var user = state.value.user
        if (user == null){
            user = User(state.value.name)
        }else{
            user.name = state.value.name
        }

        UserRepository.save(user = user)
    }


}
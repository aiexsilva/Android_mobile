package com.example.myshoppinglist.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.example.myshoppinglist.TAG
import com.example.myshoppinglist.models.ListItems
import com.example.myshoppinglist.models.User

object ListItemsRepository {

    val db = Firebase.firestore
    val auth = Firebase.auth

    fun getLists(onResult : (List<ListItems>) -> Unit ){

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        db.collection("lists")
            .whereArrayContains("owners", userId!!)
            .get()
            .addOnSuccessListener { documents ->
                val listItemsList = arrayListOf<ListItems>()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val listItem = document.toObject(ListItems::class.java)
                    listItem.docId = document.id
                    listItemsList.add(listItem)
                }
                onResult(listItemsList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun addList(listItems: ListItems){

        val db = Firebase.firestore

        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        listItems.owners = arrayListOf(userId?:"")

        db.collection("lists")
            .add(listItems)
            .addOnSuccessListener { documentReference ->
                Log.d("lol", "DocumentSnapshot written with ID: ${documentReference.id}")
                val docId = documentReference.id
                db.collection("lists")
                    .document(docId)
                    .update("docId", docId)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun addUserToList(listId: String, docId: String) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val userId = currentUser?.uid



        db.collection("lists")
            .document(listId)
            .get()
            .addOnSuccessListener { document ->
                val listItems = document.toObject(ListItems::class.java)
                listItems?.docId = document.id
                var owners = arrayListOf<String>()
                if( listItems?.owners.isNullOrEmpty() ) {
                    listItems?.owners = arrayListOf(userId?:"")
                }else{
                    var o = listItems?.owners?.filter { l ->
                        l == userId
                    }
                    if (o.isNullOrEmpty()){
                        owners = (listItems!!.owners!! + arrayListOf(userId?:"")) as ArrayList<String>
                    }
                }
                db.collection("lists")
                    .document(listId)
                    .update("owners", owners)

            }
    }

    fun deleteList(listId: String) {
        val db = Firebase.firestore

        Log.d("listitem", "$listId")

        db.collection("lists")
            .document(listId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "List deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to delete list: ${e.message}")
            }
    }

}
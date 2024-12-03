package com.example.projects.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.disk.DiskCache
import com.example.projects.models.Client
import com.example.projects.navigation.ROUTE_VIEW_CLIENT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ClientViewModel() :ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: MutableLiveData<String>
        get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: MutableLiveData<String>
        get() = _successMessage

    fun saveClient(
        firstname: String, lastname: String, gender: String,
        age: String, id: String, navController: NavController, context: Context
    ) {  //check id
        @Suppress("NAME_SHADOWING")
        val id = System.currentTimeMillis().toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("client/$id")

        val clientData = Client(firstname, lastname, gender, age, id)

        dbRef.setValue(clientData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Client added successfully", context)
                    navController.navigate(ROUTE_VIEW_CLIENT)

                } else {
                    showToast("Client not added successfully", context)
                }

            }
    }

    fun viewClients(
        client: MutableState<Client>,
        clients: SnapshotStateList<Client>,                                        //check on changes in database at specific intervals
        context: Context
    ):
            SnapshotStateList<Client> {
        val ref = FirebaseDatabase.getInstance()
            .getReference()                               //check on table 'client'
            .child("Client")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {                              //each time in database it clears at it populates from mutablestate
                clients.clear()
                for (snap in snapshot.children) {                                             // children,items
                    val value = snap.getValue(Client::class.java)
                    client.value = value!!
                    clients.add(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch clients", context)
            }
        })
        return clients
    }
    fun updateClient(context: Context,navController: NavController,
                     firstname: String,lastname: String,gender: String,
                     age: String,id: String){
        val databaseReference =FirebaseDatabase.getInstance()
            .getReference("Client/$id")
        val updatedClient = Client("",firstname,lastname,gender,age,id)

        databaseReference.setValue(updatedClient)
            .addOnCompleteListener { task->                                                 //check if updated
                if (task.isSuccessful) {
                    showToast("Client Updated Successfully", context)
                    navController.navigate(ROUTE_VIEW_CLIENT)
                }else{
                    showToast("Record update failed",context)
                }
            }
    }
    fun deleteClient(context: Context,id: String,                    // can alternatively add a variable but will delete without confirmation
                     navController: NavController){
        AlertDialog.Builder(context)
            .setTitle("Delete the client")
            .setMessage("Are you sure you want to delete this client")
            .setPositiveButton("Yes"){ _,_ ->
                val databaseReference=FirebaseDatabase.getInstance()
                    .getReference("Client/$id")
                databaseReference.removeValue().addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        showToast("Client delete Successfully", context)
                    }else{
                        showToast("Client not deleted",context)
                    }
                }

            }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()

            }
            .show()
    }


    public fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

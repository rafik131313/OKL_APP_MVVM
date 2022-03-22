package com.example.okl_app_mvvm.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.adapters.MyAdapter
import com.example.okl_app_mvvm.databinding.ActivityScoreBoardBinding
import com.example.okl_app_mvvm.other.Constants
import com.example.okl_app_mvvm.other.Account
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val REQUEST_CODE_SIGN_IN = 0

@AndroidEntryPoint
class ScoreBoard : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityScoreBoardBinding

    private lateinit var accountArrayList: ArrayList<Account>
    private lateinit var myAdapter: MyAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences



    private var accountCollectionRef = Firebase.firestore.collection("accounts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)

        auth = FirebaseAuth.getInstance()


        binding.tvRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tvRecyclerView.setHasFixedSize(true)
        accountArrayList = arrayListOf()
        myAdapter = MyAdapter(accountArrayList)
        binding.tvRecyclerView.adapter = myAdapter


        val currentUser = auth.currentUser
        val currentMail = auth.currentUser?.email
        val currentName = auth.currentUser?.displayName
        val currentUserData = Account(currentMail.toString(), currentName.toString(), pointsOverall)

        if (currentUser != null) {
            val query = accountCollectionRef
                .whereEqualTo("mail", currentMail)
                .get()
            query.addOnSuccessListener {
                for (document in it) {
/*                    Toast.makeText(this, "Zaktualizowano dane", Toast.LENGTH_SHORT).show()*/
                    accountCollectionRef.document(document.id)
                        .set(currentUserData, SetOptions.merge())
                    changeUI()
                }
            }

        } else {
            binding.signInGoogle.setOnClickListener {
                val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.client_id))
                    .requestEmail()
                    .build()
                val signInClient = GoogleSignIn.getClient(this, options)
                signInClient.signInIntent.also {
                    startActivityForResult(it, REQUEST_CODE_SIGN_IN)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)

            }
        }
        else
        {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeUI() {
        binding.l1.isVisible = false
        binding.tvRecyclerView.isVisible = true
        (resources.getString(R.string.you_are_logged) + " " + (auth.currentUser?.email)).also {
            binding.whoIsLogged.text = it
        }
        updateInRealTime()
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    val emailLogged = account.email.toString()
                    val name = account.displayName.toString()
                    val saved = Account(emailLogged, name, pointsOverall)
                    updateInRealTime()
                    saveAccount(saved)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ScoreBoard, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun saveAccount(account: Account) = CoroutineScope(Dispatchers.IO).launch {
        try {
            var toCheck = account.mail
            val querySnapshot = accountCollectionRef
                .whereEqualTo("mail", toCheck)
                .get()
                .await()
            val sb = java.lang.StringBuilder()
            for (document in querySnapshot.documents) {
                val account = document.toObject(Account::class.java)
                sb.append("${account?.mail}")
            }
            if (sb.isEmpty()) {
                accountCollectionRef.add(account).await()
                withContext(Dispatchers.Main) {
/*                    Toast.makeText(this@ScoreBoard, "Dodano dane! + $sb", Toast.LENGTH_SHORT).show()*/
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            } else {
                withContext(Dispatchers.Main) {
/*                    Toast.makeText(this@ScoreBoard, "Podany mail już istnieje!", Toast.LENGTH_SHORT).show()*/
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ScoreBoard, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateInRealTime() {
        accountCollectionRef
            .orderBy("punkty", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    for (dc: DocumentChange in it.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            accountArrayList.add(dc.document.toObject(Account::class.java))
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
    }
}
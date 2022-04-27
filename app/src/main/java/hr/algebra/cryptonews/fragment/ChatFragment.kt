package hr.algebra.cryptonews.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import hr.algebra.cryptonews.BuildConfig
import hr.algebra.cryptonews.adapter.MessageAdapter
import hr.algebra.cryptonews.databinding.FragmentChatBinding
import hr.algebra.cryptonews.model.Message


class ChatFragment : Fragment() {

    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var binding: FragmentChatBinding
    private lateinit var messages: ArrayList<Message>

    private val TOPIC = "general"
    private val CHAT = "chat"
    private val MESSAGE = "message"


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Log.w("TAG", "Google sign in failed", e)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.signInButton.setOnClickListener { signIn() }
        binding.signOutButton.setOnClickListener { signOut() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        mSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        messages = ArrayList()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        setListeners()
        adjustUI(mFirebaseAuth.currentUser != null)
        return binding.root
    }

    private fun setListeners() {
        mDatabase.child(CHAT).child(TOPIC).child(MESSAGE).limitToLast(20)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (postSnapshot in snapshot.children) {
                        messages.add(postSnapshot.getValue(Message::class.java)!!)
                    }

                    binding.rvMessages.apply {
                        layoutManager = LinearLayoutManager(context).apply {
                            stackFromEnd = true
                        }
                        adapter = MessageAdapter(context, messages)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("DB_ERROR", "DB error")
                }
            })

        binding.sendButton.setOnClickListener {

            if (binding.messageBox.text.trim().isNotEmpty()) {

                mDatabase.child(CHAT).child(TOPIC).child(MESSAGE).push()
                    .setValue(
                        Message(
                            mFirebaseAuth.currentUser!!.uid,
                            binding.messageBox.text.trim().toString(),
                            mFirebaseAuth.currentUser!!.photoUrl!!.toString()
                        )
                    ).addOnSuccessListener {
                        binding.messageBox.text.clear()
                    }
            }
        }
    }


    private fun signOut() {
        mFirebaseAuth.signOut()
        mSignInClient.signOut()
        adjustUI(false)
    }

    private fun signIn() {
        val signInIntent = mSignInClient.signInIntent
        launcher.launch(signInIntent)
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                adjustUI(true)
            }
    }

    private fun adjustUI(signedIn: Boolean) {
        when (signedIn) {
            true -> {
                binding.signInButton.visibility = View.GONE
                binding.signOutButton.visibility = View.VISIBLE
                binding.footer.visibility = View.VISIBLE
                binding.tvUsername.text = mFirebaseAuth.currentUser!!.displayName
            }
            false -> {
                binding.signInButton.visibility = View.VISIBLE
                binding.signOutButton.visibility = View.GONE
                binding.footer.visibility = View.GONE
                binding.tvUsername.text = ""
            }
        }

    }

}
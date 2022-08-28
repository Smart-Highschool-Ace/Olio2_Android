package org.gsm.olio.view.fragment.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.gsm.olio.MyApplication
import org.gsm.olio.R
import org.gsm.olio.databinding.FragmentMainBinding
import org.gsm.olio.util.Constants.TAG

class MainFragment : Fragment() {
     private val binding by lazy {FragmentMainBinding.inflate(layoutInflater)}
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var requestActivity: ActivityResultLauncher<Intent>


    override fun onStart() {
        val token = MyApplication.prefs.token

        Log.d(TAG, "MainFragment: $token")
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(binding) {
        lifecycleOwner = this@MainFragment
        fragment = this@MainFragment
        initGoolgLogin()


        return root
    }

    fun signOut(){
        googleSignInClient.signOut().addOnCanceledListener {
            Toast.makeText(requireContext(), "로그아웃", Toast.LENGTH_SHORT).show()
        }
        googleSignInClient.revokeAccess()
        findNavController().navigateUp()
    }

    private fun initGoolgLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

    }

}
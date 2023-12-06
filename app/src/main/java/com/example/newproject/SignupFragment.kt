package com.example.newproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newproject.databinding.FragmentSignupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SignupFragment: Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(binding.root.context, "회원가입 성공.", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(binding.root.context, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
                        } else if (exception is FirebaseAuthWeakPasswordException) {
                            Toast.makeText(binding.root.context, "비밀번호가 너무 약합니다.", Toast.LENGTH_SHORT).show()
                        } else if (exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(binding.root.context, "유효하지 않은 이메일 형식입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(binding.root.context, "회원가입 실패: $exception", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
    }
}
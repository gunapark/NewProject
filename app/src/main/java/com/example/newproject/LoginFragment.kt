package com.example.newproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newproject.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(binding.root.context, "로그인 성공", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        val exception = task.exception
                        when (exception) {
                            is FirebaseAuthInvalidUserException -> {
                                when (exception.errorCode) {
                                    "ERROR_USER_NOT_FOUND" ->
                                        Toast.makeText(binding.root.context, "등록되지 않은 이메일입니다.", Toast.LENGTH_SHORT).show()

                                    "ERROR_WRONG_PASSWORD" ->
                                        Toast.makeText(binding.root.context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(binding.root.context, "유효하지 않은 이메일 형식입니다.", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                Toast.makeText(binding.root.context, "로그인 실패: $exception", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }

        binding.btnGotoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
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
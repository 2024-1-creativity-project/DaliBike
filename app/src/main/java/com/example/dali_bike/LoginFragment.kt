package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.databinding.FragmentLoginBinding
import com.example.dali_bike.models.LoginRequest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var _binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val rootView = _binding.root

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val joinBtn: Button = view.findViewById(R.id.join_btn)

        joinBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }

        val loginBtn: Button = view.findViewById(R.id.login_btn)
        val editId: EditText = view.findViewById(R.id.edit_ID)
        val editPw: EditText = view.findViewById(R.id.edit_PW)

        //버튼 클릭 시 POST 요청 수행
        loginBtn.setOnClickListener {
            //입력 값 가져옴
            val id = editId.text.toString()
            val pw = editPw.text.toString()

            //유효성 검사
            if (id.isEmpty()) {
                editId.error = "ID를 입력하세요"
                return@setOnClickListener
            }

            if (pw.isEmpty()) {
                editPw.error = "비밀번호를 입력하세요"
                return@setOnClickListener
            }

            //입력 값으로 LoginRequest 객체 생성
//            val loginRequest = LoginRequest(USERId = id, Password = pw)
//                try {
//                    val user = apiService.loginUser(loginRequest)
//                    Toast.makeText(context, "${user.Name}님 환영합니다!", Toast.LENGTH_LONG).show()
//                }
//                catch (e: Exception) {
//                    e.printStackTrace()
//                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_LONG).show()
//                }
//            }
        }
    }
}

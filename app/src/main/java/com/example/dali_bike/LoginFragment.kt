package com.example.dali_bike

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            val loginRequest = LoginRequest(id = id, pw = pw)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.loginUser(loginRequest)


                    if (response.isSuccessful) {
                        val loginResList = response.body()
                        if (loginResList != null && loginResList.isNotEmpty()) {
                            val loginRes = loginResList[0]
                            withContext(Dispatchers.Main) {  // UI 작업을 메인 스레드에서 실행
                                if (loginRes.result == "true") {
                                    Toast.makeText(context, "환영합니다!", Toast.LENGTH_LONG).show()
                                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                                } else {
                                    Toast.makeText(context, "회원을 찾을 수 없습니다", Toast.LENGTH_LONG).show()
                                    editId.error = "아이디 혹은 비밀번호를 확인해주세요"
                                    editPw.error = "아이디 혹은 비밀번호를 확인해주세요"
                                }
                            }
                        }
                        else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "서버 응답이 올바르지 않습니다", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "재접속 후 로그인 바랍니다", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "로그인 실패: 네트워크 오류", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
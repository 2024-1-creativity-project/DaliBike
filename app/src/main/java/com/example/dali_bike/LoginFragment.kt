package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
//import androidx.lifecycle.lifecycleScope
import com.example.dali_bike.api.retrofit
import com.example.dali_bike.models.LoginRequest
import com.example.dali_bike.models.User
import kotlinx.coroutines.launch
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("/user/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): List<User>
}

val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginBtn: Button = view.findViewById(R.id.login_btn)
        val editId: EditText = view.findViewById(R.id.edit_ID)
        val editPw: EditText = view.findViewById(R.id.edit_PW)

        //버튼 클릭 시 POST 요청 수행
        loginBtn.setOnClickListener {
            //입력 값 가져옴
            val id = editId.text.toString()
            val pw = editPw.text.toString()

            //입력 값으로 LoginRequest 객체 생성
            val loginRequest = LoginRequest(USERId = id, Password = pw)

//            viewLifecycleOwner.lifecycleScope.launch {
//                try {
//                    val user = apiService.loginUser(loginRequest)
//                }
//                catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
        }
    }
}




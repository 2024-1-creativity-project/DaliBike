package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.databinding.FragmentJoinBinding
import com.example.dali_bike.models.Register
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinFragment : Fragment() {
    private lateinit var _binding: FragmentJoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        val rootView = _binding.root

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editName: EditText = view.findViewById(R.id.name_txt)
        val editNickname: EditText = view.findViewById(R.id.edit_nickName)
        val editID: EditText = view.findViewById(R.id.ID_edit)
        val editPW: EditText = view.findViewById(R.id.password_edit)
        val checkPW: EditText = view.findViewById(R.id.password_check)
        val editPhone: EditText = view.findViewById(R.id.phone_edit)

        val okBtn: Button = view.findViewById(R.id.ok_btn)
        val check1: Button = view.findViewById(R.id.check_btn1)
        val check2: Button = view.findViewById(R.id.check_btn2)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

        val backBtn: Button = view.findViewById(R.id.back_btn)

        var nickCheck = false
        var idCheck = false

        val id = editID.text.toString()
        val nickName = editNickname.text.toString()
        val name = editName.text.toString()
        val pw = editPW.text.toString()
        val pwCheck = checkPW.text.toString()
        val phone = editPhone.text.toString()


        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
        }

        check1.setOnClickListener {
            if (nickName.isEmpty()) {
                editNickname.error = "닉네임을 입력하세요"
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.checkNickName(nickName)

                    if (response.isSuccessful) {
                        val joinResList = response.body()
                        if (joinResList != null && joinResList.isNotEmpty()) {
                            val joinRes = joinResList[0]
                            withContext(Dispatchers.Main) {
                                if (joinRes.result == "true") {
                                    Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_LONG).show()
                                    nickCheck = true
                                } else {
                                    Toast.makeText(context, "사용할 수 없는 닉네임입니다", Toast.LENGTH_LONG).show()
                                    editNickname.error = "다른 닉네임을 입력하세요"
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
                            Toast.makeText(context, "재접속 후 다시 회원가입 바랍니다", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "중복체크 실패: 네트워크 오류", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        check2.setOnClickListener {
            if (id.isEmpty()) {
                editID.error = "아이디를 입력하세요"
                return@setOnClickListener
            }

            if (id.length < 4) {
                editID.error = "아이디는 최소 4자 이상이어야 합니다"
                return@setOnClickListener
            }

            if (id.length > 10) {
                editID.error = "아이디는 10자 이하여야 합니다"
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.checkId(id)

                    if (response.isSuccessful) {
                        val idResList = response.body()
                        if (idResList != null && idResList.isNotEmpty()) {
                            val idRes = idResList[0]
                            withContext(Dispatchers.Main) {
                                if (idRes.result == "true") {
                                    Toast.makeText(context, "사용 가능한 아이디입니다.", Toast.LENGTH_LONG).show()
                                    idCheck = true
                                } else {
                                    Toast.makeText(context, "사용할 수 없는 아이디입니다", Toast.LENGTH_LONG).show()
                                    editNickname.error = "다른 아이디를 입력하세요"
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
                            Toast.makeText(context, "재접속 후 다시 회원가입 바랍니다", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "중복체크 실패: 네트워크 오류", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        okBtn.setOnClickListener {
            if (name.isEmpty()) {
                editName.error = "이름을 입력하세요"
                return@setOnClickListener
            }

            if (pw.length < 8) {
                editPW.error = "비밀번호는 최소 8자 이상이어야 합니다"
                return@setOnClickListener
            }

            if (pw.length > 16) {
                editPW.error = "비밀번호는 16자 이하여야 합니다"
                return@setOnClickListener
            }

            if (pw != pwCheck) {
                checkPW.error = "비밀번호가 일치하지 않습니다"
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                editPhone.error = "전화번호를 입력하세요"
                return@setOnClickListener
            }

            if (phone.length < 11) {
                editPhone.error = "잘못된 전화번호입니다"
                return@setOnClickListener
            }

            if (!checkBox.isChecked) {
                checkBox.error = "체크되지 않았습니다"
                Toast.makeText(context, "동의하셔야 본 서비스 이용이 가능합니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!idCheck) {
                editID.error = "아이디 중복검사를 해야합니다"
                return@setOnClickListener
            }

            if (!nickCheck) {
                editNickname.error = "닉네임 중복검사를 해야합니다"
                return@setOnClickListener
            }

            val register = Register(id = id, pw = pw, phone = phone, name = name, nickname = nickName)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.register(register)

                    if (response.isSuccessful) {
                        val regiResList = response.body()
                        if (regiResList != null && regiResList.isNotEmpty()) {
                            val regiRes = regiResList[0]
                            withContext(Dispatchers.Main) {
                                if (regiRes.result == "true") {
                                    Toast.makeText(context, "환영합니다!", Toast.LENGTH_LONG).show()
                                    findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
                                } else {
                                    Toast.makeText(context, "회원가입 할 수 없습니다", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "서버 응답이 올바르지 않습니다", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "재접속 후 다시 회원가입 바랍니다", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "회원가입 실패: 네트워크 오류", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
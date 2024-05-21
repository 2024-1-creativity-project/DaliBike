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
import com.example.dali_bike.databinding.FragmentJoinBinding

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

        val nickCheck = false
        val idCheck = false


        check1.setOnClickListener {
            val nickName = editNickname.text.toString()

            if (nickName.isEmpty()) {
                editNickname.error = "닉네임을 입력하세요"
                return@setOnClickListener
            }

            //데베에서 닉네임 중복 검사

            //return값이 true일 경우 nickCheck = true로 바꿔주기

            //return값이 false일 경우 error = "새로운 닉네임을 입력하세요"
            //+ Toast.makeText(context, "중복된 닉네임입니다.", Toast.LENGTH_LONG).show()
        }

        check2.setOnClickListener {
            val id = editID.text.toString()

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

            //데베에서 아이디 중복 검사

            //return값이 true일 경우 idCheck = true로 바꿔주기

            //return값이 false일 경우 error = "새로운 아이디를 입력하세요"
            //+ Toast.makeText(context, "중복된 아이디입니다.", Toast.LENGTH_LONG).show()
        }

        okBtn.setOnClickListener {
            val name = editName.text.toString()

            if (name.isEmpty()) {
                editName.error = "이름을 입력하세요"
                return@setOnClickListener
            }

            val pw = editPW.text.toString()
            val pwCheck = checkPW.text.toString()

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

            val phone = editPhone.text.toString()

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

            findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
        }

        //다 되면 @PUT? @SerializedName도 공부해서 어떻게 써야 되는건지 찾아보기
    }
}
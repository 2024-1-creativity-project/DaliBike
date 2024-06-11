import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dali_bike.model.RecordUSERId
import com.example.dali_bike.models.User
import java.util.Date

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        _user.value = User(
            userId = "",
            password = "",
            phoneNumber = "",
            name = "",
            nickname = "",
            points = 0,
            subDate = "",
            dailyTime = 0,
            totalTime = 0
        )
    }

    fun setUserLogin(id: String, pw: String) {
        _user.value?.userId = id
        _user.value?.password = pw
    }

    fun setUserMain(nickname: String, dailyTime: Int, totalTime: Int) {
        _user.value?.nickname = nickname
        _user.value?.dailyTime = dailyTime
        _user.value?.totalTime = totalTime
    }


    fun setUserMyPage(name: String, point: Int, subDate: String) {
        _user.value?.name = name
        _user.value?.points = point
        _user.value?.subDate = subDate
    }
}

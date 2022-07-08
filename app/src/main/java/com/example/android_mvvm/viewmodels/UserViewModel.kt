package com.example.android_mvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_mvvm.database.entity.User
import com.example.android_mvvm.repositories.UserRepository
import com.example.android_mvvm.utils.NetworkHelper
import com.example.android_mvvm.utils.Resource
import kotlinx.coroutines.launch
import java.io.Flushable

class UserViewModel(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private var liveData = MutableLiveData<Resource<List<User>>>()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            liveData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val remoteUsers = userRepository.getRemoteUsers()
                if (remoteUsers.isSuccessful) {
                    userRepository.addUsers(remoteUsers.body() ?: emptyList())
                    liveData.postValue(Resource.success(remoteUsers.body()))
                } else {
                    liveData.postValue(Resource.error(remoteUsers.errorBody()?.string() ?: "",
                            null))
                }
            } else {
                val localUsers = userRepository.getLocalUsers()
                liveData.postValue(Resource.success(localUsers))
            }
        }
    }

    fun getUsers(): MutableLiveData<Resource<List<User>>> = liveData

}
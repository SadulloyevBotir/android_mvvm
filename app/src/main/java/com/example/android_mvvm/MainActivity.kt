package com.example.android_mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android_mvvm.adapters.UsersAdapter
import com.example.android_mvvm.database.AppDatabase
import com.example.android_mvvm.databinding.ActivityMainBinding
import com.example.android_mvvm.network.ApiClient
import com.example.android_mvvm.repositories.UserRepository
import com.example.android_mvvm.utils.NetworkHelper
import com.example.android_mvvm.utils.Status
import com.example.android_mvvm.viewmodels.UserViewModel
import com.example.android_mvvm.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        usersAdapter = UsersAdapter()
        binding.rv.adapter = usersAdapter

        var userRepository = UserRepository(ApiClient.apiService, AppDatabase.getInstance(this))
        val networkHelper = NetworkHelper(this)

        userViewModel = ViewModelProvider(this,
            ViewModelFactory(userRepository, networkHelper))[UserViewModel::class.java]

        binding.barProgress.visibility = View.VISIBLE
        userViewModel.getUsers().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.barProgress.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.barProgress.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    binding.barProgress.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    usersAdapter.submitList(it.data)
                }
            }
        })
    }
}
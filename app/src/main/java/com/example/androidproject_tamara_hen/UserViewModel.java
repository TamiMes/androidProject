package com.example.androidproject_tamara_hen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Ui.User;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userEmailLiveData = new MutableLiveData<>();

    // Method to set user details
    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    // Method to get user details
    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void setEmail(String email) {
        userEmailLiveData.setValue(email);
    }

    public MutableLiveData<String> getUserEmailLiveData() {
        return userEmailLiveData;
    }
}

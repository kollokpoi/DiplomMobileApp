package com.example.diplommobileapp.data.viewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChatsViewModel.class)) {
            return (T) new ChatsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

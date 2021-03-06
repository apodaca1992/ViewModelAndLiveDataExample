package com.example.adrian.viewmodelandlivedataexample.clases;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.adrian.viewmodelandlivedataexample.model.AgregarNuevaAfiliacionModel;
import com.example.adrian.viewmodelandlivedataexample.model.NameViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Context oContext;

    public MyViewModelFactory(Context prContext) {
        oContext = prContext;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NameViewModel.class) {
            return (T) new NameViewModel(oContext);
        } else if (modelClass == AgregarNuevaAfiliacionModel.class) {
            return (T) new AgregarNuevaAfiliacionModel(oContext);
        } else {
            return create(modelClass);
        }
    }
}

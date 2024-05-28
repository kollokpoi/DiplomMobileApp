package com.example.diplommobileapp.data.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diplommobileapp.adapters.MeasuresRecyclerAdapter;
import com.example.diplommobileapp.data.models.division.Division;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.example.diplommobileapp.ui.DivisionActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisionViewModel extends ViewModel {
    private int divisionId;
    private MutableLiveData<Division> divisionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MeasureViewModel>> measuresMutableLivedata = new MutableLiveData<>();

    private MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
        setDivisionMutableLiveData();
        setMeasuresMutableLivedata();
    }

    private void setDivisionMutableLiveData() {
        IApi retrofit = RetrofitFactory.getApiService();
        setIsLoading(true);

        Log.d("RequestTimer", "Время отправки запроса:"+ new Date());
        long startTime = System.currentTimeMillis();
        retrofit.GetDivision(divisionId).enqueue(new Callback<Division>() {
            @Override
            public void onResponse(@NonNull Call<Division> call, @NonNull Response<Division> response) {
                Log.d("RequestTimer", "Время обработки запроса:"+ (System.currentTimeMillis() - startTime)+"мс");
                Log.d("RequestTimer", "Время получения ответа:"+ new Date());
                if (response.isSuccessful()) {
                    divisionMutableLiveData.setValue(response.body());
                }
                else{
                    setIsError(true);
                }
                setIsLoading(false);
            }

            @Override
            public void onFailure(@NonNull Call<Division> call, @NonNull Throwable t) {
                setIsLoading(false);
                setIsError(true);
            }
        });
    }
    private void setMeasuresMutableLivedata() {
        List<Integer> idList = new ArrayList<>();
        idList.add(divisionId);

        IApi measuresRetrofit = RetrofitFactory.getApiService();
        measuresRetrofit.GetMeasuresForDivision(idList).enqueue(new Callback<List<MeasureViewModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MeasureViewModel>> call, @NonNull Response<List<MeasureViewModel>> response) {
                if (response.isSuccessful()) {
                    measuresMutableLivedata.setValue(response.body());
                }
                else{
                    setIsError(true);
                }
                setIsLoading(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<MeasureViewModel>> call, @NonNull Throwable t) {
                setIsLoading(false);
                setIsError(true);
            }
        });
    }


    public MutableLiveData<Division> getDivisionMutableLiveData() {
        return divisionMutableLiveData;
    }
    public MutableLiveData<List<MeasureViewModel>> getMeasuresMutableLivedata() {
        return measuresMutableLivedata;
    }

    private void setIsError(boolean isError) {
        this.isError.setValue(isError);
    }
    private void setIsLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public MutableLiveData<Boolean> getIsError() {
        return isError;
    }
}

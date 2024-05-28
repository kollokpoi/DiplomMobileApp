package com.example.diplommobileapp.ui.main.ui.home;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.adapters.DivisionRecycleAdapter;
import com.example.diplommobileapp.adapters.EventsRecycleAdapter;
import com.example.diplommobileapp.adapters.MeasuresRecyclerAdapter;
import com.example.diplommobileapp.data.models.event.Event;
import com.example.diplommobileapp.data.viewModels.MeasureViewModel;

import com.example.diplommobileapp.databinding.FragmentHomeBinding;
import com.example.diplommobileapp.services.ScreenUtils;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.example.diplommobileapp.ui.custom_elements.EventElement;
import com.example.diplommobileapp.ui.custom_elements.MeasureElement;
import com.example.diplommobileapp.ui.login.LoginActivity;
import com.example.diplommobileapp.ui.main.MainActivity;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private ProgressBar loadingProgressBar;
    private View underline;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadingProgressBar = binding.loading;
        underline = binding.underLine;

        int screenWidth = ScreenUtils.getScreenWidth(getActivity());

        ViewGroup.LayoutParams params = underline.getLayoutParams();
        params.width = (screenWidth / 2);
        underline.setLayoutParams(params);

        showLoading();

        loadEvents();

        assert binding.eventsLinear != null;
        binding.eventsLinear.setOnClickListener(this::eventsLinearClick);
        assert binding.measuresLinear != null;
        binding.measuresLinear.setOnClickListener(this::measuresLinearClick);

        return root;
    }

    private void eventsLinearClick(View view){
        assert binding.eventsTv != null;
        binding.eventsTv.setTypeface(null, Typeface.BOLD);
        assert binding.measuresTv != null;
        binding.measuresTv.setTypeface(null, Typeface.NORMAL);
        underlineAnimator((LinearLayout) view);
        loadEvents();
    }
    private void measuresLinearClick(View view){
        assert binding.eventsTv != null;
        binding.eventsTv.setTypeface(null, Typeface.NORMAL);
        assert binding.measuresTv != null;
        binding.measuresTv.setTypeface(null, Typeface.BOLD);
        underlineAnimator((LinearLayout) view);
        loadMeasures();
    }
    private void loadEvents(){
        IApi retrofit = RetrofitFactory.getApiService();
        retrofit.GetEventsForUser().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()){
                    createEventElements(response.body());
                    endLoading();
                }else{
                    showFail();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                showFail();
            }
        });
    }
    private void loadMeasures(){
        IApi retrofit = RetrofitFactory.getApiService();
        retrofit.GetMeasuresForUser().enqueue(new Callback<List<MeasureViewModel>>() {
            @Override
            public void onResponse(Call<List<MeasureViewModel>> call, Response<List<MeasureViewModel>> response) {
                if (response.isSuccessful()){
                    createMeasureElements(response.body());
                    endLoading();
                }else{
                    showFail();
                }
            }

            @Override
            public void onFailure(Call<List<MeasureViewModel>> call, Throwable t) {
                showFail();
            }
        });
    }
    private void createEventElements(List<Event> events){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventsRecycleAdapter adapter = new EventsRecycleAdapter(events,getContext());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                binding.recyclerView.setLayoutManager(layoutManager);
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }
    private void createMeasureElements(List<MeasureViewModel> measures){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MeasuresRecyclerAdapter adapter = new MeasuresRecyclerAdapter(measures,getContext(),LinearLayoutManager.VERTICAL);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                binding.recyclerView.setLayoutManager(layoutManager);
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }
    private void showLoading(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }
        });
    }
    private void endLoading(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void showFail(){
        Toast.makeText(getContext(),R.string.loading_error,Toast.LENGTH_SHORT).show();
    }

    void underlineAnimator(@NonNull LinearLayout layout){
        int[] location = new int[2];
        layout.getLocationOnScreen(location);

        int x = location[0];

        int[] originalLocation = new int[2];
        underline.getLocationOnScreen(originalLocation);
        int startX = originalLocation[0];

        ObjectAnimator animation = ObjectAnimator.ofFloat(underline, "translationX", startX, x);
        animation.setDuration(500);
        animation.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
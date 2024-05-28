package com.example.diplommobileapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diplommobileapp.MyApplication;
import com.example.diplommobileapp.R;
import com.example.diplommobileapp.adapters.MeasuresRecyclerAdapter;
import com.example.diplommobileapp.data.classes.CustomMapView;
import com.example.diplommobileapp.data.models.division.Division;
import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.example.diplommobileapp.data.viewModels.DivisionViewModel;
import com.example.diplommobileapp.data.viewModels.MeasureViewModel;
import com.example.diplommobileapp.databinding.ActivityDivisionBinding;
import com.example.diplommobileapp.services.DateWorker;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.example.diplommobileapp.services.imageworker.ImageUtils;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.runtime.image.ImageProvider;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisionActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_LOCATION = 123;
    ActivityDivisionBinding binding;
    int divisionId;

    private DivisionViewModel viewModel;

    CustomMapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDivisionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.mapView;
        mapView.setViewParent(binding.scrollView);

        viewModel = new ViewModelProvider(this).get(DivisionViewModel.class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_LOCATION
            );
        }



        divisionId = getIntent().getIntExtra("id", 0);
        viewModel.setDivisionId(divisionId);


        viewModel.getDivisionMutableLiveData().observe(this,division->{
            try {
                createUi(division);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        viewModel.getMeasuresMutableLivedata().observe(this,measures->{
            MeasuresRecyclerAdapter adapter = new MeasuresRecyclerAdapter(measures,DivisionActivity.this);
            LinearLayoutManager manager = new LinearLayoutManager(DivisionActivity.this,LinearLayoutManager.HORIZONTAL,false);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(manager);
        });
        viewModel.getIsError().observe(this,error->{
            if (error){
                showFail();
            }
        });
        viewModel.getIsLoading().observe(this,loading->{
            if (loading){
                showLoading();
            }else{
                endLoading();
            }
        });

    }

    public void onChat(View view){
        Intent intent = new Intent(DivisionActivity.this, ChatActivity.class);
        intent.putExtra("divisionId",divisionId);
        startActivity(intent);
    }

    private void createUi(Division division) throws ParseException {
        byte[] preview = division.getPreviewImage();
        if (preview != null) ImageUtils.setImageViewFromByteArray(preview, binding.eventImageView);
        String resultDate = "C ";
        resultDate += DateWorker.getShortDate(division.getDateOfStart());
        if (division.getDateOfEnd() != null) {
            resultDate += " по " + DateWorker.getShortDate(division.getDateOfEnd());
        }
        if (!MyApplication.userStamp.getRoles().contains("OrganizationUser")){
            if (division.isDivisionLeaderExist()){
                binding.goToChatBtn.setVisibility(View.VISIBLE);
            }
        }
        binding.datesTv.setText(resultDate);
        binding.nameTv.setText(division.getName());
        binding.descriptionTv.setText(division.getDescription());
        binding.placeNameTv.setText(division.getPlaceName());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            createMap(division);
        }
    }
    private void createMap(Division division){
        mapView.getMap().move(
                new CameraPosition(
                        new Point(division.getLatitude(),division.getLongitude()), 15f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0f),
                null
        );

        PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark();
        placemark.setGeometry(new Point(division.getLatitude(),division.getLongitude()));
        placemark.setIcon(ImageProvider.fromResource(this, R.drawable.ic_pin));
        IconStyle iconStyle = new IconStyle(new PointF(0.5f, 1.0f), RotationType.NO_ROTATION,10f,false,true,0.07f,null);
        placemark.setIconStyle(iconStyle);
    }
    private void endLoading() {
        runOnUiThread(() -> {
            binding.loading.setVisibility(View.GONE);
            binding.holder.setVisibility(View.VISIBLE);
        });
    }
    private void showLoading(){
        runOnUiThread(() -> {
            binding.loading.setVisibility(View.VISIBLE);
            binding.holder.setVisibility(View.GONE);
        });
    }

    private void showFail(){
        Toast.makeText(this,R.string.loading_error,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
    @Override
    protected void onStop() {
        binding.mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}
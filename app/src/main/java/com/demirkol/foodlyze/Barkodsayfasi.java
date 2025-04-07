package com.demirkol.foodlyze;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Barkodsayfasi extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private TextView resultTextView;
    private ImageView nutritionImageView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barkodsayfasi);

        resultTextView = findViewById(R.id.resultTextView);
        nutritionImageView = findViewById(R.id.urunresmi);


        // Kamera iznini kontrol et
        checkCameraPermission();

        // Barkod tarama başlatma butonunu tetikleyin
        findViewById(R.id.scanButton).setOnClickListener(view -> {
            new IntentIntegrator(Barkodsayfasi.this).initiateScan();
        });
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Kamera izni verildi.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kamera izni verilmedi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String barcode = result.getContents();
            if (barcode != null) {
                fetchProductDetails(barcode);
            } else {
                Toast.makeText(this, "Barkod tarandı, ancak geçerli bir barkod değeri alınamadı.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchProductDetails(String barcode) {
        OpenFoodFactsAPI apiService = RetrofitClient.getClient().create(OpenFoodFactsAPI.class);
        Call<ProductResponse> call = apiService.getProductDetails(barcode);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse.Product product = response.body().getProduct();

                    String productName = nullToDefault(product.getProductName());
                    String ingredients = nullToDefault(product.getIngredientsText());
                    String energy = nullToDefault(product.getNutriments().getEnergy());
                    String fat = nullToDefault(product.getNutriments().getFat());
                    String carbs = nullToDefault(product.getNutriments().getCarbohydrates());
                    String sugars = nullToDefault(product.getNutriments().getSugars());
                    String protein = nullToDefault(product.getNutriments().getProtein());
                    String salt = nullToDefault(product.getNutriments().getSalt());
                    String nutriscore = nullToDefault(product.getNutriscoreGrade());
                    String origin = nullToDefault(product.getOrigins());
                    String quantity = nullToDefault(product.getQuantity());
                    String nutritionImageUrl = product.getImageUrl();
                    if (nutritionImageUrl != null && !nutritionImageUrl.isEmpty()) {
                        Glide.with(Barkodsayfasi.this)
                                .load(nutritionImageUrl)
                                .into(nutritionImageView);
                    }

                    resultTextView.setText(
                            "Ürün: " + productName +
                                    "\nİçerik: " + ingredients +
                                    "\nEnerji: " + energy + " kcal" +
                                    "\nYağ: " + fat + " g" +
                                    "\nKarbonhidrat: " + carbs + " g" +
                                    "\nŞeker: " + sugars + " g" +
                                    "\nProtein: " + protein + " g" +
                                    "\nTuz: " + salt + " g" +
                                    "\nNutriScore: " + nutriscore.toUpperCase() +
                                    "\nMenşei: " + origin +
                                    "\nMiktar: " + quantity
                    );
                } else {
                    Toast.makeText(Barkodsayfasi.this, "Bu barkod için ürün bilgileri bulunamadı.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                resultTextView.setText("Veri çekme hatası.");
                Toast.makeText(Barkodsayfasi.this, "Ürün bilgileri alınamadı", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String nullToDefault(String value) {
        return value != null && !value.isEmpty() ? value : "Bilinmiyor";
    }
}

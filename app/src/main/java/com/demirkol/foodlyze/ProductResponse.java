package com.demirkol.foodlyze;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductResponse {
    @SerializedName("product")
    private Product product;

    public Product getProduct() {
        return product;
    }

    public class Product {
        @SerializedName("product_name")
        private String productName;

        @SerializedName("ingredients_text")
        private String ingredientsText;

        @SerializedName("ingredients_text_en")
        private String ingredientsTextEn;

        @SerializedName("allergens")
        private String allergens;

        @SerializedName("warnings")
        private List<String> warnings;

        @SerializedName("labels")
        private String labels;

        @SerializedName("labels_tags")
        private List<String> labelTags;

        @SerializedName("image_small_url")
        private String imageUrl;

        @SerializedName("image_front_url")
        private String productImage;

        @SerializedName("image_ingredients_url")
        private String ingredientsImage;

        @SerializedName("image_nutrition_url")
        private String nutritionImage;

        @SerializedName("image_small_url")
        private String barcodeImage;

        @SerializedName("categories")
        private String categories;

        @SerializedName("origins")
        private String origins;

        @SerializedName("quantity")
        private String quantity;

        @SerializedName("nutriments")
        private Nutriments nutriments;

        @SerializedName("nutriscore_grade")
        private String nutriscoreGrade;

        @SerializedName("nutriscore_score")
        private int nutriscoreScore;

        @SerializedName("traces")
        private String traces;

        public String getProductName() { return productName; }
        public String getIngredientsText() { return ingredientsText; }
        public String getIngredientsTextEn() { return ingredientsTextEn; }
        public String getAllergens() { return allergens; }
        public List<String> getWarnings() { return warnings; }
        public String getLabels() { return labels; }
        public List<String> getLabelTags() { return labelTags; }
        public String getImageUrl() { return imageUrl; }
        public String getProductImage() { return productImage; }
        public String getIngredientsImage() { return ingredientsImage; }
        public String getNutritionImage() { return nutritionImage; }
        public String getBarcodeImage() { return barcodeImage; }
        public String getCategories() { return categories; }
        public String getOrigins() { return origins; }
        public String getQuantity() { return quantity; }
        public Nutriments getNutriments() { return nutriments; }
        public String getNutriscoreGrade() { return nutriscoreGrade; }
        public int getNutriscoreScore() { return nutriscoreScore; }
        public String getTraces() { return traces; }
    }
}

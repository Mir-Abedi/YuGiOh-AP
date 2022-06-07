package model.card;

import java.util.ArrayList;
import java.util.List;

public class FeatureWrapper {
    private String effectMap;
    public ArrayList<CardFeatures> features = new ArrayList<>();

    public void addFeature(CardFeatures newFeature) {
        if (!doesHaveFeature(newFeature)) features.add(newFeature);
    }

    public void addFeatures(List<CardFeatures> features) {
        this.features.addAll(features);
    }

    public void removeFeature(CardFeatures removingFeature) {
        if (doesHaveFeature(removingFeature)) features.remove(removingFeature);
    }

    public boolean doesHaveFeature(CardFeatures checkingFeature) {
        for (CardFeatures feature : features) {
            if (checkingFeature.equals(feature)) return true;
        }
        return false;
    }

    public void setEffectMap(String map) {
        effectMap = map;
    }

    public String getEffectMap() {
        return effectMap;
    }
}

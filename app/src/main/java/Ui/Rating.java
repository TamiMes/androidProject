package Ui;

import java.util.Map;

public class Rating {
    private Map<String,Map<String,Float>> ratings;

    public Rating() {
    }

    public Map<String,Map<String,Float>> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String,Map<String,Float>> ratings) {
        this.ratings = ratings;
    }

//    public float getRatingOfItem(String itemName){
//        if (ratings != null && !ratings.isEmpty())
//            return ratings.getOrDefault(itemName,0f);
//        else return 0f;
//    }
}

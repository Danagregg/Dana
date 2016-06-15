package com.example.danagregg.locationandcardsandlibrarysohmy;

/**
 * Created by Dana Gregg on 2016-06-14.
 */
public class Card {

    public String type;
    public String title;
    public String imageURL;


    // Secondary tingz
    // I wanted to make a generic secondary data field so it could hold any type of data from the cards
    // This was complicated since I used the parser from retrofit
    public String placeCategory;
    public String movieExtraImageURL;
    public String musicVideoURL;
}

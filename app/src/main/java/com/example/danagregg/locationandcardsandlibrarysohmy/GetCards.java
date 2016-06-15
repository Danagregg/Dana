package com.example.danagregg.locationandcardsandlibrarysohmy;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Dana Gregg on 2016-06-14.
 */
public interface GetCards {
        @GET("gistfile1.js")
        Observable<Cards> listCards();
}

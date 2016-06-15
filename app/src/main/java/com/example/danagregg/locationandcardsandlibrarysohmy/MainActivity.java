package com.example.danagregg.locationandcardsandlibrarysohmy;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));
        final CardsRecyclerAdapter adapter = new CardsRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        retrieveCards(adapter);

    }

    private void retrieveCards(CardsRecyclerAdapter testAdapter) {
        // needed adapter to be final
        final CardsRecyclerAdapter adapter = testAdapter;
        // get Cards using Retrofit :D
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://gist.githubusercontent.com/helloandrewpark/0a407d7c681b833d6b49/raw/5f3936dd524d32ed03953f616e19740bba920bcd/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GetCards service = retrofit.create(GetCards.class);
        Observable<Cards> cardsObservable = service.listCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
        Observable<Location> locationObservable = locationProvider.getLastKnownLocation();


        // Wait until both observables are done before dismissing progress bar (onNext)
        Observable
                // new Func2 because my Java doesn't have lambdas
                .zip(cardsObservable, locationObservable, new Func2<Cards, Location, ResultWrapper>() {
                    @Override
                    public ResultWrapper call(Cards cards, Location location) {
                        return new ResultWrapper(cards, location);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultWrapper>() {
                    @Override
                    public void onCompleted() {

                        // Didn't add complete functionality
                    }

                    @Override
                    public void onError(Throwable e) {
                        // For debugging, errors make me sad :'(
                        Log.d("Sad", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultWrapper resultWrapper) {
                        ((TextView) findViewById(R.id.location)).setText(resultWrapper.location.getLatitude() + ", " + resultWrapper.location.getLongitude());
                        adapter.setCards(resultWrapper.cards.cards);
                        findViewById(R.id.progress).setVisibility(View.GONE);
                    }
                });
    }

    // Used to wrap two observables into one
    private class ResultWrapper {

        public Cards cards;
        public Location location;

        public ResultWrapper(Cards cards, Location location) {
            this.cards = cards;
            this.location = location;
        }
    }
}

package com.example.danagregg.locationandcardsandlibrarysohmy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dana Gregg on 2016-06-14.
 */
public class CardsRecyclerAdapter extends RecyclerView.Adapter<CardsRecyclerAdapter.CardsViewHolder>{
    private List<Card> cards = new ArrayList<>();
    private Context context;

    public CardsRecyclerAdapter (Context context) {
        this.context = context;
    }
    @Override
    // inflate view
    public CardsRecyclerAdapter.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vurb_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CardsRecyclerAdapter.CardsViewHolder holder, int position) {
        final Card card = cards.get(position);
        ((TextView) holder.itemView.findViewById(R.id.cardTitle)).setText(card.title);
        if (card.type.equals("place")) {
            // Mcdonalds ImageURL uses https which isn't given from the JSON
            // Not sure if intentional so this is just a hack to fix it
            Picasso.with(context).load(new StringBuilder(card.imageURL).insert(4, "s").toString()).into((ImageView) holder.itemView.findViewById(R.id.cardImage));
        } else {
            Picasso.with(context).load(card.imageURL).into((ImageView) holder.itemView.findViewById(R.id.cardImage));
        }

        // Set secondary data field
        // This definitely is a pain point
        // Could have made a more generic class, but would have had to branch eventually to show different UI elements
        // Could have also made several Card XML classes for various types and displayed them properly
        // Not the absolute worst option
        switch (card.type) {
            case "place":
                holder.itemView.findViewById(R.id.cardCategory).setVisibility(View.VISIBLE);
                ((TextView) holder.itemView.findViewById(R.id.cardCategory)).setText(card.placeCategory);
                break;
            case "movie":
                holder.itemView.findViewById(R.id.cardSecondaryImage).setVisibility(View.VISIBLE);
                Picasso.with(context).load(card.movieExtraImageURL).into((ImageView)holder.itemView.findViewById(R.id.cardSecondaryImage));
                break;
            case "music":;
                holder.itemView.findViewById(R.id.button).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(card.musicVideoURL));
                        context.startActivity(browserIntent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    class CardsViewHolder extends RecyclerView.ViewHolder{

        public View itemView;

        public CardsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}

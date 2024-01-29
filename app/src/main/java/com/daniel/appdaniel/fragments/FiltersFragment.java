package com.daniel.appdaniel.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.activites.FiltersActivity;


public class FiltersFragment extends Fragment
{
    View mView;
    CardView mCardViewPS4;
    CardView mCardViewNintendo;
    CardView mCardViewXbox;
    CardView mCardViewPC;


    public  FiltersFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_filters, container, false);
        mCardViewPS4 = mView.findViewById(R.id.CardViewPS4);
        mCardViewNintendo = mView.findViewById(R.id.CardViewNintendo);
        mCardViewXbox = mView.findViewById(R.id.CardViewXbox);
        mCardViewPC = mView.findViewById(R.id.CardViewPC);


        mCardViewPS4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToFilterActivity("PS4");
            }
        });

        mCardViewNintendo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToFilterActivity("Nintendo");
            }
        });

        mCardViewXbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToFilterActivity("Xbox");
            }
        });

        mCardViewPC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToFilterActivity("PC");
            }
        });

        return mView;
    }

    private void goToFilterActivity(String category)
    {
        Intent integer = new Intent(getContext(),FiltersActivity.class);
        integer.putExtra("category",category);
        startActivity(integer);
    }
}
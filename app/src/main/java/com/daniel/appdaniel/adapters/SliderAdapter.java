package com.daniel.appdaniel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.models.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context,List<SliderItem> sliderItems) {
        mSliderItems = sliderItems;
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layaout_item, parent, false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        SliderItem sliderItem = mSliderItems.get(position);
        if(sliderItem.getImagenUrl() != null )
        {
            if (!sliderItem.getImagenUrl().isEmpty())
            {
                Picasso.get().load(sliderItem.getImagenUrl()).into(viewHolder.imageViewSlider);
            }
        }

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public void setSliderItems(List<SliderItem> sliderItems) {
        mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewSlider;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewSlider = itemView.findViewById(R.id.imageViewSlider);
            this.itemView = itemView;
        }
    }
}


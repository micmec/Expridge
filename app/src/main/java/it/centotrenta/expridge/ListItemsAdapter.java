package it.centotrenta.expridge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 ** Created by michelangelomecozzi on 02/07/17.
 ** <p>
 ** 130 si volaa!
 **/

//TODO work on the images

class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ListAdapterViewHolder> {

    //Variables
    private ListItemsAdapterClickHandler listClickHandler;
    private ArrayList<String> itemInformationName;
    private ArrayList<String> itemInformationDate;
    private ArrayList<Integer> itemInformationImage;
    private String TAG = "ImageResolution";

    // Inner interface for the click handling
    public interface ListItemsAdapterClickHandler {
        void onClick(String itemNameInf, String itemDateInf);
    }


    // Constructor
    public ListItemsAdapter(ListItemsAdapterClickHandler clickHandler) {
        listClickHandler = clickHandler;
        itemInformationName = MainActivity.db.getItemsName();
        itemInformationDate = MainActivity.db.getItemsDate();
        itemInformationImage = getRelativeImages(MainActivity.db.getItemsName());
    }


    // Inner class for the view holder
    public class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Variables
        public final TextView listItemName;
        public final TextView listItemDate;
        public final ImageView listItemImage;

        // Constructor
        protected ListAdapterViewHolder(View view) {

            // Let the super class handle the view
            super(view);

            // Initialize the variables using the views from xml files
            listItemName = (TextView) view.findViewById(R.id.name_of_food);
            listItemDate = (TextView) view.findViewById(R.id.date_of_food);
            listItemImage = (ImageView) view.findViewById(R.id.food_image);

            // Click handling
            view.setOnClickListener(this);

        }

        // Override the interface method
        @Override
        public void onClick(View v) {

            //TODO click handling missing & wrong way of using data (we are passing just the name
            int adapterPosition = getAdapterPosition();
            String itemName = itemInformationName.get(adapterPosition);
            String itemDate = itemInformationDate.get(adapterPosition);
            int itemImage = itemInformationImage.get(adapterPosition);
            listClickHandler.onClick(itemName, itemDate);
            //todo click handling does not work because it involves the images, see todo at the top.
        }
    }

    // Override the method for the creation of the ViewHolders
    @Override
    public ListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context
        Context context = parent.getContext();

        // Get the id of the layout we are implementing
        int layoutId = R.layout.custom_row;

        // Get the inflater
        LayoutInflater inflater = LayoutInflater.from(context);

        // HouseKeeping
        boolean shouldAttachToParentImmediately = false;

        // Create the ViewHolder from the previous information
        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);

        return new ListAdapterViewHolder(view);


    }

    // Override the method for the data-binding
    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, int position) {

        // Get the information
        String itemName = itemInformationName.get(position);
        String itemDate = itemInformationDate.get(position);

        // And assign it
        holder.listItemName.setText(itemName);
        holder.listItemDate.setText(itemDate);

        try {
            int itemImage = itemInformationImage.get(position);
            holder.listItemImage.setImageResource(itemImage);
        }
        catch (Exception e){
            holder.listItemImage.setImageResource(R.drawable.unknown);
        }

    }

    // Override the item counting method
    @Override
    public int getItemCount() {
        if (itemInformationName == null) {
            return 0;
        }

        return itemInformationName.size();
    }

    // Create a method for assigning new data to the item, without creating a new ViewHolder
    public void setItemData(ArrayList<String> informationName, ArrayList<String> informationDate, ArrayList<Integer> informationImage) {

        itemInformationName = informationName;
        itemInformationDate = informationDate;
        itemInformationImage = informationImage;

        notifyDataSetChanged();
    }

    public void addItem() {

        itemInformationName = MainActivity.db.getItemsName();
        itemInformationDate = MainActivity.db.getItemsDate();
        itemInformationImage = getRelativeImages(itemInformationName);
        notifyItemInserted(MainActivity.db.getItemsName().size());

    }

    public ArrayList<Integer> getRelativeImages(ArrayList<String> _itemNames) {

        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < _itemNames.size(); i++) {

            String _itemName = _itemNames.get(i);

            if (
                    (_itemName.contains("Cheese")) || (_itemName.contains("CHEESE")) || (_itemName.contains("cheese")) ||
                            (_itemName.contains("Milk")) || (_itemName.contains("MILK")) || (_itemName.contains("milk")) ||
                            (_itemName.contains("Yogurt")) || (_itemName.contains("YOGURT")) || (_itemName.contains("yogurt")) ||
                            (_itemName.contains("Yogurt")) || (_itemName.contains("YOGURT")) || (_itemName.contains("yogurt")) ||
                            (_itemName.contains("Cream")) || (_itemName.contains("CREAM")) || (_itemName.contains("cream"))
                    )
            {
                Log.d(TAG,"Dairy");
                list.add(R.drawable.dairy);
            }
            else if (
                    (_itemName.contains("Meat")) || (_itemName.contains("MEAT")) || (_itemName.contains("meat")) ||
                            (_itemName.contains("Steak")) || (_itemName.contains("STEAK")) || (_itemName.contains("steak")) ||
                            (_itemName.contains("Ham")) || (_itemName.contains("HAM")) || (_itemName.contains("ham")) ||
                            (_itemName.contains("Bacon")) || (_itemName.contains("BACON")) || (_itemName.contains("bacon")) ||
                            (_itemName.contains("Chicken")) || (_itemName.contains("CHICKEN")) || (_itemName.contains("chicken")) ||
                            (_itemName.contains("Sausage")) || (_itemName.contains("SAUSAGE")) || (_itemName.contains("sausage")) ||
                            (_itemName.contains("Beef")) || (_itemName.contains("BEEF")) || (_itemName.contains("beef")) ||
                            (_itemName.contains("Chop")) || (_itemName.contains("CHOP")) || (_itemName.contains("chop")) ||
                            (_itemName.contains("Lamb")) || (_itemName.contains("LAMB")) || (_itemName.contains("lamb")) ||
                            (_itemName.contains("Burger")) || (_itemName.contains("BURGER")) || (_itemName.contains("burger")) ||
                            (_itemName.contains("Mutton")) || (_itemName.contains("MUTTON")) || (_itemName.contains("mutton")) ||
                            (_itemName.contains("Pork")) || (_itemName.contains("PORK")) || (_itemName.contains("pork")) ||
                            (_itemName.contains("Turkey")) || (_itemName.contains("TURKEY")) || (_itemName.contains("turkey")) ||
                            (_itemName.contains("Hot dog")) || (_itemName.contains("HOT DOG")) || (_itemName.contains("hot dog"))
                    )
            {
                Log.d(TAG,"Meat");
                list.add(R.drawable.meat);
            }
            else if (
                    (_itemName.contains("Pepper")) || (_itemName.contains("PEPPER")) || (_itemName.contains("pepper")) ||
                            (_itemName.contains("Salad")) || (_itemName.contains("SALAD")) || (_itemName.contains("salad")) ||
                            (_itemName.contains("Broccoli")) || (_itemName.contains("BROCCOLI")) || (_itemName.contains("broccoli")) ||
                            (_itemName.contains("Cabbage")) || (_itemName.contains("CABBAGE")) || (_itemName.contains("cabbage")) ||
                            (_itemName.contains("Carrot")) || (_itemName.contains("CARROT")) || (_itemName.contains("carrot")) ||
                            (_itemName.contains("Lettuce")) || (_itemName.contains("LETTUCE")) || (_itemName.contains("lettuce")) ||
                            (_itemName.contains("Onion")) || (_itemName.contains("ONION")) || (_itemName.contains("onion")) ||
                            (_itemName.contains("Spinach")) || (_itemName.contains("SPINACH")) || (_itemName.contains("spinach")) ||
                            (_itemName.contains("Tomato")) || (_itemName.contains("TOMATO")) || (_itemName.contains("tomato")) ||
                            (_itemName.contains("Zucchini")) || (_itemName.contains("ZUCCHINI")) || (_itemName.contains("zucchini"))
                    ){
                list.add(R.drawable.pepper);
            }
            //Todo fish
            else
            {
                Log.d(TAG,"Exception");
                list.add(R.drawable.unknown);
            }
        }

        return list;
    }
}
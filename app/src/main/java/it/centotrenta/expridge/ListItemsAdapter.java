package it.centotrenta.expridge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 ** Created by michelangelomecozzi on 02/07/17.
 ** <p>
 ** 130 si volaa!
 **/

//TODO correct the instantiation to real values

class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ListAdapterViewHolder> {

    //Variables
    private ListItemsAdapterClickHandler listClickHandler;
    private Database db;
    private ArrayList<String> itemInformationName;
    private ArrayList<String> itemInformationDate;
    private ArrayList<Integer> itemInformationImage;


    // Inner interface for the click handling
    public interface ListItemsAdapterClickHandler{void onClick(String itemInformation);}


    // Constructor
    public ListItemsAdapter(ListItemsAdapterClickHandler clickHandler, Database db){
        listClickHandler = clickHandler;
        this.db = db;
        itemInformationName = db.getItemsName();
        itemInformationDate = db.getItemsDateFormatted();
        itemInformationImage = db.getItemsImage();

    }


    // Inner class for the view holder
    public class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Variables
        public final TextView listItemName;
        public final TextView listItemDate;
        public final ImageView listItemImage;

        // Constructor
        protected ListAdapterViewHolder(View view){

            // Let the super class handle the view
            super(view);

            // Initialize the variables using the views from xml files
            listItemName = (TextView) view.findViewById(R.id.name_of_food);
            listItemDate = (TextView) view.findViewById(R.id.date_of_food);
            listItemImage = (ImageView) view.findViewById(R.id.food_image);

            // Click handling
            //view.setOnClickListener(this);

        }

        // Override the interface method
        @Override
        public void onClick(View v) {

            //TODO click handling missing & wrong way of using data (we are passing just the name
            int adapterPosition = getAdapterPosition();
            String itemName = itemInformationName.get(adapterPosition);
            String itemDate = itemInformationDate.get(adapterPosition);
            Integer  itemImage = itemInformationImage.get(adapterPosition);
            listClickHandler.onClick(itemName);
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
        View view = inflater.inflate(layoutId,parent,shouldAttachToParentImmediately);

        return new ListAdapterViewHolder(view);

    }

    // Override the method for the data-binding
    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, int position) {

        // Get the information
        String itemName = itemInformationName.get(position);
        String itemDate = itemInformationDate.get(position);
        Integer itemImage = itemInformationImage.get(position);

        // And assign it
        holder.listItemName.setText(itemName);
        holder.listItemDate.setText(itemDate);
        holder.listItemImage.setImageResource(itemImage);

    }

    // Override the item counting method
    @Override
    public int getItemCount() {
        if(itemInformationName == null){return 0;}
        return itemInformationName.size();
    }

    // Create a method for assigning new data to the item, without creating a new ViewHolder
    public void setItemData(ArrayList<String> informationName, ArrayList<String> informationDate, ArrayList<Integer> informationImage){

        itemInformationName = informationName;
        itemInformationDate = informationDate;
        itemInformationImage = informationImage;

        notifyDataSetChanged();
    }

    public static String toDate(long milliSeconds, String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    public static ArrayList<String> toArrayDate(ArrayList<Long> milliSeconds, String format){

        ArrayList<String> string = new ArrayList<>();

        for(int i = 0; i<milliSeconds.size();i++) {

            SimpleDateFormat formatter = new SimpleDateFormat(format);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds.get(i));

            string.add(formatter.format(calendar.getTime()));

        }
        return string;
    }



}
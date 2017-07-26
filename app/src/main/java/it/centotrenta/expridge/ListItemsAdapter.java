package it.centotrenta.expridge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.centotrenta.expridge.Utilities.DBHandler;


//TODO work on the images

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ListAdapterViewHolder> {

    //Variables
    private ListItemsAdapterClickHandler listClickHandler;
    private ArrayList<String> itemInformationName;
    private ArrayList<String> itemInformationDate;
    private ArrayList<Integer> itemInformationImage;
    private ArrayList<Integer> itemInformationId;
    private DBHandler databaseHandler;
    public static long dateForNotification;

    // Inner interface for the click handling
    public interface ListItemsAdapterClickHandler {
        void onClick(View view, int id, String itemInfoName, long time,ImageView deleteButton,ImageView alarmButton,
                     String dateFormatted);
    }

    // Constructor
    public ListItemsAdapter(ListItemsAdapterClickHandler clickHandler){ //ListItemsAdapterLongClickHandler longClickHandler) {
        databaseHandler = MainActivity.dataBaseHandler;
        listClickHandler = clickHandler;
        itemInformationName = databaseHandler.getNamesFromColumn();
        itemInformationDate = databaseHandler.getDatesFromColumn();
        itemInformationImage = getRelativeImages(itemInformationName);
        itemInformationId = databaseHandler.getIdFromColumn();


    }


    // Inner class for the view holder
    public class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//,View.OnLongClickListener {

        // Variables
        public final TextView listItemName;
        public final TextView listItemDate;
        public final ImageView listItemImage;
        public final ImageView deleteButton;
        public final ImageView alarmButton;

        // Constructor
        protected ListAdapterViewHolder(View view) {

            // Let the super class handle the view
            super(view);

            // Initialize the variables using the views from xml files
            listItemName = (TextView) view.findViewById(R.id.name_of_food);
            listItemDate = (TextView) view.findViewById(R.id.date_of_food);
            listItemImage = (ImageView) view.findViewById(R.id.food_image);
            deleteButton = (ImageView) view.findViewById(R.id.delete_button);
            alarmButton = (ImageView) view.findViewById(R.id.alarm_button);

            // Click handling
            view.setOnClickListener(this);

        }

        // Override the interface method
        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            String itemName = itemInformationName.get(adapterPosition);
            String dateFormatted = itemInformationDate.get(adapterPosition);
            int id = itemInformationId.get(adapterPosition);

            listClickHandler.onClick(v,id,itemName,dateForNotification,deleteButton,alarmButton,dateFormatted);

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

        // When we create it, we set the notifications and button appearance to be off
        MainActivity.isNotificationOn = false;
        MainActivity.isOpenTwo = false;
        MainActivity.isFirstOpen = true;

        return new ListAdapterViewHolder(view);

    }

    // Override the method for the data-binding
    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, int position) {

        // Get the information
        try
        {
            String itemName = itemInformationName.get(position);
            String itemDate = itemInformationDate.get(position);
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
        catch(Exception e){

            e.printStackTrace();

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

    // Create a method for assigning new data to the item, without creating a new ViewHolder TODO : we might need it in future
    public void setItemData(ArrayList<String> informationName, ArrayList<String> informationDate, ArrayList<Integer> informationImage) {

        itemInformationName = informationName;
        itemInformationDate = informationDate;
        itemInformationImage = informationImage;

        notifyDataSetChanged();
    }

    public void addItem(Context context) {

        if(!databaseHandler.getNamesFromColumn().isEmpty() && !databaseHandler.getDatesFromColumn().isEmpty()) {

            itemInformationName = databaseHandler.getNamesFromColumn();
            itemInformationDate = databaseHandler.getDatesFromColumn();
            itemInformationImage = getRelativeImages(itemInformationName);
            itemInformationId = databaseHandler.getIdFromColumn();

            notifyItemInserted(itemInformationName.size());
        }

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
                list.add(R.drawable.vedgetables);
            }
            //Todo fish and if it is unknown might add it to the categories
            else
            {
                list.add(R.drawable.unknown);
            }
        }

        return list;
    }

    public void loadDB() {

        itemInformationName = databaseHandler.getNamesFromColumn();
        itemInformationDate = databaseHandler.getDatesFromColumn();
        itemInformationImage = getRelativeImages(itemInformationName);
        itemInformationId = databaseHandler.getIdFromColumn();
        notifyItemRemoved(itemInformationName.size() + 1); //TODO check if we can use notifyDataSetChanged()

    }

}
package com.example.favdishjava.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.favdishjava.databinding.DialogCustomListBinding;
import com.example.favdishjava.databinding.ItemCustomListLayoutBinding;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.view.fragments.AllDishesFragment;

import java.util.List;

public class CustomListItemAdapter extends RecyclerView.Adapter<CustomListItemAdapter.ViewHolder> {
    private String selection;
    private List<String> listItems;
    private Activity activity;
    private Fragment fragment;

    public CustomListItemAdapter(Activity activity, Fragment fragment, List<String> listItems, String selection) {
        this.selection = selection;
        this.listItems = listItems;
        this.activity = activity;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCustomListLayoutBinding binding;

        public ViewHolder(ItemCustomListLayoutBinding root) {
            super(root.getRoot());
            binding = root;
        }
    }

    @NonNull
    @Override
    public CustomListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCustomListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomListItemAdapter.ViewHolder holder, int position) {
        String item = listItems.get(position);

        holder.binding.tvText.setText(item);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(activity instanceof AddUpdateDishActivity){
                   ((AddUpdateDishActivity) activity).selectedListItem(item,selection);
               }
               if(fragment instanceof AllDishesFragment){

               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

}

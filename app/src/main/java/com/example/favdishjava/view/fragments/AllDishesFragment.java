package com.example.favdishjava.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.favdishjava.R;
import com.example.favdishjava.databinding.DialogCustomListBinding;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.view.activities.MainActivity;
import com.example.favdishjava.view.adapters.CustomListItemAdapter;

import java.util.ArrayList;

public class AllDishesFragment extends Fragment {

    private Dialog mCustomListDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_dishes, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_all_dishes,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter_dishes:
                filterDishesListDialog();
                return true;
            case R.id.action_add_dish:
                startActivity(new Intent(getContext(), AddUpdateDishActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private  void filterDishesListDialog(){
        mCustomListDialog = new Dialog(requireActivity());
        DialogCustomListBinding dialogCustomListBinding = DialogCustomListBinding.inflate(getLayoutInflater());
        mCustomListDialog.setContentView(dialogCustomListBinding.getRoot());
        dialogCustomListBinding.tvTitle.setText(getResources().getString(R.string.title_select_item_to_filter));
        dialogCustomListBinding.rvList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ArrayList<String> dishTypes=Constants.dishTypes();
        dishTypes.add(0,Constants.ALL_ITEMS);
        dialogCustomListBinding.rvList.setAdapter(new CustomListItemAdapter(requireActivity(), AllDishesFragment.this, dishTypes, Constants.FILTER_SELECTION));
        mCustomListDialog.show();
    }
    public void filterSelection(String item){
        mCustomListDialog.dismiss();

    }
}
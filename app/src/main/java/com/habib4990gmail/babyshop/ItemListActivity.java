package com.habib4990gmail.babyshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.habib4990gmail.babyshop.Interface.ItemClickListener;
import com.habib4990gmail.babyshop.model.Item;
import com.habib4990gmail.babyshop.viewholder.ItemViewHolder;
import com.squareup.picasso.Picasso;

public class ItemListActivity extends AppCompatActivity {
    RecyclerView recycler_item;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList;

    String categoryId="";

    FirebaseRecyclerAdapter<Item,ItemViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference("Items");

        recycler_item = (RecyclerView)findViewById(R.id.recycler_item);
        recycler_item.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_item.setLayoutManager(layoutManager);

        //Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null)
        {
            loadListItem(categoryId);
        }
    }

    private void loadListItem(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class,
                R.layout.baby_item,
                ItemViewHolder.class,
                itemList.orderByChild("MenuId").equalTo(categoryId) // like : Select * from Items where MenuId =
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, int position) {
                viewHolder.item_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.item_image);


                final Item local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ItemListActivity.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        //set adapter
        recycler_item.setAdapter(adapter);
    }
}

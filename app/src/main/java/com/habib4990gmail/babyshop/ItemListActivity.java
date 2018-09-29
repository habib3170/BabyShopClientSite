package com.habib4990gmail.babyshop;

import android.content.Intent;
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
import com.habib4990gmail.babyshop.common.Common;
import com.habib4990gmail.babyshop.database.Database;
import com.habib4990gmail.babyshop.model.Item;
import com.habib4990gmail.babyshop.model.Order;
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
            if(Common.isConnectedToInterner(getBaseContext())) {
                loadListItem(categoryId);
            }
            else
            {
                Toast.makeText(ItemListActivity.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void loadListItem(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class,
                R.layout.baby_item,
                ItemViewHolder.class,
                itemList.orderByChild("menuId").equalTo(categoryId) // like : Select * from Items where MenuId =
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, final Item model, final int position) {
                viewHolder.item_name.setText(model.getName());
                viewHolder.item_price.setText(String.format("$ %s",model.getPrice().toString()));
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.item_image);

                //Quick Cart
                viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Database(getBaseContext()).addTocart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount()
                        ));
                        Toast.makeText(ItemListActivity.this,"Add To Cart", Toast.LENGTH_SHORT).show();
                    }
                });


                final Item local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent itemDelail = new Intent(ItemListActivity.this,ItemDetailActivity.class);
                        itemDelail.putExtra("ItemId",adapter.getRef(position).getKey());//Send Item Id to new activity
                        startActivity(itemDelail);
                    }
                });
            }
        };

        //set adapter
        recycler_item.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
    }
}

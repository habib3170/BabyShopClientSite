package com.habib4990gmail.babyshop;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.habib4990gmail.babyshop.common.Common;
import com.habib4990gmail.babyshop.database.Database;
import com.habib4990gmail.babyshop.model.Item;
import com.habib4990gmail.babyshop.model.Order;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends AppCompatActivity {

    TextView item_name,item_price,item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button btnCart;
    ElegantNumberButton numberButton;

    String itemId="";

    FirebaseDatabase database;
    DatabaseReference items;

    Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        items = database.getReference("Items");

        //Init View
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (Button)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addTocart(new Order(
                        itemId,
                        currentItem.getName(),
                        numberButton.getNumber(),
                        currentItem.getPrice(),
                        currentItem.getDiscount()

                ));

                Toast.makeText(ItemDetailActivity.this,"Add To Cart", Toast.LENGTH_SHORT).show();
            }
        });


        item_image = (ImageView)findViewById(R.id.img_item);
        item_name = (TextView) findViewById(R.id.item_name);
        item_price = (TextView)findViewById(R.id.item_price);
        item_description = (TextView)findViewById(R.id.item_description);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Item Id from Intent
        if(getIntent() != null)
            itemId = getIntent().getStringExtra("ItemId");
        if(!itemId.isEmpty())
        {
            if(Common.isConnectedToInterner(getBaseContext())) {
                getDetailItem(itemId);
            }
            else
            {
                Toast.makeText(ItemDetailActivity.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetailItem(String itemId) {
        items.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(Item.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentItem.getImage())
                        .into(item_image);
                collapsingToolbarLayout.setTitle(currentItem.getName());

                item_price.setText(currentItem.getPrice());
                item_name.setText(currentItem.getName());
                item_description.setText(currentItem.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.example.easybuy.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easybuy.R;
import com.example.easybuy.model.Products;
import com.example.easybuy.ui.ProductsList;
import com.example.easybuy.ui.ShoppingList;
import com.example.easybuy.ui.adapter.helper.ItemTouchHelperAdapter;
import com.example.easybuy.ui.main.PlaceholderFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<Products> productsList;
    private ItemTouchHelper itemTouchHelper;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid = FirebaseAuth.getInstance().getUid();
    private String COLLECTION = uid;
    Products products;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public ShoppingListAdapter(Context context, List<Products> productsList){
        this.productsList = productsList;
        this.mContext = context;
    }

    @NonNull
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shopping_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        products = productsList.get(position);
        holder.linkProduct(products);
    }

    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Products spotA = productsList.get(fromPosition);
        Products spotB = productsList.get(toPosition);

        db.collection(COLLECTION).document(spotA.getProduct()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentA = task.getResult();
                Products productsA = documentA.toObject(Products.class);
                productsA.setId(documentA.getId());
                db.collection(COLLECTION).document(spotB.getTitle()).set(productsA);
            }
        });

        db.collection(COLLECTION).document(spotB.getProduct()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentB = task.getResult();
                Products productsB = documentB.toObject(Products.class);
                productsB.setId(documentB.getId());
                db.collection(COLLECTION).document(spotA.getTitle()).set(productsB);
            }
        });

        productsList.add(fromPosition, spotB);
        productsList.add(toPosition, spotA);
        productsList.remove(fromPosition);
        productsList.remove(toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        Products products = productsList.remove(position);
        db.collection(COLLECTION).document(products.getTitle()).delete();
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper itemTouchHelper){
        this.itemTouchHelper = itemTouchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, GestureDetector.OnGestureListener {
        private TextView shoppingList;
        GestureDetector gestureDetector;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingList = itemView.findViewById(R.id.textViewShoppingList);
            gestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        public void linkProduct(Products products){
            shoppingList.setText(products.getTitle());
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Products products = productsList.get(getAdapterPosition());
            Intent intent = new Intent(mContext, ProductsList.class);
            intent.putExtra("product", products);
            mContext.startActivity(intent);

            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            itemTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
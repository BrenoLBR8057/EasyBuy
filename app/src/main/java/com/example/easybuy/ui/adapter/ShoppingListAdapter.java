package com.example.easybuy.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easybuy.MainActivity;
import com.example.easybuy.R;
import com.example.easybuy.model.Products;
import com.example.easybuy.ui.adapter.helper.ItemTouchHelperAdapter;
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
    private Context context;

    public ShoppingListAdapter(Context context, List<Products> productsList){
        this.productsList = productsList;
        this.context = context;
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

        db.collection(COLLECTION).document(spotA.getTitle()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentA = task.getResult();
                Products productsA = documentA.toObject(Products.class);
                productsA.setId(documentA.getId());
                db.collection(COLLECTION).document(spotB.getTitle()).set(productsA);
            }
        });

        db.collection(COLLECTION).document(spotB.getTitle()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
            MainActivity mainActivity = new MainActivity();
        mainActivity.goToProductsList(products);
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
package com.example.easybuy.ui.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybuy.ui.ShoppingList;
import com.example.easybuy.ui.adapter.ShoppingListAdapter;
import com.example.easybuy.ui.adapter.helper.ItemTouchHelperAdapter;

public class MyItemTouchHelper extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter helperAdapter;

    public MyItemTouchHelper(ItemTouchHelperAdapter helperAdapter) {
        this.helperAdapter = helperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return getFlags();
    }

    private int getFlags(){
        final int swipe = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        final int drag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(drag, swipe);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        helperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        helperAdapter.onItemSwiped(viewHolder.getAdapterPosition());
    }
}

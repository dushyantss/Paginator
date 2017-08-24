package com.dushyantss.paginator;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by dushyant on 22/08/17.
 */

class PaginatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  static final int INDICATOR_COUNT = 1;

  @LayoutRes private static final int TYPE_INDICATOR = R.layout.item_loading;

  private final RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
  private final LayoutInflater inflater;

  /**
   * To track whether all the data has been loaded
   */
  private boolean allLoaded = false;

  PaginatorAdapter(@NonNull Context context,
      @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
    this.adapter = adapter;
    this.inflater = LayoutInflater.from(context);
    initObserve();
  }

  //region Overridden common methods
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_INDICATOR) {
      View view = inflater.inflate(TYPE_INDICATOR, parent, false);
      return new IndicatorViewHolder(view);
    }

    return adapter.onCreateViewHolder(parent, viewType);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (position == getItemCount() - INDICATOR_COUNT) {
      ((IndicatorViewHolder) holder).setAllLoaded(allLoaded);
    } else {
      adapter.onBindViewHolder(holder, position);
    }
  }

  @Override
  public int getItemCount() {
    return adapter.getItemCount() + INDICATOR_COUNT;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == getItemCount() - 1) {
      return TYPE_INDICATOR;
    }
    return adapter.getItemViewType(position);
  }
  //endregion

  // region Paginator only methods
  void setAllLoaded(boolean allLoaded) {
    this.allLoaded = allLoaded;
    notifyItemChanged(getItemCount() - INDICATOR_COUNT);
  }
  //endregion

  //region Helper methods
  private void initObserve() {
    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      public void onChanged() {
        notifyDataSetChanged();
      }

      public void onItemRangeChanged(int positionStart, int itemCount) {
        notifyItemRangeChanged(positionStart, itemCount);
      }

      public void onItemRangeInserted(int positionStart, int itemCount) {
        notifyItemRangeInserted(positionStart, itemCount);
      }

      public void onItemRangeRemoved(int positionStart, int itemCount) {
        notifyItemRangeRemoved(positionStart, itemCount);
      }

      public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        notifyItemMoved(fromPosition, toPosition);
      }
    });
  }
  //endregion

  //region ViewHolders
  static class IndicatorViewHolder extends RecyclerView.ViewHolder {

    public IndicatorViewHolder(View itemView) {
      super(itemView);
    }

    public void setAllLoaded(boolean allLoaded) {
      if (allLoaded) {
        itemView.setVisibility(View.GONE);
      } else {
        itemView.setVisibility(View.VISIBLE);
      }
    }
  }
  //endregion

  //region passing on all the calls to the underlying adapter
  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
      List<Object> payloads) {
    if (position == getItemCount() - INDICATOR_COUNT) {
      ((IndicatorViewHolder) holder).setAllLoaded(allLoaded);
    } else {
      adapter.onBindViewHolder(holder, position, payloads);
    }
  }

  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(hasStableIds);
    adapter.setHasStableIds(hasStableIds);
  }

  @Override
  public long getItemId(int position) {
    if (position == getItemCount() - INDICATOR_COUNT) {
      return TYPE_INDICATOR;
    }
    return adapter.getItemId(position);
  }

  @Override
  public void onViewRecycled(RecyclerView.ViewHolder holder) {
    adapter.onViewRecycled(holder);
  }

  @Override
  public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
    if (holder instanceof IndicatorViewHolder) {
      return super.onFailedToRecycleView(holder);
    }
    return adapter.onFailedToRecycleView(holder);
  }

  @Override
  public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    adapter.onViewAttachedToWindow(holder);
  }

  @Override
  public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
    adapter.onViewDetachedFromWindow(holder);
  }
  //endregion
}

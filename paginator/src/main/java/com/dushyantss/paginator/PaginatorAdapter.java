package com.dushyantss.paginator;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dushyant on 22/08/17.
 */

public class PaginatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int INDICATOR_COUNT = 1;

  @LayoutRes private static final int TYPE_INDICATOR = R.layout.item_loading;

  private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
  private LayoutInflater inflater;

  private boolean allLoaded = false;

  PaginatorAdapter(Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
    this.adapter = adapter;
    this.inflater = LayoutInflater.from(context);
  }

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
    if (position == getItemCount() - 1) {
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

  void setAllLoaded(boolean allLoaded) {
    this.allLoaded = allLoaded;
    notifyItemChanged(getItemCount() - INDICATOR_COUNT);
  }

  static class IndicatorViewHolder extends RecyclerView.ViewHolder {

    public IndicatorViewHolder(View itemView) {
      super(itemView);
    }

    public void setAllLoaded(boolean allLoaded) {
      if (allLoaded) {
        itemView.setVisibility(View.GONE);
      }
    }
  }
}

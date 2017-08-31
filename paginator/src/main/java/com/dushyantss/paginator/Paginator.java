package com.dushyantss.paginator;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * This class should be used whenever we want a paginated recyclerview. We should call this class's
 * start and stop methods in corresponding fragment/activity's onStart and onStop methods to clear
 * the scrolllistener.
 *
 * We should also call the method dataLoaded whenever new data is loaded.
 *
 * Created by dushyant on 22/08/17.
 */

public class Paginator {

  RecyclerView recyclerView;
  LinearLayoutManager layoutManager;
  PaginatorAdapter adapter;
  RecyclerView.OnScrollListener scrollListener;

  private Callback callback;

  boolean isLoading;
  boolean allLoaded;

  int prefetchLimit;

  /**
   * This class will also do the setup for the recyclerview
   */
  public Paginator(@NonNull RecyclerView recyclerView, @NonNull LinearLayoutManager layoutManager,
      @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, int prefetchLimit,
      @NonNull Callback callback) {
    this.recyclerView = recyclerView;
    this.layoutManager = layoutManager;
    this.adapter = new PaginatorAdapter(recyclerView.getContext(), adapter);
    this.prefetchLimit = prefetchLimit;
    this.callback = callback;
    init();
  }

  //region public methods

  /**
   * Call this method when data has been loaded and updated in the underlying adapter.
   *
   * @param allLoaded whether all the data has been loaded or if we still require pagination
   */
  public void dataLoaded(boolean allLoaded) {
    this.allLoaded = allLoaded;
    this.isLoading = false;
    adapter.setAllLoaded(allLoaded);
    checkAndLoadData();
  }

  /**
   * Call this method when there was an error loading data
   */
  public void dataLoadError() {
    this.isLoading = false;
  }

  /**
   * Call this method in the rare case where you have cleared the underlying adapter and want to
   * reset paginator
   */
  public void reset() {
    dataLoaded(false);
  }

  /**
   * Call this method in the onStart method, to add the scrollListener.
   */
  public void start() {
    stop();
    recyclerView.addOnScrollListener(scrollListener);
    checkAndLoadData();
  }

  /**
   * Call this method in the onStop method to remove the scrollListener
   */
  public void stop() {
    recyclerView.removeOnScrollListener(scrollListener);
    isLoading = false;
  }
  //endregion

  //region Helper methods
  private void init() {

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    scrollListener = new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          checkAndLoadData();
        }
      }
    };
  }

  private void checkAndLoadData() {
    if (!isLoading && !allLoaded) {

      int position = layoutManager.findLastVisibleItemPosition();
      int i = adapter.getItemCount() - PaginatorAdapter.INDICATOR_COUNT - prefetchLimit;
      boolean loadMore = position != RecyclerView.NO_POSITION && position >= i;

      if (loadMore) {
        isLoading = true;
        callback.loadMoreItems();
      }
    }
  }
  //endregion

  //region Callback interface
  public interface Callback {
    void loadMoreItems();
  }
  //endregion
}

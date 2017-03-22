package br.com.pedrosilva.tecnonutri.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.pedrosilva.tecnonutri.R;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.SingleOnClickListener;

/**
 * Created by psilva on 3/16/17.
 */
public abstract class GenericAdapter<T, A extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<A> {

    private final Context context;
    private List<T> items;

    private final boolean hasLoader;
    private boolean listEnded;

    protected static final int VIEWTYPE_ITEM = 0;
    protected static final int VIEWTYPE_LOADER = 1;

    private RetryClickListener retryClickListener;
    private String errorMessage;
    private boolean showError;

    public GenericAdapter(Context context) {
        this.context = context;
        this.hasLoader = false;
        this.items = new ArrayList<>();
    }

    public GenericAdapter(Context context, boolean hasLoader) {
        this.context = context;
        this.hasLoader = hasLoader;
        this.items = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasLoader && position >= items.size()) {
            return VIEWTYPE_LOADER;
        }
        return VIEWTYPE_ITEM;
    }

    public Context getContext() {
        return context;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = sortItems(items);
        this.listEnded = false;
        notifyDataSetChanged();
    }

    public void appendItems(List<T> items) {
        this.listEnded = false;
        items = sortItems(items);
        int positionStart = getItemCount();
        int itemCount = 0;
        for (T item : items) {
            if (!this.items.contains(item)) {
                this.items.add(item);
                itemCount++;
            }
        }
        itemCount = getItemCount();
        if (positionStart != itemCount) {
            //notifyDataSetChanged();
            notifyItemChanged(positionStart - 1);
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    protected List<T> sortItems(List<T> items) {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size() + (hasLoader && !listEnded ? 1 : 0);
    }

    public T getItem(int pos) {
        if (items.size() > pos)
            return items.get(pos);
        return null;
    }

    public void setRetryClickListener(RetryClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }

    public void notifyEndList() {
        listEnded = true;
        notifyItemRemoved(items.size());
    }

    public void notifyError (String message) {
        if (!listEnded) {
            this.showError = true;
            this.errorMessage = message;
            notifyItemChanged(items.size());
        }
    }

    public boolean isListEnded() {
        return listEnded;
    }

    class ViewHolderLoader extends RecyclerView.ViewHolder {

        private View progressBar;
        private View llError;
        private TextView tvErrorMessage;

        public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
        public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

        public ViewHolderLoader(View v) {
            super(v);
            bindElements(v);
        }

        private void bindElements(View v) {
            progressBar = v.findViewById(R.id.progress_bar);
            llError = v.findViewById(R.id.ll_error);
            tvErrorMessage = (TextView) v.findViewById(R.id.tv_error_message);
        }

        public void setup() {
            setup(MATCH_PARENT, WRAP_CONTENT);
        }

        public void setup(int height1, int heightMore) {
            ViewGroup.LayoutParams vhLayoutParams = itemView.getLayoutParams();
            if (getItemCount() == 1) {
                vhLayoutParams.height = height1;
            } else {
                vhLayoutParams.height = heightMore;
            }
            itemView.setLayoutParams(vhLayoutParams);

            llError.setOnClickListener(new SingleOnClickListener() {
                @Override
                public void onItemClick(View view) {
                    if (retryClickListener != null) {
                        showError = false;
                        msgError();
                        retryClickListener.onRetry();
                    }
                }
            });

            msgError();
        }

        private void msgError() {
            if (showError) {
                progressBar.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(errorMessage);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                llError.setVisibility(View.INVISIBLE);
            }
        }

    }

    public interface RetryClickListener {
        void onRetry();
    }
}
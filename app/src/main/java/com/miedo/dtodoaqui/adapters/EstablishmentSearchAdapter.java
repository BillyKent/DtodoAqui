package com.miedo.dtodoaqui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.data.EstablishmentSearchTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EstablishmentSearchAdapter extends RecyclerView.Adapter<EstablishmentSearchAdapter.ViewHolder> {

    public interface OnClickViewHolder {
        public void clickViewHolder(EstablishmentSearchTO est);
    }

    private final OnClickViewHolder listener;
    private List<EstablishmentSearchTO> data;

    public EstablishmentSearchAdapter(OnClickViewHolder listener) {
        this.listener = listener;
        data = new ArrayList<>();
    }

    // actualiza el recyclerview
    public void setData(List<EstablishmentSearchTO> newData) {
        if (data != null) {
            EstablishmentUserDiffCallback postDiffCallback = new EstablishmentUserDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);

        } else {
            // first initialization
            data = newData;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.cardview_establishment_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        @BindView(R.id.nomEstTextView)
        TextView nomEstTextView;
        @BindView(R.id.dirEstTextView)
        TextView dirEstTextView;
        @BindView(R.id.fotEstImageView)
        ImageView fotEstImageView;
        @BindView(R.id.resEstRatingBar)
        RatingBar resEstRatingBar;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            // Settear Views
            ButterKnife.bind(this, mView);

        }

        void bind(final EstablishmentSearchTO est) {
            if (est != null) {

                nomEstTextView.setText(est.getName());
                dirEstTextView.setText(est.getAddress());
                resEstRatingBar.setRating(est.getRating());

                Picasso.get().load(est.getUrlImage()).into(fotEstImageView);

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.clickViewHolder(est);
                    }
                });
            }
        }

    }

    /*
     * Clase de utilidad para optimizar la carga de datos en el recycler.
     * */
    class EstablishmentUserDiffCallback extends DiffUtil.Callback {

        private final List<EstablishmentSearchTO> oldItems, newItems;

        EstablishmentUserDiffCallback(List<EstablishmentSearchTO> oldItems, List<EstablishmentSearchTO> newItems) {
            this.oldItems = oldItems;
            this.newItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return oldItems.size();
        }

        @Override
        public int getNewListSize() {
            return newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
        }
    }
}
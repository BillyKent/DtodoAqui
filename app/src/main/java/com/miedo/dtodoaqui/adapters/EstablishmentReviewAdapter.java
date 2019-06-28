package com.miedo.dtodoaqui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.data.EstablishmentReviewTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EstablishmentReviewAdapter extends RecyclerView.Adapter<EstablishmentReviewAdapter.ViewHolder>  {

    public interface OnClickViewHolder {
        public void clickViewHolder(EstablishmentReviewTO est);
    }

    private final OnClickViewHolder listener;
    private List<EstablishmentReviewTO> establishments;
    private Context context;

    public EstablishmentReviewAdapter(OnClickViewHolder listener, List<EstablishmentReviewTO> establishments, Context context) {
        this.listener = listener;
        this.establishments = establishments;
        this.context = context;
    }

    // actualiza el recyclerview
    public void setData(List<EstablishmentReviewTO> newData) {
        if (establishments != null) {
            EstablishmentReviewDiffCallback postDiffCallback = new EstablishmentReviewDiffCallback(establishments, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

            establishments.clear();
            establishments.addAll(newData);
            diffResult.dispatchUpdatesTo(this);

        } else {
            // first initialization
            establishments = newData;
        }
    }

    @Override
    public EstablishmentReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.cardview_establishment_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(establishments.get(position));
    }

    @Override
    public int getItemCount() {
        if(establishments != null)
            return establishments.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        @BindView(R.id.establishmentReviewUserNameTV)
        TextView userName;
        @BindView(R.id.establishmentReviewNameTV)
        TextView name;
        @BindView(R.id.establishmentReviewDescriptionTV)
        TextView description;
        @BindView(R.id.establishmentReviewRB)
        RatingBar ratingBar;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            // Settear Views
            ButterKnife.bind(this, mView);

        }

        void bind(final EstablishmentReviewTO est) {
            if (est != null) {

                userName.setText(est.getUsername());
                name.setText(est.getName());
                description.setText(est.getDecription());
                ratingBar.setRating(est.getRating());

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
    class EstablishmentReviewDiffCallback extends DiffUtil.Callback {

        private final List<EstablishmentReviewTO> oldItems, newItems;

        EstablishmentReviewDiffCallback(List<EstablishmentReviewTO> oldItems, List<EstablishmentReviewTO> newItems) {
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

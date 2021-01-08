package maher.majed.coronavirus.CoronaInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import maher.majed.coronavirus.R;


public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {

    private Context context;
    List<Data> dataList;
    String language;

    public InformationAdapter(Context context, List<Data> dataList, String language) {
        this.context = context;
        this.dataList = dataList;
        this.language = language;
    }


    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (language.equals("English")) {
            view = LayoutInflater.from(context).inflate(R.layout.information_en_item, viewGroup, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.information_ar_item, viewGroup, false);
        }
        return new InformationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InformationAdapter.ViewHolder viewHolder, int i) {
        final Data data = dataList.get(i);
        viewHolder.titleTv.setText(data.getTitle());
        viewHolder.bodyTv.setText(data.getBody());

        Glide.with(context).load(data.getImagePath()).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        public TextView bodyTv;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleTv);
            bodyTv = itemView.findViewById(R.id.bodyTv);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}



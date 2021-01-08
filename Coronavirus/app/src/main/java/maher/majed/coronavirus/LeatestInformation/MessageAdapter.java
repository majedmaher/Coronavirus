package maher.majed.coronavirus.LeatestInformation;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import maher.majed.coronavirus.R;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages){
        this.context = context;
        this.messages = messages;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.message_item, viewGroup, false);
        return new MessageAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Message message = messages.get(i);

        viewHolder.message_user.setText(message.getMessageUser());
        viewHolder.message_text.setText(message.getMessageText());
        viewHolder.message_time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                message.getMessageTime()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView message_user;
        public TextView message_text;
        public TextView message_time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message_user = itemView.findViewById(R.id.message_user);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);

        }
    }

}
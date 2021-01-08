package maher.majed.coronavirus.LeatestInformation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import maher.majed.coronavirus.PersonalAccount.LoginFragment;
import maher.majed.coronavirus.PersonalAccount.User;
import maher.majed.coronavirus.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class LeatestInformationMessagesFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    DatabaseReference referenceMessages;

    EditText text_send;
    ImageButton send_btn;
    TextView textView2;
    TextView textView3;

    MessageAdapter messageAdapter;
    List<Message> messages;

    String messageUser;


    String[] limits = { "8", "16", "24", "32", "all"};

    int limit = 8;

    RecyclerView recyclerview_chat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leatest_information_messages, container, false);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        Spinner spin = view.findViewById(R.id.spinnerLimit);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,limits);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    messageUser = user.getUsername();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        messages = new ArrayList<>();

        text_send = view.findViewById(R.id.text_send);
        send_btn = view.findViewById(R.id.send_btn);


        recyclerview_chat = view.findViewById(R.id.recyclerview_chat);
        recyclerview_chat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerview_chat.setLayoutManager(linearLayoutManager);

        referenceMessages = FirebaseDatabase.getInstance().getReference("Messages");

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = text_send.getText().toString();

                if (!message.equals("")) {
                    if (firebaseUser != null) {
                        sendMessage(messageUser, message, new Date().getTime());
                    }else {
                        LoginFragment loginFragment = new LoginFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("message",message);
                        FragmentTransaction transaction = getFragmentManager()
                                .beginTransaction();
                        loginFragment.setArguments(bundle);
                        transaction.replace(R.id.fragment_container, loginFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } else {
                    Toast.makeText(getContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");

            }
        });

        return view;

    }


    private void sendMessage(String messageUser, final String messageText, long messageTime) {
        DatabaseReference sendMessageReferance = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("messageUser", messageUser);
        hashMap.put("messageText", messageText);
        hashMap.put("messageTime", messageTime);

        sendMessageReferance.child("Messages").push().setValue(hashMap);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (limits[position]){
            case "all":
                textView2.setText("Show");
                textView3.setText("Messages");

                referenceMessages.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            messages.add(message);

                            messageAdapter = new MessageAdapter(getContext(), messages);
                            recyclerview_chat.setAdapter(messageAdapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;

            default:
                textView2.setText("Show Last");
                textView3.setText("Messages");
                limit = Integer.parseInt(limits[position]);

                Query query = referenceMessages.orderByKey().limitToLast(limit);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            messages.add(message);

                            messageAdapter = new MessageAdapter(getContext(), messages);
                            recyclerview_chat.setAdapter(messageAdapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

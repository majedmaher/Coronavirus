package maher.majed.coronavirus.PersonalAccount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import androidx.fragment.app.FragmentTransaction;
import maher.majed.coronavirus.LeatestInformation.LeatestInformationMessagesFragment;
import maher.majed.coronavirus.R;

public class LoginFragment extends Fragment {

    EditText passwordEd, emailEd;
    Button loginBtn, signupBtn;
    FirebaseAuth auth;

    String message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            message = getArguments().getString("message");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        passwordEd = view.findViewById(R.id.passwordEd);
        emailEd = view.findViewById(R.id.emailEd);
        loginBtn = view.findViewById(R.id.loginBtn);
        signupBtn = view.findViewById(R.id.signupBtn);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = passwordEd.getText().toString();
                final String email = emailEd.getText().toString();

                if (password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(view.getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);

                                                DatabaseReference sendMessageReferance = FirebaseDatabase.getInstance().getReference();

                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("messageUser", user.getUsername());
                                                hashMap.put("messageText", message);
                                                hashMap.put("messageTime", new Date().getTime());

                                                sendMessageReferance.child("Messages").push().setValue(hashMap);
                                                LeatestInformationMessagesFragment leatestInformationMessagesFragment = new LeatestInformationMessagesFragment();
                                                FragmentTransaction transaction = getFragmentManager()
                                                        .beginTransaction();
                                                transaction.replace(R.id.fragment_container, leatestInformationMessagesFragment);
                                                transaction.addToBackStack(null);
                                                transaction.commit();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(view.getContext(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                Bundle bundle = new Bundle();
                bundle.putString("message", message);
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                registerFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, registerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
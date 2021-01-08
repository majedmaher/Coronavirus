package maher.majed.coronavirus.PersonalAccount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentTransaction;
import maher.majed.coronavirus.LeatestInformation.LeatestInformationMessagesFragment;
import maher.majed.coronavirus.R;

public class RegisterFragment extends Fragment {

    EditText userNameEd, passwordEd, emailEd;
    Button registerBtn;
    FirebaseAuth auth;
    DatabaseReference reference;

    String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            message = getArguments().getString("message");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        userNameEd = view.findViewById(R.id.usernameEd);
        passwordEd = view.findViewById(R.id.passwordEd);
        emailEd = view.findViewById(R.id.emailEd);
        registerBtn = view.findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = userNameEd.getText().toString();
                final String password = passwordEd.getText().toString();
                final String email = emailEd.getText().toString();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()){
                    Toast.makeText(view.getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }else if (password.length()<8){
                    Toast.makeText(view.getContext(), "password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }else{
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        if (firebaseUser != null) {
                                            String userId = firebaseUser.getUid();


                                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                            Map<String, String> user = new HashMap<>();
                                            user.put("id", userId);
                                            user.put("username", username);

                                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("messageUser", username);
                                                        hashMap.put("messageText", message);
                                                        hashMap.put("messageTime", new Date().getTime());

                                                        FirebaseDatabase.getInstance().getReference().child("Messages").push().setValue(hashMap);

                                                        LeatestInformationMessagesFragment leatestInformationMessagesFragment = new LeatestInformationMessagesFragment();
                                                        FragmentTransaction transaction = getFragmentManager()
                                                                .beginTransaction();
                                                        transaction.replace(R.id.fragment_container, leatestInformationMessagesFragment);
                                                        transaction.addToBackStack(null);
                                                        transaction.commit();                                                    }
                                                }
                                            });
                                        }
                                    }else {
                                        Toast.makeText(view.getContext(), "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }
}
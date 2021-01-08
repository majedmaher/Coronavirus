package maher.majed.coronavirus.CoronaInformation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import maher.majed.coronavirus.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CoronaInformationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String[] language = {"English", "Arabic"};

    List<Data> dataList;


    RecyclerView recyclerview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_corona_information, container, false);


        dataList = new ArrayList<>();

        Spinner spin = view.findViewById(R.id.spinnerLanguage);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(view.getContext(),android.R.layout.simple_spinner_item,language);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerview.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

        if (language[position].equals("English")) {

            FirebaseFirestore.getInstance().collection("data_english").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        dataList.clear();
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            Data data = documentSnapshot.toObject(Data.class);
                            dataList.add(data);
                        }
                        InformationAdapter informationAdapter = new InformationAdapter(view.getContext(), dataList, language[position]);
                        recyclerview.setAdapter(informationAdapter);
                    }


                }
            });

        }else {

            FirebaseFirestore.getInstance().collection("data_arabic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        dataList.clear();
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            Data data = documentSnapshot.toObject(Data.class);
                            dataList.add(data);
                        }
                        InformationAdapter informationAdapter = new InformationAdapter(view.getContext(), dataList, language[position]);
                        recyclerview.setAdapter(informationAdapter);
                    }


                }
            });
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
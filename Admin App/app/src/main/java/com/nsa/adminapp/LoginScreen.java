package com.nsa.adminapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LoginScreen extends Fragment {
    Button mButton;
    EditText username;
    EditText password;
    ImageView logo;
    TextView title;

    public LoginScreen() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
        main.setDrawerVisible(false);

        logo = view.findViewById(R.id.logo);
        logo.setY(-1250);
        logo.setX(-500);
        title = view.findViewById(R.id.title);
        title.setY(-100);
        username = view.findViewById(R.id.editText2);
        password = view.findViewById(R.id.editText1);
        mButton = view.findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    Toast.makeText(view.getContext(), "Login Sucsessful", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    ft.replace(R.id.start_fragment, new HomePage(), "detailFragment");
                    ft.commit();
                } else {
                    Toast.makeText(view.getContext(), "Login Unsucsessful", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}

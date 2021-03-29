package ru.konstantin_starikov.samsung.izhhelper.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.konstantin_starikov.samsung.izhhelper.R;
import ru.konstantin_starikov.samsung.izhhelper.models.TypeClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViolationTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationTypeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView violationTypeIcon;
    private TextView violationTypeName;

    private TypeClickListener typeClickListener;

    public ViolationTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment violationTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViolationTypeFragment newInstance(String param1, String param2) {
        ViolationTypeFragment fragment = new ViolationTypeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_violation_type, container, false);
        violationTypeIcon = rootView.findViewById(R.id.violationTypeIcon);
        violationTypeName = rootView.findViewById(R.id.violationTypeName);
        rootView.findViewById(R.id.fragmentTypeClickButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeClickListener.execute();
            }
        });
        return rootView;
    }

    public void setViolationTypeIcon(Drawable violationTypeIcon)
    {
        this.violationTypeIcon.setImageDrawable(violationTypeIcon);
    }

    public void setViolationTypeName(String violationTypeName)
    {
        this.violationTypeName.setText(violationTypeName);
    }

    public void setTypeClickListener(TypeClickListener typeClickListener)
    {
        this.typeClickListener = typeClickListener;
    }
}
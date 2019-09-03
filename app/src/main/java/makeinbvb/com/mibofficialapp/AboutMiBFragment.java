package makeinbvb.com.mibofficialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class AboutMiBFragment extends Fragment
{
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_about_mib, container, false);

        viewPager = view.findViewById(R.id.view_pager_about_us);

        firebaseAuth = FirebaseAuth.getInstance();

        ViewPagerAdapter_AboutMiB adapter = new ViewPagerAdapter_AboutMiB(this.getActivity());

        viewPager.setAdapter(adapter);


        return view;
    }
}

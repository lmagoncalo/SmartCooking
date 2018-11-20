package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import smartcooking.developer.com.smartcooking.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    public static Spanned fromHtml(String html) {
        Spanned result = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ArrayList<Spanned> links = new ArrayList<>(6);
        links.add(fromHtml("<a href=\'https://www.facebook.com/SmartCookingApp/'>SmartCookingApp</a>"));
        links.add(fromHtml("<a href=\'http://maisumsobreculinaria.blogspot.pt/'>Mais Um Sobre Culinária</a>"));
        links.add(fromHtml("<a href=\'http://hojeparajantar.blogspot.pt/'>Hoje para jantar</a>"));
        links.add(fromHtml("<a href=\'http://uc.pt/'>Universidade de Coimbra</a>"));
        links.add(fromHtml("<a href=\'https://twitter.com/smartcookingapp'>@smartcookingapp</a>"));
        links.add(fromHtml("<a href='https://www.flaticon.com/authors/madebyoliver'>Madebyoliver</a>"));

        ArrayList<TextView> textViews = new ArrayList<>(6);
        textViews.add(0, (TextView) getActivity().findViewById(R.id.partners_one_textview));
        textViews.add(1, (TextView) getActivity().findViewById(R.id.partners_two_textview));
        textViews.add(2, (TextView) getActivity().findViewById(R.id.partners_three_textview));
        textViews.add(3, (TextView) getActivity().findViewById(R.id.facebook_link_textview));
        textViews.add(4, (TextView) getActivity().findViewById(R.id.twitter_link_textview));
        textViews.add(5, (TextView) getActivity().findViewById(R.id.icons_link_textview));

        for (int i = 0; i < 6; i++) {
            textViews.get(i).setText(links.get(i));
            textViews.get(i).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}

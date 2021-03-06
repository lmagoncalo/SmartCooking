package smartcooking.developer.com.smartcooking.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import smartcooking.developer.com.smartcooking.R;

import static android.text.Html.fromHtml;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_about, container, false);

        // displays the links for our partners, source of icons and our social media accounts
        ArrayList<Spanned> links = new ArrayList<>(6);
        links.add(this.fromHtml("<a href=\'http://maisumsobreculinaria.blogspot.pt/'>Mais Um Sobre Culinária</a>"));
        links.add(this.fromHtml("<a href=\'http://hojeparajantar.blogspot.pt/'>Hoje para jantar</a>"));
        links.add(this.fromHtml("<a href=\'http://uc.pt/'>Universidade de Coimbra</a>"));
        links.add(this.fromHtml("<a href=\'https://www.facebook.com/SmartCookingApp/'>SmartCookingApp</a>"));
        links.add(this.fromHtml("<a href=\'https://twitter.com/smartcookingapp'>@smartcookingapp</a>"));
        links.add(this.fromHtml("<a href=\'https://smashicons.com/'>Smashicons</a>"));

        ArrayList<TextView> textViews = new ArrayList<>(6);
        textViews.add(0, (TextView) result.findViewById(R.id.partners_one_textview));
        textViews.add(1, (TextView) result.findViewById(R.id.partners_two_textview));
        textViews.add(2, (TextView) result.findViewById(R.id.partners_three_textview));
        textViews.add(3, (TextView) result.findViewById(R.id.facebook_link_textview));
        textViews.add(4, (TextView) result.findViewById(R.id.twitter_link_textview));
        textViews.add(5, (TextView) result.findViewById(R.id.icons_link_textview));

        // to be displays as a link and not just text
        for (int i = 0; i < 6; i++) {
            textViews.get(i).setText(links.get(i));
            textViews.get(i).setMovementMethod(LinkMovementMethod.getInstance());
        }

        return result;
    }

    public Spanned fromHtml(String html){
        // create HTML element for different SDK version
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}

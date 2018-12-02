package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.utils.UpdateRecipesTask;

public class SplashFragment extends Fragment {

    private final String[] SPLASH_PHRASES = {"A aquecer o forno...", "YEEEEEEET", "2 Shots of vodka", "Blink motherfucker", "esabatad gnitadpU"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_splash, container, false);

        if (haveNetworkConnection()) { // Have internet connection
            ProgressBar progressBar = result.findViewById(R.id.progress_bar);

            setRandomPhrase(result);

            UpdateRecipesTask myTask = new UpdateRecipesTask(getContext(), progressBar, getActivity());

            myTask.execute();

            if (myTask.isError()) {
                crash("Ocorreu um erro inesperado aqui.");
            }
        }

        return result;
    }

    private void crash(String tipo) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());

        // set title
        alertDialogBuilder.setTitle("Erro");

        // set dialog message
        alertDialogBuilder
                .setMessage(tipo)
                .setCancelable(false)
                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        getActivity().finish();
                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void setRandomPhrase(View result){
        int max = SPLASH_PHRASES.length;
        Random rdm = new Random();
        int value = rdm.nextInt(max);

        TextView textView = (TextView) result.findViewById(R.id.progress_text);

        String str = SPLASH_PHRASES[value];
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> str: " + str);
        textView.setText(str);

    }

}

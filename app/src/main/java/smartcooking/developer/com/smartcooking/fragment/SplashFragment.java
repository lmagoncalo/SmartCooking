package smartcooking.developer.com.smartcooking.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.utils.UpdateRecipesTask;

public class SplashFragment extends Fragment {

    // list of strings from which 1 string will be randomly selected to be displayed during the SplashScreen
    private final String[] SPLASH_PHRASES = {"a aquecer o forno...", "a cozinhar os gatos...", "a preparar os melhores ingredientes...",
            "à caça de miscaros...", "a pescar peixe graúdo...", "a caçar o melhor javali...", "removendo o gluten do pão...",
            "a roubar receitas...", "a pôr a mesa...", "a descongelar os bifes..."};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_splash, container, false);

        ProgressBar progressBar = result.findViewById(R.id.progress_bar);

        setRandomPhrase(result);

        // initialize the "UpdateRecipesTask" AssyncTask
        // We use AssyncTasks because the API requests must be done inside an AssyncTask
        UpdateRecipesTask myTask = new UpdateRecipesTask(getContext(), progressBar, getActivity(), this);
        myTask.execute();

        return result;
    }

    private void setRandomPhrase(View result){
        // display the random phrase from the array
        TextView textView = result.findViewById(R.id.progress_text);
        textView.setText(SPLASH_PHRASES[new Random().nextInt(SPLASH_PHRASES.length)]);
    }

}

package smartcooking.developer.com.smartcooking.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.utils.UpdateRecipesTask;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 1;
    private Handler handler = new Handler();
    private TextView textView;

    //Carregar aqui as receitas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);

        /*if (getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            String id;
            if ((id = uri.getQueryParameter("id")) != null) {
                RecipeFragment recipeFragment = RecipeFragment.newInstance(Integer.parseInt(id));
                getFragmentManager().beginTransaction().replace(R.id.fragment, recipeFragment).commit();
                return;
            }
        }*/

        if (haveNetworkConnection()) { // Have internet connection
            try {
                UpdateRecipesTask myTask = new UpdateRecipesTask(this);
                myTask.execute().get();

                if (myTask.isError()) {
                    crash("Ocorreu um erro inesperado aqui.");
                } else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }

                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                textView = (TextView) findViewById(R.id.progress_text);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(1);
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BEGIN");
                        while (progressStatus < 100) {
                            progressStatus += 1;
                            // Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                    textView.setText(progressStatus+"/"+progressBar.getMax());
                                }
                            });
                            try {
                                // Sleep for 200 milliseconds.
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DONE");
                    }
                });
                t.start();
                t.join();

            } catch (InterruptedException | ExecutionException e) {
                crash("Ocorreu um erro inesperado aqui.");
            }
        }/*else{
            try {
                WriteObjectFile wo = new WriteObjectFile(this);
                wo.readObject("lista_receitas");

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }catch (Exception e){
                crash("Na primeira vez que abra a aplicação certifique-se que está conectado à Internet.");
            }
        }*/
    }

    private void crash(String tipo) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SplashScreen.this);

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
                        SplashScreen.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
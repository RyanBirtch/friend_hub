package cpen391.friendhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cpen391.friendhub.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Communicator";
    private static final String BASE_SERVER_URL = "https://friend-hub.herokuapp.com";
    private static final String EXIT_EXTRA = "EXIT";
    private static final String USER_ATTRIBUTE_KEY = "User Attributes";
    final User client = new User();
    final int NUM_QUESTIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button getUserButton = (Button) findViewById(R.id.getUser);

        Button goToSurveyButton = (Button) findViewById(R.id.SurveyButton);
        final Intent intent = new Intent(this, IntermediateSurveyActivity.class );
        goToSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Ryanout","Onclick  "+IntermediateSurveyActivity.class);
                startActivity(intent);
            }
        });
        getUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo(1);
            }
        });

    }

    public void onResume(Bundle savedInstanceState){
        super.onResume();
        Intent intent = getIntent();
        if(intent.hasExtra(EXIT_EXTRA)){
            double tempArray[] = intent.getDoubleArrayExtra(USER_ATTRIBUTE_KEY);
            for (int i = 0; i < NUM_QUESTIONS; i++) {
                client.updateAttributes(i, tempArray[i]);
                Log.e("RyanOut",client.attributesToString());
            }
        }
    }



    public void renderResponse(User client){
        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);

        name.setText(client.getName());
        email.setText(client.getEmail());
    }
    public void getUserInfo(int id) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.show(this, "Loading", "Wait while loading...");
        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_SERVER_URL)
                .build();

        ServerCallable service = retrofit.create(ServerCallable.class);

        Call<User> call = service.showUser("1");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                client.setId(response.body().getId());
                client.setName(response.body().getName());
                client.setEmail(response.body().getEmail());
                client.setBio(response.body().getBio());
                progress.dismiss();
                Log.e(TAG, "Success" + client);
                renderResponse(client);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Failed\n" + t.getMessage());
            }
        });

    }


}

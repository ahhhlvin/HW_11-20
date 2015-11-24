package ahhhlvin.c4q.nyc.alvinapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Query;

public class MainActivity extends AppCompatActivity {


        // you should either define client id and secret as constants or in string resources
        private final String clientId = "d4ea73a16c9b8d2d786abf0c65225ff4";
        private final String clientSecret = "1bc923d1e4578454cab62a60bb809774";
        private final String redirectUri = "sufeizhao://www.hello.com/";

        private final String token = "https://api.soundcloud.com/oauth2/token";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button loginButton = (Button) findViewById(R.id.loginbutton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(ServiceGenerator.API_BASE_URL + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code"));                    startActivity(intent);
                }
            });
        }


    public interface LoginService {
        @POST("/token")
        Call<AccessToken> getAccessToken(
                @Query("code") String code,
                @Query("grant_type") String grantType);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                LoginService loginService =
                        ServiceGenerator.createService(LoginService.class, clientId, clientSecret);
                Call<AccessToken> call = loginService.getAccessToken(code, "authorization_code");
                try {
                    AccessToken accessToken = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}

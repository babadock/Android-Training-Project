package vn.edu.hcmut.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView1);

        final Button loadButton = (Button) findViewById(R.id.button1);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpGetTask().execute();
            }
        });

    }
    private class HttpGetTask extends AsyncTask<Void, Void, String> {
        private  static final String TAG = "HttpGetTask";

       // private static final String USER_NAME = "";
        private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=London,uk";

        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            HttpURLConnection httpURLConnection = null;

            try {
                httpURLConnection = (HttpURLConnection) new URL(URL).openConnection();
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

                data = readStream(in);
            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } finally {
                if (null != httpURLConnection) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            mTextView.setText(result);
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e(TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }
    }

}

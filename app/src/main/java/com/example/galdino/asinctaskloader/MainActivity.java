package com.example.galdino.asinctaskloader;

import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.galdino.asinctaskloader.databinding.ActivityMainBinding;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    private ActivityMainBinding mBinding;
    private int LOADER_ID = 0;
    private int mSecondsLooping = 1;
    private CountDownTimer mCountDownTimer;
    private String mConstantTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        loadControls();
    }

    private void loadControls() {
        LoaderManager.LoaderCallbacks<String>  callbacks = MainActivity.this;
        getSupportLoaderManager().initLoader(LOADER_ID,null,callbacks);

        mCountDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.tvContador.setText(String.valueOf(mSecondsLooping));
                mSecondsLooping++;
            }

            @Override
            public void onFinish() {
                mBinding.tvContador.setText("Acabou");
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args)
    {
        return new AsyncTaskLoader<String>(this)
        {
            @Override
            protected void onStartLoading()
            {
                mBinding.pgLoader.setVisibility(View.VISIBLE);
                if(mConstantTest == null)
                {
                    forceLoad(); // Chama o método loadInBackground
                }
                else
                {
                    deliverResult(mConstantTest);
                }
            }

            @Override
            public String loadInBackground()
            {

                Connection.Response response;
                try {
                    response = Jsoup.connect("https://www.facebook.com/").followRedirects(false).execute();

                    /* response.statusCode() will return you the status code */
                    if (200 == response.statusCode()) {
//                        Document doc = Jsoup.connect("https://www.facebook.com/").get();
                        return "OK";
                    /* what ever you want to do */
                    }
                    else
                    {
                        return "ERRO 1";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "ERRO 2";
                }
            }

            @Override
            public void deliverResult(String data) {
                mConstantTest = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data)
    {
        mBinding.pgLoader.setVisibility(View.INVISIBLE);
        mCountDownTimer.cancel();
        if(!data.equals("OK"))
        {
            mBinding.tvContador.setText(data);
        }
        mBinding.tvContador.setText("Loader finalizado com sucesso");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}

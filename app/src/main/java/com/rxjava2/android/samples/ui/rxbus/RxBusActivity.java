package com.rxjava2.android.samples.ui.rxbus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.MyApplication;
import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.model.Events;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 06/02/17.
 * https://blog.mindorks.com/implementing-eventbus-with-rxjava-rxbus-e6c940a94bd8
 */

public class RxBusActivity extends AppCompatActivity {

    public static final String TAG = RxBusActivity.class.getSimpleName();
    TextView textView;
    Button button;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // do not send event after activity has been destroyed
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);

        ((MyApplication) getApplication()).sendAutoEvent();

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        disposables.add(((MyApplication) getApplication())
                .getRxBus()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> {
                    if (object instanceof Events.AutoEvent) {
                        textView.setText("Auto Event Received");
                    } else if (object instanceof Events.TapEvent) {
                        textView.setText("Tap Event Received");
                    }
                }));

        button.setOnClickListener(v -> ((MyApplication) getApplication())
                .getRxBus()
                .send(new Events.TapEvent()));
    }



}

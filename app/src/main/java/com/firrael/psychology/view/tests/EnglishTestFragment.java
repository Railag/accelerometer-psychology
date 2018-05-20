package com.firrael.psychology.view.tests;

import android.content.res.AssetManager;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firrael.psychology.AccelerometerListener;
import com.firrael.psychology.App;
import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.Answer;
import com.firrael.psychology.model.Difficulty;
import com.firrael.psychology.model.Result;
import com.firrael.psychology.model.english.Definition;
import com.firrael.psychology.model.english.Word;
import com.firrael.psychology.presenter.EnglishTestPresenter;
import com.firrael.psychology.view.base.BaseFragment;
import com.firrael.psychology.view.results.EnglishResultsFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by railag on 13.02.2018.
 */

@RequiresPresenter(EnglishTestPresenter.class)
public class EnglishTestFragment extends BaseFragment<EnglishTestPresenter> implements AccelerometerListener {

    private final static String TAG = EnglishTestFragment.class.getSimpleName();

    private final static int MAX_WORDS = 20;

    private Handler handler;

    private int wins;
    private long errors;

    @BindView(R.id.testBackground)
    View testBackground;

    @BindView(R.id.center_word)
    TextView centerWordView;

    @BindView(R.id.left_word)
    TextView leftWordView;

    @BindView(R.id.right_word)
    TextView rightWordView;

    private int currentWord = 0;

    private boolean locked;


    private long time;

    private ArrayList<String> usedWords = new ArrayList<>();
    private Random random;
    private List<Word> words = new ArrayList<>();
    private boolean isRightWordTrue = false;

    private ArrayList<Answer> answers = new ArrayList<>();
    private SensorEventListener sensorListener;

    public static EnglishTestFragment newInstance() {

        Bundle args = new Bundle();

        EnglishTestFragment fragment = new EnglishTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.englishTestTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_test_english;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler();
        random = new Random();

        loadWords();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorListener = Utils.registerSensor(getActivity(), this, 1, 3);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.unregisterSensor(getActivity(), sensorListener);
    }

    @Override
    protected void initView(View v) {
        replaceWords();
    }

    private void loadWords() {
        try {
            AssetManager manager = getActivity().getAssets();

            StringBuilder stringBuilder = new StringBuilder();
            InputStream html = manager.open(getString(R.string.words_json));
            BufferedReader in = new BufferedReader(new InputStreamReader(html, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                stringBuilder.append(str);
            }

            in.close();

            String wordsJson = stringBuilder.toString();

            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Word>>() {
            }.getType();

            Map<String, Word> wordsMap = gson.fromJson(wordsJson, mapType);
            for (Map.Entry<String, Word> entry : wordsMap.entrySet()) {
                String wordText = entry.getKey();
                Word word = entry.getValue();
                word.setWord(wordText);
                words.add(word);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.leftButton) // левое слово
    public void leftClick() {
        click(false);
    }

    @OnClick(R.id.rightButton) // правое слово
    public void rightClick() {
        click(true);
    }

    public void click(boolean right) {

        if (locked)
            return;

        locked = true;

        Difficulty diff = App.diff(getActivity());

        if (currentWord > MAX_WORDS * diff.getLevel()) {
            return;
        }

        double resultTime = Utils.calcTime(time);
        Answer answer = new Answer();
        answer.setTime(resultTime);

        if (right) {
            if (isRightWordTrue) {
                wins++;
                answer.setErrorValue(0);
            } else {
                errors++;
                answer.setErrorValue(1);
            }
        } else {
            if (!isRightWordTrue) {
                wins++;
                answer.setErrorValue(0);
            } else {
                errors++;
                answer.setErrorValue(1);
            }
        }

        answer.setNumber(answers.size());
        answers.add(answer);

        replaceWords();

        currentWord++;

        if (currentWord == MAX_WORDS * diff.getLevel()) {
            toNextTest();
        }

        locked = false;

        Toast.makeText(getActivity(), "Wins = " + wins + ", Fails = " + errors, Toast.LENGTH_SHORT).show();
    }

    private void replaceWords() {
        String centerWord = "";
        String leftWord = "";
        String rightWord = "";

        while (true) {
            Word word = getRandomWord();
            boolean hasDefinitions = word.getDefinitions() != null && word.getDefinitions().size() > 0;
            if (hasDefinitions) { // or take another word
                List<Definition> definitions = word.getDefinitions();
                for (Definition definition : definitions) {
                    List<String> synonyms = definition.getSynonyms();
                    boolean hasSynonyms = synonyms != null && synonyms.size() > 0;
                    if (hasSynonyms) { // or take another word
                        centerWord = word.getWord();

                        String firstSynonym = synonyms.get(0); // TODO more various selection
                        isRightWordTrue = random.nextBoolean();
                        if (isRightWordTrue) {
                            rightWord = firstSynonym;
                        } else {
                            leftWord = firstSynonym;
                        }

                        Word wrongWord = getRandomWord();
                        while (usedWords.contains(wrongWord.getWord())) {
                            wrongWord = getRandomWord();
                        }

                        if (isRightWordTrue) {
                            leftWord = wrongWord.getWord();
                        } else {
                            rightWord = wrongWord.getWord();
                        }

                        break; // valid words choosen
                    }
                }

                if (!TextUtils.isEmpty(centerWord)) {
                    break;
                }
            }
        }

        usedWords.add(centerWord);
        usedWords.add(leftWord);
        usedWords.add(rightWord);

        centerWordView.setText(centerWord);
        leftWordView.setText(leftWord);
        rightWordView.setText(rightWord);

        time = System.nanoTime();
    }

    private Word getRandomWord() {
        int randomWordIndex = random.nextInt(words.size());
        Word word = words.get(randomWordIndex);
        return word;
    }

    private void toNextTest() {
        ArrayList<Double> times = new ArrayList<>();

        for (Answer a : answers) {
            times.add(a.getTime());
        }

        startLoading();

        getPresenter().save(times, errors, usedWords);
    }

    public void onSuccess(Result result) {
        stopLoading();

        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        Bundle args = new Bundle();
        args.putParcelableArrayList(EnglishResultsFragment.RESULTS, answers);
        getMainActivity().toEnglishResults(args);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onLeft() {
        testBackground.setBackgroundColor(getResources().getColor(R.color.greyReaction));
        leftClick();
    }

    @Override
    public void onRight() {
        testBackground.setBackgroundColor(getResources().getColor(R.color.greyReaction));
        rightClick();
    }

    @Override
    public void onMinThreshold() {
        testBackground.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    @Override
    public void onUpdate(double x, double y, double z) {
    }

}
package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firrael.psychology.R;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Railag on 20.03.2017.
 */

public class InstructionFragment extends SimpleFragment {

    private final static String TYPE = "type";
    @BindView(R.id.instructionTitle)
    TextView instructionTitle;
    @BindView(R.id.instructionText)
    TextView instructionText;
    @BindView(R.id.instructionImage)
    ImageView instructionImage;

    public enum Test {
        RAMVolume,
        ATTENTION_STABILITY,
        STRESS_RESISTANCE,
        ENGLISH
    }

    public static InstructionFragment newInstance(Test test) {

        Bundle args = new Bundle();
        args.putSerializable(TYPE, test);

        InstructionFragment fragment = new InstructionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Test test = Test.RAMVolume;

    @Override
    protected String getTitle() {
        return getString(R.string.instruction);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_instruction;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().toggleArrow(true);

        Bundle args = getArguments();
        if (args != null && args.containsKey(TYPE)) {
            test = (Test) args.getSerializable(TYPE);

            String title = "";
            String instruction = "";
            int drawableId = 0;

            switch (test) {
                case RAMVolume:
                    title = getString(R.string.ramVolumeTestTitle);
                    instruction = getString(R.string.instruction_focusing);
                    drawableId = R.drawable.test3;
                    break;
                case ATTENTION_STABILITY:
                    title = getString(R.string.attentionStabilityTitle);
                    instruction = getString(R.string.instruction_attention_stability);
                    drawableId = R.drawable.test2;
                    break;
                case STRESS_RESISTANCE:
                    title = getString(R.string.stressResistanceTestTitle);
                    instruction = getString(R.string.instruction_stress_resistance);
                    drawableId = R.drawable.test4;
                    break;
                case ENGLISH:
                    title = getString(R.string.englishTestTitle);
                    instruction = getString(R.string.instruction_attention_stability);
                    drawableId = R.drawable.test;
                    break;
            }

            instructionTitle.setText(title);
            instructionText.setText(instruction);
            instructionImage.setImageDrawable(getResources().getDrawable(drawableId));
        }
    }

    @OnClick(R.id.startButton)
    public void start() {
        switch (test) {
            case RAMVolume:
                getMainActivity().toRAMVolumeTest();
                break;
            case ATTENTION_STABILITY:
                getMainActivity().toAttentionStabilityTest();
                break;
            case STRESS_RESISTANCE:
                getMainActivity().toStressResistanceTest();
                break;
            case ENGLISH:
                getMainActivity().toEnglishTest();
                break;
        }
    }
}

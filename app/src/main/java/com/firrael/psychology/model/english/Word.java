
package com.firrael.psychology.model.english;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word {

    private String word;

    @SerializedName("syllables")
    @Expose
    private Syllables syllables;
    @SerializedName("rhymePatterns")
    @Expose
    private RhymePatterns rhymePatterns;
    @SerializedName("definitions")
    @Expose
    private List<Definition> definitions = null;
    @SerializedName("letters")
    @Expose
    private Integer letters;
    @SerializedName("sounds")
    @Expose
    private Integer sounds;

    public Syllables getSyllables() {
        return syllables;
    }

    public void setSyllables(Syllables syllables) {
        this.syllables = syllables;
    }

    public RhymePatterns getRhymePatterns() {
        return rhymePatterns;
    }

    public void setRhymePatterns(RhymePatterns rhymePatterns) {
        this.rhymePatterns = rhymePatterns;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public Integer getLetters() {
        return letters;
    }

    public void setLetters(Integer letters) {
        this.letters = letters;
    }

    public Integer getSounds() {
        return sounds;
    }

    public void setSounds(Integer sounds) {
        this.sounds = sounds;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

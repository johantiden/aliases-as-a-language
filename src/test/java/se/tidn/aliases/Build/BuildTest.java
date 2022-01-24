package se.tidn.aliases.Build;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BuildTest {


    @Test
    public void name() {
        List<Sentence> sentences = Language.listAllPossibleSentences();

        Map<Sentence, Implementation> allSentences = Build.bake(Build.getAllSentencesRaw());

        for (Sentence sentence : sentences) {
            assertTrue(sentence + " must be implemented in Build.getAllSentences", allSentences.containsKey(sentence));
        }
    }
}
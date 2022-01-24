package se.tidn.aliases.Build;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class BuildTest {


    @Test
    public void testAllPossibleAreImplemented() {
        List<Sentence> sentences = Language.listAllPossibleSentences();

        Map<Sentence, Implementation> allSentences = Build.bake(Build.getAllSentencesRaw());

        for (Sentence sentence : sentences) {
            assertTrue(sentence + " must be implemented in Build.getAllSentences", allSentences.keySet().stream().anyMatch(k -> k.command.equals(sentence.command)));
        }
    }

    @Test
    public void testNoDuplicates() {
        List<Sentence> sentences = Language.listAllPossibleSentences();

        for (Sentence sentence : sentences) {
            Optional<Sentence> duplicateCommand = sentences.stream()
                    .filter(s -> !s.equals(sentence))
                    .filter(s -> s.command.equals(sentence.command))
                    .findAny();
            duplicateCommand.ifPresent(dupe -> {
                fail(String.format("The command '%s' is ambiguous. It could be either '%s' or '%s'", sentence.command, sentence.description, dupe.description));
            });
        }
    }
}
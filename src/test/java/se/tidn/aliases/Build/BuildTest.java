package se.tidn.aliases.Build;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BuildTest {


    @Test
    public void testAllPossibleAreImplemented() {
        List<Sentence> sentences = Language.listAllPossibleSentences();

        Map<Sentence, Implementation> allSentences = Build.bake(Build.getAllSentencesRaw());

        for (Sentence sentence : sentences) {
            assertTrue(allSentences.keySet().stream().anyMatch(k -> k.command.equals(sentence.command)), sentence + " must be implemented in Build.getAllSentences");
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

package se.tidn.aliases.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Language {

    public static List<Sentence> listAllPossibleSentences() {
        List<Sentence> nounEndings = allPossibleNounEndings();

        List<Sentence> commands = new ArrayList<>(nounEndings);

        for (Verb verb : Verb.values()) {
            commands.add(Sentence.of(verb.word));
        }

        for (Quantifier quantifier : Quantifier.values()) {
            commands.add(Sentence.of(quantifier.word));
        }

        for (Verb verb : Verb.values()) {
            for (Sentence nounEnding : nounEndings) {
                commands.add(Sentence.of(verb.word, nounEnding));
            }
        }

        for (Contextual contextual : Contextual.values()) {
            for (Verb verb : Verb.values()) {
                commands.add(Sentence.of(contextual.word, verb.word));
            }
        }

        for (Contextual contextual : Contextual.values()) {
            for (Verb verb : Verb.values()) {
                for (Sentence nounEnding : nounEndings) {
                    commands.add(Sentence.of(contextual.word, verb.word, nounEnding));
                }
            }
        }

        return removeKnownNonsenseCombinations(filterDuplicateWords(commands));
    }

    private static List<Sentence> removeKnownNonsenseCombinations(List<Sentence> sentences) {
        return sentences.stream()
                .filter(sentence -> !(contains(sentence, Quantifier.THIS) && contains(sentence, Noun.FILE)))
                .filter(sentence -> !(contains(sentence, Verb.EDIT) && contains(sentence, Noun.VERSION)))
                .filter(sentence -> !(contains(sentence, Verb.EDIT) && contains(sentence, Quantifier.ALL)))
//                .filter(sentence -> !(contains(sentence, Contextual.GIT) && contains(sentence, Verb.EDIT) && contains(sentence, Noun.BRANCH)))
                .filter(sentence -> !(contains(sentence, Quantifier.THIS) && contains(sentence, Noun.PASTING)))
                .filter(sentence -> !(contains(sentence, Sentence.of(Verb.YANK.word, Noun.PASTING.word))))
                .filter(sentence -> !(contains(sentence, Sentence.of(Verb.YANK.word, Quantifier.ALL.word, Noun.PASTING.word))))
                .collect(Collectors.toList());
    }

    private static boolean contains(Sentence sentence, Quantifier quantifier) {
        return sentence.description.contains(quantifier.word.description);
    }

    private static boolean contains(Sentence sentence, Noun noun) {
        return sentence.description.contains(noun.word.description);
    }

    private static boolean contains(Sentence sentence, Verb verb) {
        return sentence.description.contains(verb.word.description);
    }

    private static boolean contains(Sentence sentence, Sentence subSentence) {
        return sentence.description.contains(subSentence.description);
    }

    private static boolean contains(Sentence sentence, Contextual contextual) {
        return sentence.description.contains(contextual.word.description);
    }

    private static List<Sentence> allPossibleNounEndings() {
        List<Sentence> nounEndings = new ArrayList<>();

        for (Noun noun : Noun.values()) {
            nounEndings.add(Sentence.of(noun.word));
        }

        ArrayList<Sentence> nounEndingsCopy = new ArrayList<>(nounEndings);
        for (Quantifier quantifier : Quantifier.values()) {
            for (Sentence nounEnding : nounEndingsCopy) {
                nounEndings.add(Sentence.of(quantifier.word, nounEnding));
            }
        } return nounEndings;
    }

    static List<Sentence> filterDuplicateWords(List<Sentence> commands) {
        return commands.stream()
                .filter(sentence -> !hasDuplicateWords(sentence))
                .collect(Collectors.toList());
    }

    private static boolean hasDuplicateWords(Sentence sentence) {
        List<String> list = Arrays.stream(sentence.description.split(" ")).toList();
        HashSet<String> set = new HashSet<>(list);

        return set.size() != list.size();
    }

    enum Contextual {
//        GIT(Sentence.of("g", "github")),
        ;

        final Sentence word;

        Contextual(Sentence word) {
            this.word = word;
        }
    }

    private enum Quantifier {
        ALL(Sentence.of("a", "all")),
//        EACH(Sentence.of("e", "each")),
//        EXACT(Sentence.of("e", "exact")),
        ONE(Sentence.of("1", "one")),
        THIS(Sentence.of(".", "this")),

        ;

        private final Sentence word;

        Quantifier(Sentence word) {
            this.word = word;
        }
    }

    enum Noun {
        FILE(Sentence.of("f", "file")),
        BRANCH(Sentence.of("b", "branch")),
        PASTING(Sentence.of("p", "pasting")),
        REPO(Sentence.of("r", "repo")),
        VERSION(Sentence.of("v", "version")),
        ;

        final Sentence word;

        Noun(Sentence word) {
            this.word = word;
        }
    }

    enum Verb {
        OPEN(Sentence.of("o", "open")),
        YANK(Sentence.of("y", "yank")),
        EDIT(Sentence.of("z", "edit")),
        XEDIT(Sentence.of("x", "xedit")),
        ;

        final Sentence word;

        private Verb(Sentence word) {
            this.word = word;
        }
    }
}

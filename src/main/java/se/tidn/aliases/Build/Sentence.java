package se.tidn.aliases.Build;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Sentence {
    final String command;
    final String description;

    private Sentence(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public static Sentence of(String command, String description) {
        return new Sentence(command, description);
    }

    public static Sentence of(Sentence... words) {
        return of(Arrays.asList(words));
    }

    public static Sentence of(List<Sentence> words) {
        return new Sentence(getCommand(words), getDescription(words));
    }

    public static Sentence of(Sentence word, Sentence ending) {
        return new Sentence(
                word.command + ending.command,
                word.description + " " + ending.description);
    }

    public static String getCommand(List<Sentence> words) {
        return words.stream()
                .map(word -> word.command)
                .collect(Collectors.joining());
    }

    public static String getDescription(List<Sentence> words) {
        return words.stream()
                .map(word -> word.description)
                .collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sentence sentence = (Sentence) o;
        return Objects.equals(command, sentence.command) && Objects.equals(description, sentence.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, description);
    }

    @Override
    public String toString() {
        return "Sentence{" +
                command + ": " + description +
                '}';
    }
}

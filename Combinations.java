package com.looklet.lookcreator.batch;

import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Combinations {
    private static class Word {
        private final String command;
        private final String description;


        private Word(String command, String description) {
            this.command = command;
            this.description = description;
        }

        private static Word of(String command, String description) {
            return new Word(command, description);
        }
    }

    public static void main(String[] args) {

        List<Word> verbs = Lists.newArrayList(
                Word.of("b", "(go)branch"),
                Word.of("c", "commit"),
                Word.of("e","echo"),
                Word.of("g","get"),
                Word.of("m","merge"),
                Word.of("o","open"),
                Word.of("r","remove"),
                Word.of("v","(set)version"),
                Word.of("u","up"),
                Word.of("y","yank"),
                Word.of("z","subl"),
                Word.of("Z","sudo subl")
        );

        List<Word> modifiers = Lists.newArrayList(
                Word.of("", ""),
                Word.of("1", "one"),
                Word.of("2", "two"),
                Word.of("a", "all"),
                Word.of(".", "local"),
                Word.of("1.", "one local"),
                Word.of("2.", "two local"),
                Word.of("a.", "all local"),
                Word.of("g", "git"),
                Word.of("j", "jira")
        );

        List<Word> nouns = Lists.newArrayList(
                Word.of("b", "branch"),
                Word.of("d", "directory~"),
                Word.of("D", "directory/"),
                Word.of("d", "develop"),
                Word.of("f", "file~"),
                Word.of("F", "file/"),
                Word.of("m", "master"),
                Word.of("p", "pasting"),
                Word.of("v", "version"),
                Word.of("t", "text-line~"),
                Word.of("T", "text-line/")
        );

        for (Word verb : verbs) {
            System.out.println(verb.command+ " - " +
                    verb.description);
        }

        for (Word modifier : modifiers) {
            System.out.println(modifier.command+ " - " +
                    modifier.description);
        }

        for (Word noun : nouns) {
            System.out.println(noun.command+ " - " +
                    noun.description);
        }

        for (Word verb : verbs) {
            for (Word modifier : modifiers) {
                for (Word noun : nouns) {
                    System.out.println(verb.command+modifier.command+noun.command + " - " +
                            verb.description + " " + modifier.description + " " + noun.description);
                }
            }
        }
    }


}

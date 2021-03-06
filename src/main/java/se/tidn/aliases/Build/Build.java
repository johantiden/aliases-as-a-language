package se.tidn.aliases.Build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class Build {

    private static final Logger log = LoggerFactory.getLogger(Build.class);
    public static final String GENERATED = "generated";
    public static final String FORMAT_OUT = GENERATED + "/%s";

    private static class Word {
        private final String command;
        private final String description;

        private Word(String command, String description) {
            this.command = command;
            this.description = description;
        }

        public String getCommand() {
            return command;
        }

        public String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) throws IOException {
        Path generatedFolder = new File(GENERATED).toPath();
        Files.list(generatedFolder)
                .forEach(file -> {
                    log.info("deleting {}", file);
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

        List<Verb> verbs = getVerbs();
        List<Noun> allNouns = getNouns();

        for (Verb verb : verbs) {
            buildSolo(verb, verb.getDescription());
        }

        for (Noun noun : allNouns) {
            buildSolo(noun, noun.getDescription());
        }

        for (Verb verb : verbs) {
            for (Noun noun : allNouns) {
                buildOne(verb, noun);
                buildAll(verb, noun);
            }
        }

        List<Sentence> specials = getSpecials();

        for (Sentence special : specials) {
            buildSpecial(special);
        }
    }

    private static List<Sentence> getSpecials() {
        return Arrays.asList(
            Sentence.of("zf", "z1f")
        );
    }

    private static List<Verb> getVerbs() {
        return Arrays.asList(
                Verb.of("", "",
                        Optional.empty(),
                        Optional.of("%s"),
                        Optional.of("%s")),

                //                Word.of("b", "(go)branch"),
//                Word.of("c", "commit"),
//                Word.of("g","get"),
//                Word.of("m","merge"),
                Verb.of("o","open \"${@}\"",
                        Optional.of("__aaal_open \"${@}\""),
                        Optional.of("o \"$(%s)\""),
                        Optional.of("%s | xargs o")),

                rm(),
//                Word.of("v","(set)version"),
//                Word.of("u","up"),
                Verb.of("y","yank",
                        Optional.of("echo \"${@}\" | __aaal_clip_save"),
                        Optional.of("\"$(%s)\" | __aaal_clip_save"),
                        Optional.empty()
                ),
                Verb.of("z","sublime \"${@}\"",
                        Optional.of("subl \"${@}\""),
                        Optional.of("z \"$(%s)\""),
                        Optional.of("%s | xargs z")),
                Verb.of("Z","(sudo)sublime",
                        Optional.of("sudo subl \"${@}\""),
                        Optional.of("z \"$(%s)\""),
                        Optional.of("%s | xargs z"))
        );
    }

    private static Verb rm() {
        final Optional<String> rmImpl = Optional.of(
                  "target=$(%s);\n"
                + "if [[ -z \"$target\" ]]; then\n"
                + "  __debugecho abort\n"
                + "  exit 0;\n"
                + "fi\n"
                + "\n"
                + "if __aaal_ask \"'rm $target', continue?\" N; then\n"
                + "  #rm $target\n"
                + "  echo DRYRUN: rm $target\n"
                + "fi;");
        return Verb.of("r", "remove",
                Optional.empty(),
                rmImpl,
                rmImpl);
    }

    private static ArrayList<Noun> getNouns() {
        List<Noun> nouns = Arrays.asList(
                Noun.of("b", "branch(es)",
                        Optional.of("1b"),
                        Optional.of("git rev-parse --abbrev-ref HEAD"),
                        Optional.empty(),
                        Optional.of("__aaal_get_all_branches")),
//                Word.of("d", "directory~"),
//                Word.of("D", "directory/"),
//                Word.of("d", "develop"),
                Noun.of("f", "file(s)~",
                        Optional.of("1f"),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("__aaal_list_all_files_from_home")),
//                Word.of("F", "file/"),
//                Word.of("m", "master"),
                Noun.of("p", "pasting(s)",
                        Optional.of("__aaal_clip_load"),
                        Optional.of("__aaal_clip_load"),
                        Optional.of("__aaal_clip_load"),
                        Optional.of("__aaal_clip_load")
                ),
                Noun.of("r", "repo(s)",
                        Optional.of("1r"),
                        Optional.of("__looklet_github_repository_name"),
                        Optional.empty(),
                        Optional.of("__aaal_all_repos")
                        )
//                Word.of("v", "version"),
//                Word.of("t", "text-line~"),
//                Word.of("T", "text-line/")
        );

        final List<Noun> autoOneNouns = nouns.stream()
                .filter(noun -> noun.implOne.isEmpty())
                .filter(noun -> noun.implAll.isPresent())
                .map(noun -> Noun.of(
                        noun.getCommand(),
                        "(auto)" + noun.getDescription(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(noun.implAll.get() + " | __aaal_one"),
                        Optional.empty()
                ))
                .collect(Collectors.toList());

        final List<Noun> autoOneExactNouns = nouns.stream()
                .filter(noun -> noun.implAll.isPresent())
                .map(noun -> Noun.of(
                        "e" + noun.getCommand(),
                        "exact (auto)" + noun.getDescription(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(noun.implAll.get() + " | __aaal_one_exact"),
                        Optional.empty()
                ))
                .collect(Collectors.toList());

        final ArrayList<Noun> allNouns = new ArrayList<>(nouns);
        allNouns.addAll(autoOneNouns);
        allNouns.addAll(autoOneExactNouns);
        return allNouns;
    }

    private static void buildAll(Verb verb, Noun noun) {
        String sentenceFlat = flat(verb, "a", noun);
        final String description = verb.getDescription() + " all " + noun.getDescription();

        implAll(verb, noun)
                .ifPresent(impl -> build(description, sentenceFlat, impl));
    }

    private static void buildSpecial(Sentence special) {
        String description = "'" + special.command + "', short for '"+special.impl+"'";
        build(description, special.command, special.impl);
    }

    private static Optional<String> implAll(Verb verb, Noun noun) {
        return verb.implForMany.flatMap(verbForMany ->
                noun.getImplAll()
                        .map(nounForAll -> {
                                String template =
                                    """
                                    lines=$(%s)
                                    count=$(echo "${lines}" | wc -l)
                                    
                                    if __aaal_ask "Count is $count, continue?" Y; then
                                        %s
                                    fi;
                                    """;

                                String inner = String.format(verbForMany, "echo \"${lines}\"");
                                String code = template.formatted(nounForAll, inner);

                                return code;
                            }));
    }

    private static void buildSolo(Verb verb, String description) {
        String sentenceFlat = flat(verb);
        impl(verb)
                .ifPresent(impl -> build(description, sentenceFlat, impl));
    }

    private static void buildSolo(Noun noun, String description) {
        final String sentenceFlat = flat(noun);
        impl(noun)
                .ifPresent(impl -> build(description, sentenceFlat, impl));
    }

    private static Optional<String> impl(Verb verb) {
        return verb.implSolo;
    }

    private static Optional<String> impl(Noun noun) {
        return noun.implSolo;
    }

    private static void buildOne(Verb verb, Noun noun) {
        final String sentenceFlat = flat(verb, "1", noun);
        final String description = verb.getDescription() + " one " + noun.getDescription();
        implOne(verb, noun)
                .ifPresent(impl -> build(description, sentenceFlat, impl));
    }

    private static Optional<String> implOne(Verb verb, Noun noun) {
        return verb.implForOne.flatMap(verbForOne ->
                noun.getImplOne()
                        .map(nounForOne ->
                                String.format(verbForOne, nounForOne)));
    }

    private static void build(String description, String command, String impl) {
        String contents = "#!/usr/bin/env bash" + lineSeparator();
//        contents += "echo $(dirname $0)/../impl.sh";
        contents += lineSeparator();
        contents += "source \"$(dirname $0)/../impl.sh\"";
        contents += lineSeparator();
        contents += "__debugecho \": " + description + "\"" + lineSeparator();
        contents += lineSeparator();
        contents += impl;

//        out.writeUTF(contents);

        try {
            final Path path = new File(String.format(FORMAT_OUT, command)).toPath();
            log.info("writing {}", path);
            Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rwxrwxr-x");
            FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(ownerWritable);
            Files.deleteIfExists(path);
            Files.createFile(path, permissions);
            Files.writeString(path, contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //        out.close();
    }

    private static String flat(Verb verb, String center, Noun noun) {
        return verb.getCommand() + center + noun.getCommand();
    }

    private static String flat(Word word) {
        return word.command;
    }

    private static class Verb extends Word {
        private final Optional<String> implSolo;
        private final Optional<String> implForOne;
        private final Optional<String> implForMany;

        private Verb(String command, String description, Optional<String> implSolo, Optional<String> implForOne, Optional<String> implForMany) {
            super(command, description);
            this.implSolo = implSolo;
            this.implForOne = implForOne;
            this.implForMany = implForMany;
        }

        private static Verb of(String command, String description, Optional<String> implSolo, Optional<String> implForOne, Optional<String> implForMany) {
            return new Verb(command, description, implSolo, implForOne, implForMany);
        }

        public Optional<String> getImplForOne() {
            return implForOne;
        }

        public Optional<String> getImplSolo() {
            return implSolo;
        }

        public Optional<String> getImplForMany() {
            return implForMany;
        }
    }

    private static class Noun extends Word {
        private final Optional<String> implSolo;
        private final Optional<String> implThis;
        private final Optional<String> implOne;
        private final Optional<String> implAll;

        private Noun(String command, String description, Optional<String> implSolo, Optional<String> implThis, Optional<String> implOne, Optional<String> implAll) {
            super(command, description);
            this.implSolo = implSolo;
            this.implThis = implThis;
            this.implOne = implOne;
            this.implAll = implAll;
        }

        private static Noun of(String command, String description, Optional<String> soloImpl, Optional<String> implThis, Optional<String> implOne, Optional<String> implAll) {
            return new Noun(command, description, soloImpl, implThis, implOne, implAll);
        }

        public Optional<String> getImplThis() {
            return implThis;
        }

        public Optional<String> getImplOne() {
            return implOne;
        }

        public Optional<String> getImplAll() {
            return implAll;
        }
    }

    private static class Sentence {
        final String command;
        final String impl;

        private Sentence(String command, String impl) {
            this.command = command;
            this.impl = impl;
        }


        public static Sentence of(String command, String impl) {
            return new Sentence(command, impl);
        }
    }
}

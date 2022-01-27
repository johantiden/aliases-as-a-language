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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class Build {

    private static final Logger log = LoggerFactory.getLogger(Build.class);
    public static final String GENERATED = "generated";
    public static final String FORMAT_OUT = GENERATED + "/%s";

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

        Map<Sentence, Implementation> allSentences = getAllSentences();
        allSentences.forEach(Build::build);
    }

    public static Map<Sentence, Implementation> getAllSentences() {
        return bake(filterOutNones(getAllSentencesRaw()));
    }

    /* test access */ static Map<String, Implementation> getAllSentencesRaw() {
        HashMap<String, Implementation> implementations = new HashMap<>();

        // Standalone verb
        implementations.put("o", Implementation.of("__aaal_open \"${@}\""));
        implementations.put("y", Implementation.of("__aaal_clip_save \"${@}\""));


        putTodo(implementations, "z");

        // No verb
        implementations.put("f", Implementation.of("1f"));
        implementations.put("f.", Implementation.of("1f."));
        implementations.put("1f", Implementation.of("af | 1"));
        implementations.put("af", Implementation.of("__aaal_list_all_files_from_home"));
        implementations.put("af.", Implementation.of("__aaal_list_all_files_from_here"));
        implementations.put("1f.", Implementation.of("af. | 1"));

        implementations.put("b", Implementation.of("__aaal_git_checkout_contextual \"${@}\""));
        implementations.put("1b", Implementation.of("ab | 1"));
        implementations.put("ab", Implementation.of("__aaal_get_all_branches"));
        implementations.put(".b", Implementation.of("__aaal_echo_this_branch"));

        implementations.put("v", Implementation.of(".v")); // TODO: implement setter if argument present
        implementations.put("1v", Implementation.of("av | 1"));
        implementations.put("av", Implementation.of("__aaal_get_all_versions"));
        implementations.put(".v", Implementation.of("__aaal_get_version"));

        implementations.put("1", Implementation.of("__aaal_fzf"));

        implementations.put("1r", Implementation.of("ar | 1"));
        implementations.put("ar", Implementation.of("__aaal_all_repos"));
        implementations.put(".r", Implementation.of("__aaal_this_repo"));

        implementations.put("p", Implementation.of("__aaal_clip_load"));
        implementations.put("ap", Implementation.of("p"));
        implementations.put("1p", Implementation.of("p | 1"));
        implementations.put("op", Implementation.of("o `p`"));

        putTodo(implementations, "ad");
        putTodo(implementations, "1d");
        implementations.put(".d", Implementation.of("pwd"));
        putTodo(implementations, "1d");
        implementations.put("d.", Implementation.of("1d."));
        putTodo(implementations, "1d.");
        putTodo(implementations, "ad.");

        implementations.put("t", Implementation.of("1t"));
        implementations.put("1t", Implementation.of("at | 1"));
        implementations.put("at", Implementation.of("ag --nobreak --noheading . ~"));
        implementations.put("t.", Implementation.of("1t."));
        implementations.put("at.", Implementation.of("ag --nobreak --noheading . ."));
        implementations.put("1t.", Implementation.of("at. | 1"));
        implementations.put("zt", Implementation.of("z1t"));
        implementations.put("zt.", Implementation.of("z1t."));
        putTodo(implementations, "z1t");
        putTodo(implementations, "z1t.");

        // Open
        implementations.put("of", Implementation.of("o1f"));
        implementations.put("of.", Implementation.of("o1f."));
        implementations.put("oaf.", Implementation.of("o `af.`"));
        implementations.put("o1f", Implementation.of("o `1f`"));
        implementations.put("o1f.", Implementation.of("o `1f.`"));
        implementations.put("ob", Implementation.of("gob"));
        implementations.put("oab", Implementation.of("goab"));
        implementations.put("o1b", Implementation.of("go1b"));
        implementations.put("o.b", Implementation.of("go.b"));
        implementations.put("oap", Implementation.of("p | xargs -n1 o"));
        implementations.put("o1p", Implementation.of("o `1p`"));

        implementations.put("ov", Implementation.of("gov"));
        implementations.put("oav", Implementation.of("goav"));
        implementations.put("o1v", Implementation.of("go1v"));
        implementations.put("o.v", Implementation.of("go.v"));

        implementations.put("or", Implementation.of("gor"));
        implementations.put("oar", Implementation.of("goar"));
        implementations.put("o1r", Implementation.of("go1r"));
        implementations.put("o.r", Implementation.of("go.r"));

        implementations.put("od", Implementation.of("o.d"));
        implementations.put("o1d", Implementation.of("o `1d`"));
        implementations.put("o.d", Implementation.of("o `.d`"));
        implementations.put("od.", Implementation.of("o `d.`"));
        implementations.put("o1d.", Implementation.of("o `1d.`"));
        implementations.put("oad.", Implementation.of("o `ad.`"));

        implementations.put("ot", Implementation.of("o1t"));
        implementations.put("o1t", Implementation.of("__aaal_o1t"));
        implementations.put("ot.", Implementation.of("o1t."));
        implementations.put("o1t.", Implementation.of("__aaal_o1t."));

        // Remove
        implementations.put("rf", Implementation.of("r1f"));
        implementations.put("rf.", Implementation.of("r1f."));
        putTodo(implementations, "rb"); // remove branch with parameter
        putTodo(implementations, "r1p"); // try to guess what the pasting contains
        putTodo(implementations, "rp"); // try to guess what the pasting contains
        putTodo(implementations, "rv");
        putTodo(implementations, "r1v");
        putTodo(implementations, "r.v");
        implementations.put("r1f", Implementation.of("rm `1f`"));
        implementations.put("r1f.", Implementation.of("rm `1f.`"));
        putTodo(implementations, "r1b");
        putTodo(implementations, "r.b");
        implementations.put("rd", Implementation.of("r1d"));
        implementations.put("r1d", Implementation.of("rm `1d`"));
        putTodo(implementations, "r.d");
        implementations.put("rd.", Implementation.of("r1d."));
        implementations.put("r1d.", Implementation.of("rm `1d.`"));

        putTodo(implementations, "rt");
        putTodo(implementations, "rt.");
        putTodo(implementations, "r1t");
        putTodo(implementations, "r1t.");

        // Yank
        implementations.put("yf", Implementation.of("y1f"));
        implementations.put("yf.", Implementation.of("y1f."));
        implementations.put("yaf.", Implementation.of("af. | y"));
        implementations.put("yv", Implementation.of("y.v"));
        implementations.put("yav", Implementation.of("av | y"));
        implementations.put("y1f", Implementation.of("1f | y"));
        implementations.put("y1f.", Implementation.of("1f. | y"));
        implementations.put("y1b", Implementation.of("1b | y"));
        implementations.put("y1v", Implementation.of("1v | y"));
        implementations.put("y.v", Implementation.of(".v | y"));
        implementations.put("yb", Implementation.of("y.b"));
        implementations.put("yab", Implementation.of("ab | y"));
        implementations.put("y.b", Implementation.of(".b | y"));
        implementations.put("y1p", Implementation.of("p | 1 | y"));

        implementations.put("yr", Implementation.of("y.r"));
        implementations.put("yar", Implementation.of("ar | y"));
        implementations.put("y1r", Implementation.of("1r | y"));
        implementations.put("y.r", Implementation.of(".r | y"));
        implementations.put("yd", Implementation.of("1d | y"));
        implementations.put("yad", Implementation.of("ad | y"));
        implementations.put("y1d", Implementation.of("1d | y"));
        implementations.put("y.d", Implementation.of(".d | y"));
        implementations.put("yd.", Implementation.of("y1d."));
        implementations.put("y1d.", Implementation.of("1d. | y"));
        implementations.put("yad.", Implementation.of("ad. | y"));

        implementations.put("yt", Implementation.of("t | y"));
        implementations.put("y1t", Implementation.of("1t | y"));
        implementations.put("yt.", Implementation.of("t. | y"));
        implementations.put("yat.", Implementation.of("at. | y"));
        implementations.put("y1t.", Implementation.of("1t. | y"));

        // z edit
        implementations.put("zf", Implementation.of("z1f"));
        implementations.put("zf.", Implementation.of("z1f."));
        implementations.put("z1f", Implementation.of("z `1f`"));
        implementations.put("z1f.", Implementation.of("z `1f.`"));
        putTodo(implementations, "zb"); // rename branch maybe?
        putTodo(implementations, "z.b"); // rename branch maybe?
        putTodo(implementations, "z1b"); // rename branch maybe?

        putTodo(implementations, "zr"); // repo settings on github
        putTodo(implementations, "z1r"); // repo settings on github
        putTodo(implementations, "z.r"); // repo settings on github

        putTodo(implementations, "zp"); // TODO: create tempfile, edit, yank
        putTodo(implementations, "z1p"); // TODO: create tempfile, edit, yank

        implementations.put("zd", Implementation.of("z.d"));
        implementations.put("z1d", Implementation.of("z `1d`"));
        implementations.put("z.d", Implementation.of("z `.d`"));
        implementations.put("zd.", Implementation.of("z `d.`"));
        implementations.put("z1d.", Implementation.of("z `1d.`"));

        // These don't make sense on their own.
        implementations.put("a", Implementation.NONE);
        implementations.put(".", Implementation.NONE);

        // These make sense but are blocked for other reasons (e.g. too dangerous)
        implementations.put("d", Implementation.NONE);
        implementations.put("oaf", Implementation.NONE);
        implementations.put("yaf", Implementation.NONE);
        implementations.put("rr", Implementation.NONE);
        implementations.put("raf", Implementation.NONE);
        implementations.put("rab", Implementation.NONE);
        implementations.put("rap", Implementation.NONE);
        implementations.put("rar", Implementation.NONE);
        implementations.put("rav", Implementation.NONE);
        implementations.put("r1r", Implementation.NONE);
        implementations.put("r.r", Implementation.NONE);
        implementations.put("raf.", Implementation.NONE);
        implementations.put("oad", Implementation.NONE);
        implementations.put("rad", Implementation.NONE);
        implementations.put("rad.", Implementation.NONE);
        implementations.put("oat", Implementation.NONE);
        implementations.put("oat.", Implementation.NONE);
        implementations.put("rat", Implementation.NONE);
        implementations.put("rat.", Implementation.NONE);
        implementations.put("yat", Implementation.NONE);
        return implementations;
    }

    private static Map<String, Implementation> filterOutNones(Map<String, Implementation> implementations) {
        return implementations.entrySet().stream()
                .filter(e -> e.getValue() != Implementation.NONE)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static void putTodo(HashMap<String, Implementation> implementations, String command) {
        implementations.put(command, Implementation.TODO(command));
    }

    /* test access */ static Map<Sentence, Implementation> bake(Map<String, Implementation> implementations) {
        List<Sentence> allPossibleSentences = Language.listAllPossibleSentences();

        return implementations.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> findOne(allPossibleSentences, e.getKey()),
                        Map.Entry::getValue
                ));
    }

    private static Sentence findOne(List<Sentence> list, String command) {
        return list.stream()
                .filter(sentence -> sentence.command.equals(command))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Could not find '"+command+"' among all sentences. Remove it from Build.getAllSentences"));
    }

    private static void build(Sentence sentence, Implementation implementation) {
        String contents = getFullFileContents(sentence.description, implementation.getBashCode());

        try {
            final Path path = new File(String.format(FORMAT_OUT, sentence.command)).toPath();
            log.info("writing {}", path);
            Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rwxrwxr-x");
            FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(ownerWritable);
            Files.deleteIfExists(path);
            Files.createFile(path, permissions);
            Files.writeString(path, contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFullFileContents(String description, String impl) {
        String contents = "#!/usr/bin/env bash" + lineSeparator();
        contents += lineSeparator();
        contents += "source \"$(dirname $0)/../impl.sh\"";
        contents += lineSeparator();
        contents += "__debugecho \": " + description + "\"" + lineSeparator();
        contents += lineSeparator();
        contents += impl;
        contents += lineSeparator();
        return contents;
    }
}

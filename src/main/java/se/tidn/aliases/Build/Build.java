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
        putTodo(implementations, "o");
        putTodo(implementations, "y");
        putTodo(implementations, "z");
        putTodo(implementations, "x");
        putTodo(implementations, "go"); // open stuff on github
        putTodo(implementations, "gz"); // open github editor?
        putTodo(implementations, "gx"); // open github editor?

        // No verb
        implementations.put("f", Implementation.of("1f"));
        implementations.put("ef", Implementation.of("af | e1"));
        implementations.put("1f", Implementation.of("af | 1"));
        implementations.put("af", Implementation.of("__aaal_list_all_files_from_home"));

        implementations.put("b", Implementation.of("cd `1b`"));
        implementations.put("eb", Implementation.of("ab | e1"));
        implementations.put("1b", Implementation.of("ab | 1"));
        implementations.put("ab", Implementation.of("__aaal_get_all_branches"));
        implementations.put(".b", Implementation.of("__aaal_echo_this_branch"));

        implementations.put("v", Implementation.of(".v")); // TODO: implement setter if argument present
        implementations.put("ev", Implementation.of("av | e1"));
        implementations.put("1v", Implementation.of("av | 1"));
        putTodo(implementations, "av"); // list all tags
        putTodo(implementations, ".v");

        implementations.put("1", Implementation.of("__aaal_fzf"));

        implementations.put("r", Implementation.NONE);
        implementations.put("er", Implementation.of("ar | e1"));
        implementations.put("1r", Implementation.of("ar | 1"));
        implementations.put("ar", Implementation.of("__aaal_all_repos"));
        implementations.put(".r", Implementation.of("__looklet_github_repository_name"));

        // Open
        implementations.put("of", Implementation.of("o1f"));
        implementations.put("o1f", Implementation.of("o `1f`"));
        implementations.put("oef", Implementation.of("o `ef`"));
        implementations.put("gof", Implementation.of("go1f"));
        implementations.put("go1f", Implementation.of("1f | go"));
        implementations.put("goef", Implementation.of("ef | go"));
        implementations.put("ob", Implementation.of("gob"));
        implementations.put("oab", Implementation.of("goab"));
        implementations.put("oeb", Implementation.of("goeb"));
        implementations.put("o1b", Implementation.of("go1b"));
        implementations.put("gob", Implementation.of("go.b"));
        implementations.put("o.b", Implementation.of("go.b"));
        implementations.put("goab", Implementation.of("ab | go"));
        implementations.put("goeb", Implementation.of("ab | e1 | go"));
        implementations.put("go1b", Implementation.of("ab | 1 | go"));
        implementations.put("go.b", Implementation.of(".b | go"));

        implementations.put("ov", Implementation.of("gov"));
        implementations.put("gov", Implementation.of(".v | go"));
        implementations.put("oav", Implementation.of("goav"));
        implementations.put("goav", Implementation.of("av | go"));
        implementations.put("oev", Implementation.of("goev"));
        implementations.put("o1v", Implementation.of("go1v"));
        implementations.put("go1v", Implementation.of("1v | go"));
        implementations.put("o.v", Implementation.of("go.v"));
        implementations.put("go.v", Implementation.of(".v | go"));

        implementations.put("or", Implementation.of("gor"));
        implementations.put("oar", Implementation.of("goar"));
        implementations.put("oer", Implementation.of("goer"));
        implementations.put("o1r", Implementation.of("go1r"));
        implementations.put("o.r", Implementation.of("go.r"));

        // Yank
        implementations.put("yf", Implementation.of("y1f"));
        implementations.put("yv", Implementation.of("y.v"));
        implementations.put("yav", Implementation.of("av | y"));
        implementations.put("yef", Implementation.of("ef | y"));
        implementations.put("yeb", Implementation.of("eb | y"));
        implementations.put("yev", Implementation.of("ev | y"));
        implementations.put("y1f", Implementation.of("1f | y"));
        implementations.put("y1b", Implementation.of("1b | y"));
        implementations.put("y1v", Implementation.of("1v | y"));
        implementations.put("y.v", Implementation.of(".v | y"));
        implementations.put("yb", Implementation.of("y.b"));
        implementations.put("gyb", Implementation.of("g.b | y"));
        implementations.put("yab", Implementation.of("ab | y"));
        implementations.put("y.b", Implementation.of(".b | y"));

        implementations.put("gor", Implementation.of("go.r"));
        implementations.put("gyr", Implementation.of("gy.r"));
        putTodo(implementations, "gyf"); // TODO: github url
        putTodo(implementations, "gyv"); // TODO: github url
        putTodo(implementations, "gyab"); // TODO: github url
        putTodo(implementations, "gy.b"); // TODO: github url
        putTodo(implementations, "gyef"); // TODO: github url
        putTodo(implementations, "gy1f"); // TODO: github url
        putTodo(implementations, "gyeb"); // TODO: github url
        putTodo(implementations, "gy1b"); // TODO: github url
        putTodo(implementations, "gyev"); // TODO: github url
        putTodo(implementations, "gy1v"); // TODO: github url
        putTodo(implementations, "gy.v"); // TODO: github url
        putTodo(implementations, "goar"); // TODO: github url
        putTodo(implementations, "goer"); // TODO: github url
        putTodo(implementations, "go1r"); // TODO: github url
        putTodo(implementations, "go.r"); // TODO: github url
        putTodo(implementations, "gyar"); // TODO: github url
        putTodo(implementations, "gyer"); // TODO: github url
        putTodo(implementations, "gy1r"); // TODO: github url
        putTodo(implementations, "gy.r"); // TODO: github url
        putTodo(implementations, "gzr"); // TODO: github url
        putTodo(implementations, "gzer"); // TODO: github url
        putTodo(implementations, "gz1r"); // TODO: github url
        putTodo(implementations, "gz.r"); // TODO: github url
        putTodo(implementations, "gxr"); // TODO: github url
        putTodo(implementations, "gxer"); // TODO: github url
        putTodo(implementations, "gx1r"); // TODO: github url
        putTodo(implementations, "gx.r"); // TODO: github url

        implementations.put("yr", Implementation.of("y.r"));
        implementations.put("yar", Implementation.of("ar | y"));
        implementations.put("yer", Implementation.of("er | y"));
        implementations.put("y1r", Implementation.of("1r | y"));
        implementations.put("y.r", Implementation.of(".r | y"));

        // z edit
        implementations.put("zf", Implementation.of("z1f"));
        implementations.put("zef", Implementation.of("z `ef`"));
        implementations.put("z1f", Implementation.of("z `1f`"));
        putTodo(implementations, "zb"); // rename branch maybe?
        putTodo(implementations, "z.b"); // rename branch maybe?
        putTodo(implementations, "zeb"); // rename branch maybe?
        putTodo(implementations, "z1b"); // rename branch maybe?
        putTodo(implementations, "gzf"); // edit file on github
        putTodo(implementations, "gzef"); // edit file on github
        putTodo(implementations, "gz1f"); // edit file on github
        putTodo(implementations, "gxf"); // edit file on github
        putTodo(implementations, "gxef"); // edit file on github
        putTodo(implementations, "gx1f"); // edit file on github

        putTodo(implementations, "zr"); // repo settings on github
        putTodo(implementations, "zer"); // repo settings on github
        putTodo(implementations, "z1r"); // repo settings on github
        putTodo(implementations, "z.r"); // repo settings on github

        // x edit
        implementations.put("xf", Implementation.of("x1f"));
        implementations.put("xef", Implementation.of("x `ef`"));
        implementations.put("x1f", Implementation.of("x `1f`"));
        putTodo(implementations, "xb"); // rename branch maybe?
        putTodo(implementations, "x.b"); // rename branch maybe?
        putTodo(implementations, "xeb"); // rename branch maybe?
        putTodo(implementations, "x1b"); // rename branch maybe?

        putTodo(implementations, "xr"); // repo settings on github
        putTodo(implementations, "xer"); // repo settings on github
        putTodo(implementations, "x1r"); // repo settings on github
        putTodo(implementations, "x.r"); // repo settings on github

        // These don't make sense on their own.
        implementations.put("goev", Implementation.NONE);
        implementations.put("gy", Implementation.NONE);
        implementations.put("a", Implementation.NONE);
        implementations.put("e", Implementation.NONE);
        implementations.put(".", Implementation.NONE);
        //        implementations.put("gf", Implementation.NONE);

        // These make sense but are blocked for other reasons (e.g. too dangerous)
        implementations.put("oaf", Implementation.NONE);
        implementations.put("goaf", Implementation.NONE);
        implementations.put("gyaf", Implementation.NONE);
        implementations.put("gyav", Implementation.NONE);
        implementations.put("yaf", Implementation.NONE);
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

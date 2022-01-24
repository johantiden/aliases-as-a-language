package se.tidn.aliases.Build;

interface Implementation {
    Implementation NONE = new Implementation() {
        @Override
        public String getBashCode() {
            throw new IllegalStateException("Dont implement this! It must be ignored");
        }
    };

    static Implementation of(String bashCode) {
        return () -> bashCode;
    }

    static Implementation TODO(String command) {
        return Implementation.of("echo \"TODO: implement command '"+command+"' in aliases-as-a-language");
    }

    String getBashCode();
}

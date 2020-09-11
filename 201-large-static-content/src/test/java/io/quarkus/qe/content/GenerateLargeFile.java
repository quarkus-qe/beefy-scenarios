package io.quarkus.qe.content;

import java.io.RandomAccessFile;

public class GenerateLargeFile {
    public static void main(String args[]) throws Exception {
        // args[0] is expected to be something like /Users/rsvoboda/git/beefy-scenarios/201-large-static-content/target/classes

        try (RandomAccessFile f = new RandomAccessFile(args[0] + "/META-INF/resources/big-file", "rw");) {
            f.setLength(1024 * 1024 * 512); // avoid https://github.com/quarkusio/quarkus/issues/2917
        }
    }
}

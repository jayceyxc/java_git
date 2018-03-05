package com.linus.tools;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.Collection;

public class RobertAhoCorasickTest {
    public static void main (String[] args) {
        Trie trie = Trie.builder ().addKeyword ("hot").addKeyword ("choco").addKeyword ("hello").build ();
        Collection<Emit> emits = trie.parseText("hot chocolate");
        for (Emit emit : emits) {
            System.out.println (emit.getKeyword ());
        }
    }
}

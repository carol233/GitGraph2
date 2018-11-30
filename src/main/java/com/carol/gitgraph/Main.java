package com.carol.gitgraph;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Created by Carol on 2018/11/27.
 */

public class Main {
    public static void main(String[] args){
        ArgumentParser parser = ArgumentParsers.newFor("Main").build()
                .defaultHelp(true)
                .description("Analyse a com.carol.gitgraph.git project and build visible graph");

        parser.addArgument("-p", "--project")
                .help("com.carol.gitgraph.git project root directory");
        parser.addArgument("-f", "--filter")
                .help("type filter switch for source file");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        String project = ns.getString("project");
        String filter = ns.getString("filter");
        System.out.println("Starting...");
        new Analyser(project, filter).run();
        System.out.println("Done.");
    }
}

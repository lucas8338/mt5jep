package mt5jep;

import rife.bld.Project;

import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

public class Mt5jepBuild extends Project {
    public Mt5jepBuild() {
        pkg = "mt5jep";
        name = "Mt5jep";
        version = version(2, 0, 0, "0");

        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);
        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,3)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,3)));
    }

    public static void main(String[] args) {
        new Mt5jepBuild().start(args);
    }
}
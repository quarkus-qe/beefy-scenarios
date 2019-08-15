# Quarkus - all extensions in one app

## Import all extensions
Similar process to this:
```
mvn io.quarkus:quarkus-maven-plugin:0.20.0:create \
    -DprojectGroupId=org.acme \
    -DprojectArtifactId=getting-started \
    -DclassName="org.acme.quickstart.GreetingResource" \
    -Dpath="/hello"

mvn io.quarkus:quarkus-maven-plugin:0.20.0:list-extensions > ext-0.20.0
cat ext-0.20.0 | grep -v 'INFO' | grep 'quarkus-' | sed 's/.*quarkus-/quarkus-/g' | sed 's/ //g' > extensions-0.20.0
mvn quarkus:add-extensions -Dextensions=`cat extensions-0.20.0 | tr '\n' ',' | sed 's/,$//g'`
```

## Issues in JVM and Native mode
Several Issues were met in JVM and Native mode. Some extensions need initial configuration to build successfully in JVM mode,
some extension need some code and configuration to build successfully in Native mode.
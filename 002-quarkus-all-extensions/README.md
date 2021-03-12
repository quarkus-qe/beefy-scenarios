# Quarkus - all extensions in one app

## Import all extensions
Similar process to this:
```
mvn io.quarkus:quarkus-maven-plugin:1.8.0.Final:create \
    -DprojectGroupId=org.acme \
    -DprojectArtifactId=getting-started-17 \
    -DplatformVersion=1.7.3.Final \
    -DclassName="org.acme.getting.started.GreetingResource" \
    -Dpath="/hello"

mvn io.quarkus:quarkus-maven-plugin:1.8.0.Final:create \
    -DprojectGroupId=org.acme \
    -DprojectArtifactId=getting-started-18 \
    -DclassName="org.acme.getting.started.GreetingResource" \
    -Dpath="/hello"

mvn -f getting-started-17/pom.xml quarkus:list-extensions > ext-17
mvn -f getting-started-18/pom.xml quarkus:list-extensions > ext-18

cat ext-17 | grep -v 'INFO' | grep -v camel  | grep -v kogito | grep -v optaplanner | grep 'quarkus-' | sed 's/.*quarkus-/quarkus-/g' | sed 's/ //g' | sort > extensions-17
cat ext-18 | grep -v 'INFO' | grep -v camel  | grep -v kogito | grep -v optaplanner | grep 'quarkus-' | sed 's/.*quarkus-/quarkus-/g' | sed 's/ //g' | sort > extensions-18

code --diff  extensions-17 extensions-18

mvn quarkus:add-extensions -Dextensions=`cat extensions-1.7.1 | tr '\n' ',' | sed 's/,$//g'`
```

## Issues in JVM and Native mode
Several Issues were met in JVM and Native mode. Some extensions need initial configuration to build successfully in JVM mode,
some extension need some code and configuration to build successfully in Native mode.

Note that this module requires more than 12GB of RAM.

The REST endpoints are not exposed when running the application in Native. This is preventing the Native test to work and it has been disabled.
# Quarkus - all extensions in one app

## Import all extensions
Similar process to this:
```
mvn io.quarkus:quarkus-maven-plugin:0.20.0:create \
    -DprojectGroupId=org.acme \
    -DprojectArtifactId=getting-started \
    -DclassName="org.acme.quickstart.GreetingResource" \
    -Dpath="/hello"

mvn quarkus:list-extensions > ext-0.20.0

cat ext-1.3.4 | grep -v 'INFO' | grep -v camel  | grep -v kogito | grep -v optaplanner | grep 'quarkus-' | sed 's/.*quarkus-/quarkus-/g' | sed 's/ //g' | sort > extensions-1.3.4
cat ext-1.7.1 | grep -v 'INFO' | grep -v camel  | grep -v kogito | grep -v optaplanner | grep 'quarkus-' | sed 's/.*quarkus-/quarkus-/g' | sed 's/ //g' | sort > extensions-1.7.1

code --diff  extensions-1.3.4 extensions-1.7.1

mvn quarkus:add-extensions -Dextensions=`cat extensions-1.7.1 | tr '\n' ',' | sed 's/,$//g'`
```

## Issues in JVM and Native mode
Several Issues were met in JVM and Native mode. Some extensions need initial configuration to build successfully in JVM mode,
some extension need some code and configuration to build successfully in Native mode.
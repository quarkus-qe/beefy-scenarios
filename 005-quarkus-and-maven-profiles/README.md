# Quarkus and Maven profiles
Ensure maven profile for the project has been activated.  
The trick with:
```
<property>
   <name>!no-profile</name>
</property>
```
do activate the profile without a need of property or profile to be specified during the build.

More about maven profiles can be found at https://www.baeldung.com/maven-profiles
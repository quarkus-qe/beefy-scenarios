# Quarkus - OpenShift extension ([Moved to 020-quarkus-all-extensions](../020-quarkus-http-non-application-endpoints))
By adding *quarkus-openshift* extension, maven build creates */kubernetes* directory under the */target*
which includes *.yml* files for deployment

### Test goal
The goal of the test is to ensure that *kubernetes.yml* file has been created
under mentioned */kubernetes* directory and parse that file to see required fields are present.


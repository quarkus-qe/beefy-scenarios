# Quarkus - OpenShift extension
By adding *quarkus-openshift* extension, maven build creates */kubernetes* directory under the */target*
which includes *.yml* files for deployment

### Test goal
The goal of the test is to ensure that *kubernetes.yml* file has been created
under mentioned */kubernetes* directory and parse that file to see required fields are present.
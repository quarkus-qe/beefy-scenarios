# Quarkus - Bootstrap project 
Simple bootstrap project created by *quarkus-maven-plugin*  

### Additions
* *@Deprecated* annotation has been added for test regression purposes to ensure `java.lang` annotations are allowed for resources
* Resource with multipart body support, provided parts are text, image and binary data, charset checked with `us-ascii` and `utf-8`
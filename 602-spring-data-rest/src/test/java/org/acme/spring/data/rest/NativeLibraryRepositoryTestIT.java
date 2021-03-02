package org.acme.spring.data.rest;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@Disabled("TODO: Caused by https://github.com/quarkusio/quarkus/issues/15409")
@NativeImageTest
public class NativeLibraryRepositoryTestIT extends LibraryRepositoryTest{

}

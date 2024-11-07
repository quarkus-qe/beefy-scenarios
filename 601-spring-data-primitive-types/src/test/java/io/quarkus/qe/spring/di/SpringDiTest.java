package io.quarkus.qe.spring.di;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableBean;
import io.quarkus.test.common.TestResourceScope;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.enterprise.inject.spi.CDI;

@QuarkusTest
@WithTestResource(value = H2DatabaseTestResource.class, scope = TestResourceScope.MATCHING_RESOURCES)
public class SpringDiTest {

    private static Stream<Class<?>> beanClassProvider() {
        return Stream.of(
                Account.class,
                AccountService.class,
                AudioBookService.class,
                BookService.class,
                PersonDao.class,
                SpringPersonService.class,
                UserService.class);
    }

    private static Stream<Arguments> beanNameAndClassProvider() {
        return Stream.of(
                Arguments.of("account", Account.class),
                Arguments.of("accountServiceImpl", AccountService.class),
                Arguments.of("audioBookServiceGenerator", AudioBookService.class),
                Arguments.of("bookServiceGenerator", BookService.class),
                Arguments.of("personDaoImpl", PersonDao.class),
                Arguments.of("springPersonService", SpringPersonService.class),
                Arguments.of("userService", UserService.class));
    }

    @ParameterizedTest
    @MethodSource("beanClassProvider")
    public void beanExists(Class<?> beanClass) {
        final Object bean = CDI.current().select(beanClass).get();
        assertNotNull(bean);
    }

    @Test
    public void givenAccountServiceAutowiredToUserService_WhenGetAccountServiceInvoked_ThenReturnValueIsNotNull() {
        UserService userService = CDI.current().select(UserService.class).get();
        assertNotNull(userService.getAccountService());
    }

    @Test
    public void givenPersonDaoAutowiredToSpringPersonServiceBySetterInjection_WhenSpringPersonServiceRetrievedFromContext_ThenPersonDaoInitializedByTheSetter() {
        SpringPersonService personService = CDI.current().select(SpringPersonService.class).get();
        assertNotNull(personService);
        assertNotNull(personService.getPersonDao());
    }

    @ParameterizedTest
    @MethodSource("beanClassProvider")
    public void cdiAndArcWayReturnTheSameBean(Class<?> beanClass) {
        Object bean1 = CDI.current().select(beanClass).get();
        Object bean2 = Arc.container().select(beanClass).get();
        assertNotNull(bean1);
        assertNotNull(bean2);
        assertEquals(bean1, bean2);
    }

    @ParameterizedTest
    @MethodSource("beanClassProvider")
    public void cdiAndArcInstanceWayReturnTheSameBean(Class<?> beanClass) {
        Object bean1 = CDI.current().select(beanClass).get();
        Object bean2 = Arc.container().instance(beanClass).get();
        assertNotNull(bean1);
        assertNotNull(bean2);
        assertEquals(bean1, bean2);
    }

    @ParameterizedTest
    @MethodSource("beanNameAndClassProvider")
    public void cdiAndArcStringWayReturnTheSameBean(String beanName, Class<?> beanClass) {
        Object bean1 = CDI.current().select(beanClass).get();
        Object bean2 = Arc.container().instance(beanName).get();
        assertNotNull(bean1);
        assertNotNull(bean2);
        assertEquals(bean1, bean2);
    }

    @Test
    public void beanDeclaringClassMatch() {
        final InjectableBean<Object> audioBookServiceGenerator = Arc.container().instance("audioBookServiceGenerator")
                .getBean();
        final InjectableBean<Object> bookServiceGenerator = Arc.container().instance("bookServiceGenerator").getBean();
        assertNotNull(audioBookServiceGenerator);
        assertNotNull(bookServiceGenerator);
        assertEquals(SpringBeansConfig.class, audioBookServiceGenerator.getBeanClass());
        assertEquals(SpringMainConfig.class, bookServiceGenerator.getBeanClass());
    }
}

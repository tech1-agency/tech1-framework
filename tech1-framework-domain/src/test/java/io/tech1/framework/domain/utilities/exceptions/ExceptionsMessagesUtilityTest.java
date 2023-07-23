package io.tech1.framework.domain.utilities.exceptions;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsMessagesUtilityTest {

    @Test
    void contactDevelopmentTeamTest() {
        // Arrange
        var prefix = randomString();

        // Act
        var actual = contactDevelopmentTeam(prefix);

        // Assert
        assertThat(actual).isEqualTo(prefix + ". Please contact development team");
    }

    @Test
    void attributeNameTest() {
        // Arrange
        var attributeName = randomString();

        // Act
        var actual = invalidAttribute(attributeName);

        // Assert
        assertThat(actual).isEqualTo("Attribute `" + attributeName + "` is invalid");
    }

    @Test
    void entityNotFoundShortTest() {
        // Arrange
        var entity = randomString();

        // Act
        var actual = entityNotFoundShort(entity);

        // Assert
        assertThat(actual).isEqualTo(entity + " is not found");
    }

    @Test
    void entityNotFoundTest() {
        // Arrange
        var entity = randomString();
        var value = randomString();

        // Act
        var actual = entityNotFound(entity, value);

        // Assert
        assertThat(actual).isEqualTo(entity + " is not found. Value: `" + value + "`");
    }

    @Test
    void entityAlreadyUsedTest() {
        // Arrange
        var entity = randomString();

        // Act
        var actual = entityAlreadyUsed(entity);

        // Assert
        assertThat(actual).isEqualTo(entity + " is already used");
    }

    @Test
    void accessDeniedTest() {
        // Arrange
        var username = randomUsername();
        var entity = randomString();
        var value = randomString();

        // Act
        var actual = accessDenied(username, entity, value);

        // Assert
        assertThat(actual).isEqualTo("Access denied. Username: `" + username + "`, Entity: `" + entity + "`. Value: `" + value + "`");
    }

    @Test
    void parametrizedTestCaseTest() {
        // Arrange
        var sourceObj = randomString();
        var actualObj = randomString();
        var expectedObj = randomString();

        // Act
        var actual = parametrizedTestCase(sourceObj, actualObj, expectedObj);

        // Assert
        assertThat(actual).isEqualTo("Execute parametrized test case. Source: `" + sourceObj + "`. Actual: `" + actualObj + "`. Expected: `" + expectedObj + "`");
    }
}

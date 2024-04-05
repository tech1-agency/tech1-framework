package io.tech1.framework.domain.utilities.exceptions;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsMessagesUtilityTest {

    @Test
    void pleaseWaitTest() {
        // Arrange
        var prefix = randomString();

        // Act
        var actual = pleaseWait(prefix);

        // Assert
        assertThat(actual).isEqualTo(prefix + ". Please wait...");
    }

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
    void notImplementedYetTest() {
        // Act
        var actual = notImplementedYet();

        // Assert
        assertThat(actual).isEqualTo("Not implemented yet. Please contact development team");
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
    void entityNotFoundTest() {
        // Arrange
        var entity = randomString();
        var entityId = randomString();

        // Act
        var actual = entityNotFound(entity, entityId);

        // Assert
        assertThat(actual).isEqualTo(entity + " is not found. Id: " + entityId);
    }

    @Test
    void entityAlreadyUsedTest() {
        // Arrange
        var entity = randomString();
        var entityId = randomString();

        // Act
        var actual = entityAlreadyUsed(entity, entityId);

        // Assert
        assertThat(actual).isEqualTo(entity + " is already used. Id: " + entityId);
    }

    @Test
    void entityAccessDeniedTest() {
        // Arrange
        var entity = randomString();
        var entityId = randomString();

        // Act
        var actual = entityAccessDenied(entity, entityId);

        // Assert
        assertThat(actual).isEqualTo("Access denied on " + entity.toLowerCase() + ". Id: " + entityId);
    }
}

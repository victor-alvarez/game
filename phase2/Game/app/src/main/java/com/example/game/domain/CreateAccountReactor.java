package com.example.game.domain;

/**
 * Interface for reactions to the results of the Use Cases.
 */
public interface CreateAccountReactor {
    void accountCreationFailed();

    void accountCreationSuccess();
}
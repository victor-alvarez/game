package com.example.game.viewLevel;

import android.util.Log;

import com.example.game.models.Account;
import com.example.game.models.Interfaces.AccountDataRepositoryInterface;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * This is the class which reads from and writes to the database.
 */
class AccountDataRepository implements AccountDataRepositoryInterface {
    /**
     * The name of the save file.
     */
    private static final String FILE_NAME = "save.txt";

    /**
     * Saves account data for account.
     * @param contextFile the file where the database is
     * @param account the Account whose data is to be saved
     */
    public void save(File contextFile, Account account){
        try {
            Gson gson = new Gson();
            String accountString = gson.toJson(account);

            File saveFile = new File(contextFile, FILE_NAME);
            FileReader fileReader = new FileReader(saveFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(account.getLogin())) {
                    line = accountString;
                }
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            fileReader.close();
            bufferedReader.close();
            FileWriter fileOut = new FileWriter(saveFile);
            fileOut.write(stringBuilder.toString());
            fileOut.close();
            Log.v(TAG, "lol" + stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens and returns an existing account if it exists.
     * @param login the login/username for the account
     * @param contextFile the file in which the database is located
     * @return the account with login as their username if found in the file, or null if not found
     */
    public Account openExistingAccount(String login, File contextFile){
        try {
            Gson gson = new Gson();
            File saveFile = new File(contextFile, FILE_NAME);
            FileReader loadAccountData = new FileReader(saveFile);
            BufferedReader loadAccData = new BufferedReader(loadAccountData);
            String line;
            while ((line = loadAccData.readLine()) != null) {
                if (line.contains(login)) {
                    Account acc = gson.fromJson(line, Account.class);
                    loadAccData.close();
                    return acc;
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
            System.out.println("Can't find account");
        }
        return null;
    }

    /**
     * Creates a new account in the database.
     * @param login the username/login of the new account to be stored
     * @param contextFile the file in which the database is located
     */
    public void createNewAccount(String login, File contextFile) {
        try {
            Gson gson = new Gson();
            Account account = new Account(login);
            File saveFile = new File(contextFile, FILE_NAME);
            FileWriter fileWriter = new FileWriter(saveFile, true);
            fileWriter.write(gson.toJson(account));
            fileWriter.write("\n");
            fileWriter.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     * Deletes the file in which the account data is located. It deleted all accounts.
     * @param contextFile the file in which the database is located
     */
    public void deleteAccountData(File contextFile){
        File saveFile = new File(contextFile, FILE_NAME);
        if (saveFile.delete()) {
            System.out.println("Successfully deleted");
        }
    }
}

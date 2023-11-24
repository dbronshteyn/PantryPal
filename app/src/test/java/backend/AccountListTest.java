package backend;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

import java.io.IOException;

import java.util.Date;


class AccountListTest {

    private AccountList accountList;
    private File databaseFile;

    @BeforeEach
    public void setUp() {
        databaseFile = new File("test-account-database.json");
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
        accountList = new AccountList(databaseFile);
    }

    @AfterEach
    public void tearDown() {
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
    }

    @Test
    void testAddAccountValid() {
        assertTrue(accountList.addAccount("test username", "test password"));
        assertEquals(1, accountList.getAccounts().size());
        assertTrue(accountList.addAccount("test username 2", "test password 2"));
        assertEquals(2, accountList.getAccounts().size());
    }

    @Test
    void testAddAccountInvalid() {
        assertTrue(accountList.addAccount("test username", "test password"));
        assertFalse(accountList.addAccount("test username", "test password 2"));
        assertEquals(1, accountList.getAccounts().size());
    }
}

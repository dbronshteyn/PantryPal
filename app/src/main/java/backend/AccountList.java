package backend;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;


public class AccountList {

    private File databaseFile;
    private List<Account> accounts;

    public class Account {
        private String username;
        private String password;

        public Account(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Account(JSONObject jsonAccount) {
            this.username = jsonAccount.getString("username");
            this.password = jsonAccount.getString("password");
        }

        public JSONObject toJSON() {
            JSONObject jsonAccount = new JSONObject();
            jsonAccount.put("username", this.username);
            jsonAccount.put("password", this.password);
            return jsonAccount;
        }

        public String getUsername() {
            return this.username;
        }

        public String getPassword() {
            return this.password;
        }
    }

    public AccountList(File databaseFile) {
        this.databaseFile = databaseFile;
        this.accounts = new ArrayList<>();
        this.loadAccountsFromFile();
    }

    public boolean addAccount(String username, String password) {
        for (Account account : this.accounts) {
            if (account.getUsername().equals(username)) {
                return false;
            }
        }
        this.accounts.add(new Account(username, password));
        this.updateDatabase();
        return true;
    }
    
    public boolean login(String username, String password) {
        for (Account account : this.accounts) {    
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void loadAccountsFromFile() {
        if (this.databaseFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(this.databaseFile.getAbsolutePath())));
                JSONArray accountList = new JSONArray(content);
                for (int i = 0; i < accountList.length(); i++) {
                    JSONObject jsonAccount = accountList.getJSONObject(i);
                    Account account = new Account(jsonAccount);
                    this.accounts.add(account);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateDatabase() {
        JSONArray jsonAccountList = new JSONArray();
        for (Account account : this.accounts) {
            jsonAccountList.put(account.toJSON());
        }
        try {
            FileWriter fw = new FileWriter(this.databaseFile);
            fw.write(jsonAccountList.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


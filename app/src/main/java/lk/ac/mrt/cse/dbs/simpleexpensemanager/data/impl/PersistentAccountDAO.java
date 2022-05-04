package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PersistentAccountDAO  extends SQLiteOpenHelper implements AccountDAO{

    public static final String ACCOUNT_TABLE = "`ACCOUNT`";
    public static final String TRANSACTION_TABLE = "`TRANSACTION`";
    public static final String ACCOUNT_NO = "Account_No";
    public static final String HOLDER = "Holder";
    public static final String BANK = "Bank";
    public static final String BALANCE = "Balance";
    public static final String ID = "ID";
    public static final String TYPE = "Type";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";

    public PersistentAccountDAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE `ACCOUNT`(" +
                "ACCOUNT_NO TEXT PRIMARY KEY," +
                "HOLDER TEXT NOT NULL," +
                "BANK TEXT NOT NULL," +
                "BALANCE REAL NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE `TRANSACTION`(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ACCOUNT_NO TEXT NOT NULL," +
                "TYPE TEXT NOT NULL," +
                "AMOUNT REAL NOT NULL," +
                "DATE REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public List<String> getAccountNumbersList() {
        return null;
    }

    @Override
    public List<Account> getAccountsList() {
        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}

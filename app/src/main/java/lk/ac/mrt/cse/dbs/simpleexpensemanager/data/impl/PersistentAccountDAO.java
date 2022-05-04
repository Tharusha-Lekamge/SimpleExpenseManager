package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO  extends SQLiteOpenHelper implements AccountDAO{

    public static final String ACCOUNT_TABLE = "`ACCOUNT`";
    public static final String TRANSACTION_TABLE = "`TRANSACTION`";

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
        List<String> accountNumbersList = new ArrayList<>();
        String accountNo;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM `ACCOUNT`", null);

        if (cursor.moveToFirst()) {
            do{
                accountNo = cursor.getString(0);
                accountNumbersList.add(accountNo);

            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        String accountNo;
        String holder;
        String bank;
        Double balance;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM `ACCOUNT`", null);

        if (cursor.moveToFirst()) {
            do{
                accountNo = cursor.getString(0);
                holder = cursor.getString(1);
                bank = cursor.getString(2);
                balance = cursor.getDouble(3);

                Account account = new Account(accountNo, bank, holder, balance);
                accountList.add(account);

            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryString = "SELECT ACCOUNT_NO FROM `ACCOUNT` WHERE ACCOUNT_NO=" + accountNo;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            String acNo = cursor.getString(0);
            String holder = cursor.getString(1);
            String bank = cursor.getString(2);
            double balance = cursor.getDouble(3);

            Account account = new Account(acNo, bank, holder, balance);

            cursor.close();
            sqLiteDatabase.close();

            return  account;
        }else{
            cursor.close();
            sqLiteDatabase.close();
            throw new InvalidAccountException("INVALID ACCOUNT NO");
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ACCOUNT_NO", account.getAccountNo());
        cv.put("HOLDER", account.getAccountHolderName());
        cv.put("BANK", account.getBankName());
        cv.put("BALANCE", account.getBalance());

        sqLiteDatabase.insertWithOnConflict(ACCOUNT_TABLE, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String deleteAccStatement = "DELETE FROM `ACCOUNT` WHERE ACCOUNT_NO = "+ accountNo;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(deleteAccStatement);
        sqLiteDatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String getBalanceQuery = "SELECT BALANCE FROM `ACCOUNT` WHERE ACCOUNT_NO=?";
        Cursor cursor = sqLiteDatabase.rawQuery(getBalanceQuery, new String[]{accountNo});

        if(cursor.moveToFirst()) {
            Double balance = cursor.getDouble(0);

            double newBalance;

            if (expenseType.equals(ExpenseType.INCOME)) {
                newBalance = balance + amount;
            } else if (expenseType.equals(ExpenseType.EXPENSE)){
                newBalance = balance - amount;
            } else {
                newBalance = balance;
            }

            String strNewBalance = Double.toString(newBalance);
            String updateString = "UPDATE `ACCOUNT` SET BALANCE = " + strNewBalance + " WHERE ACCOUNT_NO=?";

            sqLiteDatabase.execSQL(updateString,new String[]{accountNo});
        }

        cursor.close();
        sqLiteDatabase.close();
    }
}

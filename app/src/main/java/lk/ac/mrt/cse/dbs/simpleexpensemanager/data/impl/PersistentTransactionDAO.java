package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;



public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {

    public PersistentTransactionDAO(
            @Nullable Context context,
            @Nullable String name,
            @Nullable SQLiteDatabase.CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String strDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        String type = expenseType.equals(ExpenseType.EXPENSE)?"Expense":"Income";
        cv.put("ACCOUNT_NO", accountNo);
        cv.put("TYPE", type);
        cv.put("DATE", strDate);
        cv.put("AMOUNT",amount);

        db.insert("`TRANSACTION`", null, cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();
        String accountNo;
        String type;
        Double amount;
        String strDate;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM `TRANSACTION`", null);

        if (cursor.moveToFirst()){
            do{
                accountNo = cursor.getString(1);
                type = cursor.getString(2);
                amount = cursor.getDouble(3);
                strDate = cursor.getString(4);

                //double intAmount = Double.parseDouble(amount);
                ExpenseType expenseType;
                if (type == "EXPENSE"){
                    expenseType = ExpenseType.EXPENSE;
                }else{
                    expenseType = ExpenseType.INCOME;
                }
                Date date;
                try {
                    date = new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
                    Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }while(cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return transactionList;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionsList = getAllTransactionLogs();
        int size = transactionsList.size();
        if (size <= limit) {
            return transactionsList;
        } else{
            return transactionsList.subList(size - limit, size);
        }
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}

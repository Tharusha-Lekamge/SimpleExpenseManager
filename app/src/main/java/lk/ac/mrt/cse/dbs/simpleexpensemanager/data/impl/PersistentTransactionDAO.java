package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;



public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

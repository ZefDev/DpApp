package com.example.vadim.dpapp.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vadim.dpapp.containers.ActivContainer;
import com.example.vadim.dpapp.containers.CompliteTaskContainer;
import com.example.vadim.dpapp.containers.DocContainer;
import com.example.vadim.dpapp.containers.OTaskContainer;
import com.example.vadim.dpapp.containers.ReportContainer;
import com.example.vadim.dpapp.containers.TaskContainer;
import com.example.vadim.dpapp.containers.UserContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.example.vadim.dpapp/databases/";
    private static String DB_NAME = "base.db3";
    private static final int SCHEMA = 1; // версия базы данных

    public SQLiteDatabase database;
    private Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
    }

    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH + DB_NAME);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH + DB_NAME;
                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);
                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){
        }
    }

    public void open() throws SQLException {
        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    public void addTask(TaskContainer task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Code",task.getCode());
        values.put("Name",task.getTaskName());
        values.put("Kontracter",task.getContractor());
        values.put("Date",task.getDate());
        values.put("Ispolnitel",task.getExecutor());
        values.put("Complite",task.getComplite());
        db.insert("Task",null,values);
    }

    public int updateTask(TaskContainer task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Code",task.getCode());
        values.put("Name",task.getTaskName());
        values.put("Kontracter",task.getContractor());
        values.put("Date",task.getDate());
        values.put("Ispolnitel",task.getExecutor());
        values.put("Complite",task.getComplite());

        return db.update("Task", values, "Code = ?", new String[]{task.getCode()});
    }

    public ArrayList addOTasks(TaskContainer Otask){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList tmp = new ArrayList();
        ContentValues values = new ContentValues();
        //Log.e("Otask","Otask: " + Otask.getOtasks());
        for(OTaskContainer ot: Otask.getOtasks()){
            //Log.e("O","\ncodeTask: "  + ot.getCodeTask() + "\ncodeActiv: " + ot.getCodeActiv() +"\nname: " + ot.getOpisanie());
            //Log.e("Otask", ot.toString());
            values.put("name",ot.getOpisanie());
            values.put("codeActiv",ot.getCodeActiv());
            try {
                values.put("codeTask",new String(ot.getCodeTask().getBytes(),"Cp1251"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tmp.add(db.insert("DescriptionTask",null,values));
        }
        return tmp;
    }

    public ArrayList addCompliteTask(TaskContainer Otask){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList tmp = new ArrayList();
        ContentValues values = new ContentValues();
        //Log.e("Otask","Otask: " + Otask.getOtasks());
        int pos = 0;
        for(CompliteTaskContainer compliteTask: Otask.getCompliteTask()){
            //Log.e("O","\ncodeTask: "  + ot.getCodeTask() + "\ncodeActiv: " + ot.getCodeActiv() +"\nname: " + ot.getOpisanie());
            //Log.e("Otask", ot.toString());
            values.put("id",pos);
            values.put("date",compliteTask.getDate());
            values.put("compliteOTask",compliteTask.getCompliteTask());
            values.put("time",compliteTask.getTime());
            values.put("codeActiv",compliteTask.getCodeActiv());
            try {
                values.put("codeTask",new String(compliteTask.getCodeTask().getBytes(),"Cp1251"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tmp.add(db.insert("CompliteTask",null,values));
            pos++;
        }
        return tmp;
    }

    public ArrayList updateCompliteTasks(TaskContainer compliteTask){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CompliteTask","codeTask = ?", new String[]{compliteTask.getCode()});
        ArrayList tmp = new ArrayList();
        tmp =   addCompliteTask(compliteTask);
        return tmp;
    }

    public ArrayList updateOTasks(TaskContainer Otask){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DescriptionTask","codeTask = ?", new String[]{Otask.getCode()});
        ArrayList tmp = new ArrayList();
        tmp =   addOTasks(Otask);
        return tmp;
    }

    public void addActiv(ActivContainer activ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",activ.getCode());
        values.put("name",activ.getName());
        values.put("shtrihCode",activ.getShtrihCode());
        values.put("photo",activ.getPhoto());
        values.put("contractor",activ.getContractor());
        db.insert("activ",null,values);
    }

    public void addReport(ReportContainer report){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("shtrihCod",report.getShtrihCod());
        values.put("nameActiv",report.getNameActiv());
        values.put("mol",report.getMol());
        values.put("status",report.getStatus());
        values.put("divisionOfContractor",report.getDivisionOfContractor());
        db.insert("report",null,values);
    }

    public int updateActiv(ActivContainer activ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",activ.getCode());
        values.put("name",activ.getName());
        values.put("shtrihCode",activ.getShtrihCode());
        values.put("photo",activ.getPhoto());
        values.put("contractor",activ.getContractor());
        return db.update("activ", values, "number = ?", new String[]{activ.getCode()});
    }
    public int updateReport(ReportContainer report){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("shtrihCod",report.getShtrihCod());
        values.put("nameActiv",report.getNameActiv());
        values.put("mol",report.getMol());
        values.put("status",report.getStatus());
        values.put("divisionOfContractor",report.getDivisionOfContractor());
        return db.update("report", values, "shtrihCod = ?", new String[]{report.getShtrihCod()});
    }

    public ArrayList<TaskContainer> getAllTasks(String code){
        ArrayList<TaskContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(code!=null)
            cursor = db.query("Task",null,"Code = ?",new String[]{code},null,null,null);
        else
            cursor = db.query("Task",null,null,null,null,null,null);
        ArrayList<OTaskContainer> oTaskContainers = new ArrayList<>();
        ArrayList<CompliteTaskContainer> compliteTaskContainer = new ArrayList<>();
        int count = 0;
        if(cursor.moveToFirst()){
            oTaskContainers.clear();
            compliteTaskContainer.clear();
            Cursor cursorOTasks;
            Cursor cursorCompliteTask;
            do {
                if(code!=null)
                    cursorOTasks = db.query("DescriptionTask",null,"codeTask = ?",new String[] {code},null,null,null);
                else
                    cursorOTasks = db.query("DescriptionTask",null,null,null,null,null,null);
                if(cursorOTasks.moveToFirst()){
                    do {
                        OTaskContainer otask = new OTaskContainer(
                                cursorOTasks.getInt(cursorOTasks.getColumnIndex("code")),
                                cursorOTasks.getString(cursorOTasks.getColumnIndex("codeTask")),
                                cursorOTasks.getString(cursorOTasks.getColumnIndex("codeActiv")),
                                cursorOTasks.getString(cursorOTasks.getColumnIndex("name"))
                        );
                        oTaskContainers.add(otask);
                        count++;
                    }while (cursorOTasks.moveToNext());
                }
                if(code!=null)
                    cursorCompliteTask = db.query("CompliteTask",null,"codeTask = ?",new String[] {code},null,null,null);
                else
                    cursorCompliteTask = db.query("CompliteTask",null,null,null,null,null,null);
                if(cursorCompliteTask.moveToFirst()){
                    do {
                        CompliteTaskContainer compliteTask = new CompliteTaskContainer(
                                cursorCompliteTask.getInt(cursorCompliteTask.getColumnIndex("id")),
                                cursorCompliteTask.getString(cursorCompliteTask.getColumnIndex("date")),
                                cursorCompliteTask.getString(cursorCompliteTask.getColumnIndex("compliteOTask")),
                                cursorCompliteTask.getString(cursorCompliteTask.getColumnIndex("time")),
                                cursorCompliteTask.getString(cursorCompliteTask.getColumnIndex("codeTask")),
                                cursorCompliteTask.getString(cursorCompliteTask.getColumnIndex("codeActiv"))
                        );
                        compliteTaskContainer.add(compliteTask);
                        //count++;
                    }while (cursorCompliteTask.moveToNext());
                }
                TaskContainer container = new TaskContainer(
                        cursor.getString(cursor.getColumnIndex("Code")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Kontracter")),
                        cursor.getString(cursor.getColumnIndex("Date")),
                        cursor.getString(cursor.getColumnIndex("Ispolnitel")),
                        cursor.getString(cursor.getColumnIndex("Complite")),
                        new ArrayList(oTaskContainers),
                        new ArrayList(compliteTaskContainer)
                );
                list.add(container);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<ActivContainer> getAllActiv(){
        ArrayList<ActivContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Activ",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    ActivContainer container = new ActivContainer(
                            cursor.getString(cursor.getColumnIndex("number")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("typeActiv")),
                            cursor.getString(cursor.getColumnIndex("shtrihCode")),
                            cursor.getString(cursor.getColumnIndex("photo")),
                            cursor.getString(cursor.getColumnIndex("contractor"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }

    public ArrayList<ReportContainer> getAllReport(String shtrihCode){
        ArrayList<ReportContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(shtrihCode!=null)
            cursor = db.query("report",null,"shtrihCod = ?",new String[]{shtrihCode},null,null,null);
        else
            cursor = db.query("report",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    ReportContainer container = new ReportContainer(
                            cursor.getString(cursor.getColumnIndex("shtrihCod")),
                            cursor.getString(cursor.getColumnIndex("nameActiv")),
                            cursor.getString(cursor.getColumnIndex("mol")),
                            cursor.getString(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("divisionOfContractor"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }
    public String getIP(){
        String ip=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Setting",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                ip = cursor.getString(cursor.getColumnIndex("ip"))+":"+cursor.getString(cursor.getColumnIndex("port"));
            }
        }
        return ip;
    }
    public int saveIP(String ip, String port){
        SQLiteDatabase db = this.getWritableDatabase();
        int id=1;
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("port",port);
        values.put("ip",ip);
        return db.update("Setting", values, "id = ?",new String[]{"1"});
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Activ",null,null);
        db.delete("Task",null,null);
        db.delete("CompliteTask",null,null);
        db.delete("DescriptionTask",null,null);
        db.delete("Documents",null,null);

    }

    public void addDocument(DocContainer doc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codeDoc",doc.getCodeDoc());
        values.put("avtorDoc",doc.getAvtorDoc());
        values.put("messageDoc",doc.getMessageDoc());
        values.put("lastDate",doc.getLastDate());
        db.insert("Documents",null,values);
    }

    public int updateDocument(DocContainer doc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codeDoc",doc.getCodeDoc());
        values.put("avtorDoc",doc.getAvtorDoc());
        values.put("messageDoc",doc.getMessageDoc());
        return db.update("Documents", values, "codeDoc = ?", new String[]{doc.getCodeDoc()});
    }

    public ArrayList<DocContainer> getAllDocuments() {
        ArrayList<DocContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Documents",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    DocContainer container = new DocContainer(
                            cursor.getString(cursor.getColumnIndex("codeDoc")),
                            cursor.getString(cursor.getColumnIndex("avtorDoc")),
                            cursor.getString(cursor.getColumnIndex("messageDoc")),
                            cursor.getString(cursor.getColumnIndex("lastDate"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }

    /*public void addDscriptionTask(OTaskContainer OTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code",OTask.getCode());
        values.put("name",OTask.getOpisanie());
        values.put("codeTask",OTask.getCodeTask());
        values.put("codeActiv",OTask.getCodeActiv());
        db.insert("DescriptionTask",null,values);
    }*/

    public void addCompliteTask(CompliteTaskContainer compliteTaskContainer, int position){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",compliteTaskContainer.getCodeTask()+position);
        values.put("date",compliteTaskContainer.getDate());
        values.put("compliteOTask",compliteTaskContainer.getCompliteTask());
        values.put("time",compliteTaskContainer.getTime());
        values.put("codeTask",compliteTaskContainer.getCodeTask());
        values.put("codeActiv",compliteTaskContainer.getCodeActiv());
        db.insert("CompliteTask",null,values);
    }
    public void updateCompliteTask(int position,CompliteTaskContainer compliteTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.query("CompliteTask",null,"codeTask = ?",new String[]{compliteTask.getCodeTask()},null,null,null);
        int currPosition = 0;
        long updateId = position++;
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    Log.e("",""+cursor.getCount());
                    /*if(currPosition == position){
                        updateId = cursor.getLong(cursor.getColumnIndex("id"));
                        break;
                    }*/
                    Log.e("",""+cursor.getInt(cursor.getColumnIndex("id")));
                    Log.e("",""+cursor.getInt(cursor.getColumnIndex("codeTask")));
                    Log.e("",""+cursor.getInt(cursor.getColumnIndex("compliteOTask")));
                    Log.e("",""+cursor.getString(cursor.getColumnIndex("date")));
                    Log.e("",""+cursor.getString(cursor.getColumnIndex("time")));
                    currPosition++;
                }while (cursor.moveToNext());
            }
        }
        values.put("date",compliteTask.getDate());
        values.put("compliteOTask",compliteTask.getCompliteTask());
        values.put("time",compliteTask.getTime());
        db.update("CompliteTask",values,"id = ?", new String[]{String.valueOf(updateId)});
    }

    public ArrayList<OTaskContainer> getAllOTask(String codeActiv){
        ArrayList<OTaskContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("DescriptionTask",null,"codeActiv = ?",new String[]{codeActiv},null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    OTaskContainer container = new OTaskContainer(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("code"))),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("codeTask")),
                            cursor.getString(cursor.getColumnIndex("codeActiv")),
                            cursor.getString(cursor.getColumnIndex("time"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }

    public ArrayList<CompliteTaskContainer> getAllCompliteTask(String codeTask){
        ArrayList<CompliteTaskContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CompliteTask",null,"codeTask = ?",new String[]{codeTask},null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    CompliteTaskContainer container = new CompliteTaskContainer(
                            0,
                            cursor.getString(cursor.getColumnIndex("date")),
                            cursor.getString(cursor.getColumnIndex("compliteOTask")),
                            cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("codeTask")),
                            cursor.getString(cursor.getColumnIndex("codeActiv"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }

    public void addUser(UserContainer user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login",user.getLogin());
        values.put("password",user.getPassword());
        values.put("uid",user.getUid());
        db.insert("User",null,values);
    }

    public ArrayList<UserContainer> getUser(){
        ArrayList<UserContainer> list = new ArrayList<UserContainer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    UserContainer container = new UserContainer(
                            cursor.getString(cursor.getColumnIndex("login")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("uid"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }

    public void remove(String code,String keyColumn, String table){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table,keyColumn+" = " + code, null);
    }

    public void removeAll(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table,null,null);
    }

    public void updateCompliteTaskS(int position,CompliteTaskContainer compliteTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",compliteTask.getDate());
        values.put("compliteOTask",compliteTask.getCompliteTask());
        values.put("time",compliteTask.getTime());
        db.update("CompliteTask",values,"id = ?", new String[]{String.valueOf(position)});
    }
}

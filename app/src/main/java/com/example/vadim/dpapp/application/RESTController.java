package com.example.vadim.dpapp.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.ArraySet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vadim.dpapp.activity.ElementActivity;
import com.example.vadim.dpapp.activity.Pizdec;
import com.example.vadim.dpapp.activity.Setting;
import com.example.vadim.dpapp.activity.Sign_in;
import com.example.vadim.dpapp.adapters.ActivAdapter;
import com.example.vadim.dpapp.adapters.DocAdapter;
import com.example.vadim.dpapp.adapters.ReportAdapter;
import com.example.vadim.dpapp.adapters.TaskAdapter;
import com.example.vadim.dpapp.containers.ActivContainer;
import com.example.vadim.dpapp.containers.CompliteTaskContainer;
import com.example.vadim.dpapp.containers.DocContainer;
import com.example.vadim.dpapp.containers.OTaskContainer;
import com.example.vadim.dpapp.containers.ReportContainer;
import com.example.vadim.dpapp.containers.TaskContainer;
import com.example.vadim.dpapp.containers.UserContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RESTController {

    private ProgressDialog dialog;
    private DBHelper dbHelper;
    private Context context;
    private String tag;
    private String ip;
    private String prefix = "http://";
    String tag_string_req;
    String tag_message;
    JSONArray jsarray;

    public RESTController(Context context,String tag) {
        this.context = context;
        this.tag = tag;
        dbHelper = new DBHelper(context);
        dbHelper.open();
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        this.ip = dbHelper.getIP();

    }

    public void getTasks(final ListView listView,final Spinner spinner) {
        tag_string_req = "req_get_tasks";
        dialog.setMessage("Get tasks...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        String str = null;
                        try {
                            str = URLDecoder.decode(response,"windows-1251");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Log.d(tag, "Get Tasks Response: " + str.toString());

                        hideDialog();
                        ArrayList<TaskContainer> tmp = new ArrayList<>();
                        ArrayList<OTaskContainer> otasks = new ArrayList<>();
                        ArrayList<CompliteTaskContainer> compliteTask = new ArrayList<>();
                            JSONArray jsonarray = null;
                            try {
                                jsonarray = new JSONArray(response);
                                JSONArray arrayOTasks = new JSONArray();
                                JSONArray arrayCompliteTask = new JSONArray();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                otasks.clear();
                                compliteTask.clear();
                                JSONObject task = jsonarray.getJSONObject(i);
                                arrayOTasks = task.getJSONArray("otasks");
                                for (int j = 0; j < arrayOTasks.length(); j++) {
                                    JSONObject otask = arrayOTasks.getJSONObject(j);
                                    otasks.add(new OTaskContainer(0, task.getString("code"),
                                            otask.getString("codeActiv"),
                                            otask.getString("opisanie"),
                                            "",
                                            otask.getString("shtrihCode")));
                                }
                                arrayCompliteTask =  task.getJSONArray("compliteTasks");
                                for (int j = 0; j < arrayCompliteTask.length(); j++) {
                                    JSONObject cTask = arrayCompliteTask.getJSONObject(j);
                                    compliteTask.add(new CompliteTaskContainer(
                                            0,
                                            cTask.getString("date"),
                                            cTask.getString("compliteOTask"),
                                            cTask.getString("time"),
                                            cTask.getString("codeTask"),
                                            cTask.getString("codeActiv")));
                                }
                                TaskContainer taskContainer = new TaskContainer(
                                        task.getString("code"),
                                        task.getString("taskName"),
                                        task.getString("contractor"),
                                        task.getString("date"),
                                        task.getString("executor"),
                                        task.getString("complite"),
                                        new ArrayList(otasks),
                                        new ArrayList(compliteTask),
                                        task.getString("typeTask"),
                                        task.getString("isApproved"),
                                        task.getString("waypoint"),
                                        task.getString("nextExecutor")//<----
                                );
                                tmp.add(taskContainer);
                            }
                            ArrayList<TaskContainer> oldTasks = new ArrayList<>();
                            oldTasks = dbHelper.getAllTasks(null);
                            for(TaskContainer t: tmp){
                                dbHelper.removeAll("CompliteTask");
                                String oldTask = null;
                                for(TaskContainer oldt: oldTasks) {
                                    if(t.getCode().equals(oldt.getCode())){
                                        dbHelper.updateTask(t);
                                        dbHelper.updateOTasks(t);
                                        dbHelper.addCompliteTask(t);//dbHelper.updateCompliteTasks(t);
                                        oldTask = oldt.getCode();
                                        break;
                                    }
                                }
                                if(oldTask==null) {
                                    dbHelper.addTask(t);
                                    dbHelper.addOTasks(t);
                                    dbHelper.addCompliteTask(t);
                                }
                            }
                            listView.setAdapter(new TaskAdapter(context, dbHelper.getAllTasks(null)));
                            listView.setDividerHeight(0);
                            if(spinner!= null){
                                ArrayList list = dbHelper.statusTask();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
                                spinner.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_tasks");
                params.put("user", AppConfig.User);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getDocuments(final ListView listView) {
        tag_string_req = "req_get_doc";
        dialog.setMessage("Get documents...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Documents Response: " + response.toString());
                        hideDialog();
                        ArrayList<DocContainer> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;

                        try {
                            jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject doc = jsonarray.getJSONObject(i);
                                DocContainer docContainer = new DocContainer(
                                        doc.getString("codeDoc"),
                                        doc.getString("avtorDoc"),
                                        doc.getString("messageDoc"),
                                        doc.getString("dateDoc")
                                );
                                tmp.add(docContainer);
                            }

                            ArrayList<DocContainer> oldDoc = new ArrayList<>();
                            oldDoc = dbHelper.getAllDocuments();
                            for(DocContainer t: tmp){
                                String oldTask = null;
                                for(DocContainer oldt: oldDoc) {
                                    if(t.getCodeDoc().equals(oldt.getCodeDoc())){
                                        dbHelper.updateDocument(t);
                                        oldTask = oldt.getCodeDoc();
                                        break;
                                    }
                                }
                                if(oldTask==null) {
                                    dbHelper.addDocument(t);
                                }
                            }
                            listView.setAdapter(new DocAdapter(context, dbHelper.getAllDocuments()));
                            listView.setDividerHeight(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_documents");
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getActiv(final ListView listView,final Spinner spinner,final String shtrihCode) {
        tag_string_req = "req_get_activ";
        if(shtrihCode==null){
            tag_message = "Загрузка списка активов...";
        }
        else {
            tag_message = "Поиск актива...";
        }
        dialog.setMessage(tag_message);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Activs Response: " + response.toString());
                        hideDialog();
                        ArrayList<ActivContainer> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;

                        try {
                            jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject activ = jsonarray.getJSONObject(i);
                                ActivContainer activContainer = new ActivContainer(
                                        activ.getString("codeActiv"),
                                        activ.getString("nameActiv"),
                                        activ.getString("typeActiv"),
                                        activ.getString("shtrihActiv"),
                                        activ.getString("photo"),
                                        activ.getString("contractorActiv"),
                                        activ.getString("mol"),
                                        activ.getString("condition")
                                );
                                tmp.add(activContainer);
                            }

                            ArrayList<ActivContainer> oldDoc = new ArrayList<>();
                            oldDoc = dbHelper.getAllActiv();
                            for(ActivContainer t: tmp){
                                String oldTask = null;
                                for(ActivContainer oldt: oldDoc) {
                                    if(t.getCode().equals(oldt.getCode())){
                                        dbHelper.updateActiv(t);
                                        oldTask = oldt.getCode();
                                        break;
                                    }
                                }
                                if(oldTask==null) {
                                    dbHelper.addActiv(t);
                                }
                            }
                            if(shtrihCode!=null){
                                if(tmp.isEmpty()){
                                    dia("add_activ",(Activity) context,shtrihCode,null);
                                }
                                else {
                                    dia("activ_here",(Activity) context,shtrihCode,tmp.get(0).getName());
                                }
                            }
                            if(listView!=null) {
                                listView.setAdapter(new ActivAdapter(context, dbHelper.getAllActiv()));
                                listView.setDividerHeight(0);
                            }
                            if(spinner!= null){

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dbHelper.statusActiv());
                                spinner.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                if(spinner==null & listView==null){
                    params.put("tag", "get_activ_by_shtrihCode");
                    params.put("contractor","");
                    params.put("shtrihCode",shtrihCode);
                }
                else if(!AppConfig.rights.equals("Контрагент")) {
                    params.put("tag", "get_activ");
                    params.put("contractor","");
                    params.put("mol",AppConfig.User);
                }
                else{
                    params.put("tag", "get_activ");
                    params.put("contractor",AppConfig.Contractor);
                }
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getActivImage(final ImageView imageView, final String shtrih) {
        tag_string_req = "req_get_activ_image";
        dialog.setMessage("Get image...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Activ Images Response: " + response.toString());
                        hideDialog();
                        JSONArray jsonarray = null;
                        try {
                            jsonarray = new JSONArray(response);
                            String flag = (String)jsonarray.get(0);
                            String image = "";
                            if(flag.equals("true")){
                                image = (String)jsonarray.get(1);
                                Bitmap bitmap = ElementActivity.convertBase64StringToBitmap(image);
                                imageView.setImageBitmap(bitmap);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_activ_image");
                params.put("shtrih",shtrih);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getOtchet(final ListView listView, final Spinner spinner) {
        tag_string_req = "req_get_otchet";
        dialog.setMessage("Get otchet...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get otchet Response: " + response.toString());
                        hideDialog();
                        ArrayList<ReportContainer> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;

                        try {
                            jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject activ = jsonarray.getJSONObject(i);
                                ReportContainer reportContainer = new ReportContainer(
                                        activ.getString("shtrihCode"),
                                        activ.getString("nameActiv"),
                                        activ.getString("mol"),
                                        activ.getString("status"),
                                        activ.getString("divisionOfContractor")
                                );
                                tmp.add(reportContainer);
                            }

                            ArrayList<ReportContainer> oldDoc = new ArrayList<>();
                            oldDoc = dbHelper.getAllReport(null);
                            for(ReportContainer t: tmp){
                                String oldTask = null;
                                for(ReportContainer oldt: oldDoc) {
                                    if(t.getShtrihCod().equals(oldt.getShtrihCod())){
                                        dbHelper.updateReport(t);
                                        oldTask = oldt.getShtrihCod();
                                        break;
                                    }
                                }
                                if(oldTask==null) {
                                    dbHelper.addReport(t);
                                }
                            }

                            listView.setAdapter(new ReportAdapter(context, dbHelper.getAllReport(null)));
                            listView.setDividerHeight(0);
                            if(spinner!= null){

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dbHelper.statusReortActiv());
                                spinner.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_activ_contractor");
                params.put("contractor",AppConfig.Contractor);
                params.put("user",AppConfig.User);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    ArrayList<String> arrayString;

    public void getUser() {
        arrayString = new ArrayList<>();
        tag_string_req = "req_get_user";
        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Users Response: " + response.toString());
                        ArrayList<UserContainer> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;
                        try {
                            jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject user = jsonarray.getJSONObject(i);
                                UserContainer use = new UserContainer(
                                        user.getString("login"),
                                        user.getString("contractor"),
                                        user.getString("post"),
                                        user.getString("right"),
                                        user.getString("UID")
                                );
                                tmp.add(use);
                                arrayString.add(user.getString("post"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Sign_in.listUser=tmp;
                        if( Sign_in.listUser.size()!=0) {
                            if(Sign_in.listUser.get(0).getPost().equals("true")){
                                AppConfig.User=Sign_in.listUser.get(0).getLogin();
                                AppConfig.Contractor= Sign_in.listUser.get(0).getContractor();
                                AppConfig.rights= Sign_in.listUser.get(0).getRight();
                                context.startActivity(Sign_in.intent);
                            }
                            else if ((Sign_in.listUser.get(0).getPost().equals("false")&(!Sign_in.listUser.get(0).getUid().equals(""))&(!Sign_in.listUser.get(0).getRight().equals("")))) {//Вас заблокиировали
                                AppConfig.flagAccess=false; noAccess("Вас временно заблокировали, обратитесь к администратору!",true,null);//Toast.makeText(context,"Запрос отправлен, ожидайте подтверждение регистрации!",Toast.LENGTH_LONG).show();//noAccess();
                            }
                            else if ((Sign_in.listUser.get(0).getPost().equals("false")&(!Sign_in.listUser.get(0).getUid().equals(""))&(Sign_in.listUser.get(0).getRight().equals("")))) {//Вас заблокиировали
                                AppConfig.flagAccess=false; noAccess("Запрос отправлен, ожидайте подтверждение регистрации!",true,null);
                            }
                            else if((Sign_in.listUser.get(0).getPost().equals("false")&Sign_in.listUser.get(0).getUid().equals(""))){
                                context.startActivity(Sign_in.registrationIntent);
                            }
                        }
                        AppConfig.flagEnter = true;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                connectionProblem(tag_string_req,0);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_user");
                params.put("uid", AppConfig.uid);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    ArrayList<String> arrayContractor;
    public void getContractors(final Spinner spinner, final String currContractor) {
        arrayContractor = new ArrayList<>();
        tag_string_req = "req_get_contractor";
        dialog.setMessage("Get contractor...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Contractor Response: " + response.toString());
                        hideDialog();
                        ArrayList<String> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;
                        try {
                            jsonarray = new JSONArray(response);
                            arrayContractor.add("Выберите контрагента");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject user = jsonarray.getJSONObject(i);
                                tmp.add(user.getString("name"));
                                arrayContractor.add(tmp.get(i));
                            }
                            ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayContractor);
                            spinner.setAdapter(adapter);
                            if(currContractor!=null)
                                spinner.setSelection(arrayContractor.indexOf(currContractor));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_contractor");
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void sendDocuments(final String code,final String codeDoc, final String avtorDoc, final String message) {
        tag_string_req = "req_send_doc";
        dialog.setMessage("Send documents...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        /*if(code==null) {
                            dbHelper.addDocument(new DocContainer(codeDoc, avtorDoc, message,""));
                        }
                        else {
                            dbHelper.updateDocument(new DocContainer(codeDoc, avtorDoc, message,""));
                        }*/
                        hideDialog();
                        noAccess("Сообщение успешно отправлено!",false,(Activity) context);
                        Log.d(tag, "Send Documents Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "send_documents");
                params.put("codeDoc",codeDoc);
                params.put("nameDoc",avtorDoc);
                params.put("message",message);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void sendTask(final String codeTask, final String nameTask, final String contractorTask, final String dateTask, final String complite, final ArrayList<CompliteTaskContainer> list, final String typeTask,final String isApproved,final String waypoint,final String nextExecutor) {
        tag_string_req = "req_send_task";
        dialog.setMessage("Send task...");
        showDialog();
        //jsarray = new JSONArray(list.toString());

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "DBBBBBBBBBBBBB: " + complite);
                        if(complite.equals("Да")){

                        }
                        else {
                           dbHelper.updateTask(new TaskContainer(codeTask, nameTask, contractorTask, dateTask, complite));
                        }
                        Log.d(tag, "Send Tasks Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                connectionProblem(tag_string_req,0);
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "send_task");
                params.put("codeTask",codeTask);
                params.put("nameTask",nameTask);
                params.put("contractorTask",contractorTask);
                params.put("dateTask",dateTask);
                params.put("executer",AppConfig.User);
                params.put("complite",complite);
                params.put("listCompliteTask",list.toString());
                params.put("typeTask",typeTask);
                params.put("isApproved",isApproved);
                params.put("waypoint",waypoint);
                params.put("nextExecutor",nextExecutor);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void sendActiv(final String codeActiv, final String nameActiv, final String typeActiv, final String shtrihCode, final String photo, final String contractor) {
        tag_string_req = "req_send_activ";
        dialog.setMessage("Send activ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Send Activ Response: " + response.toString());
                        hideDialog();
                        Activity acivity = (Activity)context;
                        Intent intent =  acivity.getIntent();
                        acivity.finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, "Send activ Error: " + error.toString());
//                connectionProblem(tag_string_req,0);
                hideDialog();
                Activity acivity = (Activity)context;
                acivity.getIntent().putExtra("shtrihCode",shtrihCode);
                acivity.getIntent().putExtra("nameActiv",nameActiv);
                acivity.finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "send_activ");
                params.put("codeActiv",codeActiv);
                params.put("nameActiv",nameActiv);
                params.put("typeActiv",typeActiv);
                params.put("shtrihCode",shtrihCode);
                params.put("photo",photo);
                params.put("contractor",contractor);
                return params;
            }

        };
        try {
            strReq.getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void sendUser(final String login, final String loginMobile, final String contractor, final String UID) {
        tag_string_req = "req_send_user";
        dialog.setMessage("Send user...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Send user Response: " + response.toString());
                        noAccess("Запрос отправлен, ожидайте подтверждение регистрации!",true,null);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                noAccess("Запрос отправлен, ожидайте подтверждение регистрации!",true,null);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "send_user");
                params.put("login",login);
                params.put("loginMobile",loginMobile);
                params.put("contractor",contractor);
                params.put("uid",UID);
                params.put("post","false");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getDirectoryUser(final Spinner spinner) {
        final ArrayList<String> arrayDirectoryUser = new ArrayList<>();
        tag_string_req = "req_get_directoryUser";
        dialog.setMessage("Get directoryUser...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get directoryUser Response: " + response.toString());
                        hideDialog();
                        ArrayList<String> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;
                        try {
                            jsonarray = new JSONArray(response);
                            arrayDirectoryUser.add("Выберите исполнителя");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject user = jsonarray.getJSONObject(i);
                                tmp.add(user.getString("name"));
                                arrayDirectoryUser.add(tmp.get(i));
                            }
                            ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayDirectoryUser);
                            spinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_directoryUser");
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog(){
        if(!dialog.isShowing()){
            dialog.show();
        }
    }

    private void hideDialog(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
    /* 0- для главного меню и остальных активити
       1- для врагментов */
    public void connectionProblem(String tag, final int flag){
        Activity cont = (Activity) context;
        final Intent settingActivity  = new Intent(context, Setting.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch(tag){
            case "req_get_user":
                builder.setMessage("Отсутствует подключение к интернету, повторить попытку?")
                        .setCancelable(false)
                        .setPositiveButton("Повтор",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ip = dbHelper.getIP();
                                        getUser();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Настройки",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        context.startActivity(settingActivity);
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(flag==0) {
                                            ((Activity) context).finish();
                                        }
                                        dialog.cancel();
                                    }
                                });

                break;
            case "req_get_activ":
            case "req_get_doc":
            case "req_get_tasks":
            case "req_send_doc":
            case "req_send_activ":
            case "req_send_task":
                builder.setTitle("Отсутствует подключение к интернету, повторите попытку")
                        .setCancelable(false)
                        .setPositiveButton("Ок",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                break;
        }
        AlertDialog alert = builder.create();
        if(!cont.isFinishing()) {
            alert.show();
        }

    }

    public void noAccess(String message, final boolean flag, final Activity cntx){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        //((Activity) context).finish();
                                        if(flag){
                                            System.exit(0); //android.os.Process.killProcess(android.os.Process.myPid());//<-------Так делать плохо, не надо так!!!!
                                        }
                                        else {
                                            cntx.finish();
                                        }
                                    }
                                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void dia(final String tag, final Activity cntx, final String shtrihCode,final String nameActiv){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "";
        if(tag=="add_activ") {
            message = "Актив в базе не найден! Создать новый актив?";
            builder.setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent intent = new Intent(cntx, ElementActivity.class);
                            intent.putExtra("shtrihCode",shtrihCode);
                            cntx.startActivityForResult(intent,1);
                        }
                    });
        }
        else if(tag=="activ_here"){
            message = "Актив в базе найден!";
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            cntx.getIntent().putExtra("nameActiv",nameActiv);
                            dialog.dismiss();
                        }
                    });
        }
        AlertDialog alert = builder.create();
        alert.show();

    }


}



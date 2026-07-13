package com.mohitingale.agroguide;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mohitingale.agroguide.Common.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    EditText etregistername,etregisterusername,etregisteremail,etregisterphone,etregisterpass,etregisterconfirmpass;
    CheckBox cbshowhidepass;
    Button btn_register;
    TextView tvregister_login;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etregistername=findViewById(R.id.etregistername);
        etregisterusername=findViewById(R.id.etregisterusername);
        etregisteremail=findViewById(R.id.etregisteremail);
        etregisterphone=findViewById(R.id.etregisterphone);
        etregisterpass=findViewById(R.id.etregisterpass);
        etregisterconfirmpass=findViewById(R.id.etregisterconfirmpass);

        cbshowhidepass=findViewById(R.id.cbshowhidepass);

        btn_register=findViewById(R.id.btn_register);

        tvregister_login=findViewById(R.id.tvregister_login);

        progressDialog = new ProgressDialog(RegisterActivity.this);

        cbshowhidepass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    etregisterconfirmpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etregisterconfirmpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (etregistername.getText().toString().isEmpty())
                {
                    etregistername.setError("Please Enter Your Full Name.");
                }
                else if (etregisterusername.getText().toString().isEmpty())
                {
                    etregisterusername.setError("Please Enter Your Username.");
                }
                else if (etregisterusername.getText().toString().length()<8)
                {
                    etregisterusername.setError("Username must be more than 8");
                }
                else if (!etregisterusername.getText().toString().matches("(.*[A-Z].*)(.*[a-z].*)(.*[@#$%^&+=!].*)(.*[0-9].*)"))
                {
                    etregisterusername.setError("Username must contain Uppercase, Lowercase, Number and Special Character");
                }
                else if (etregisteremail.getText().toString().isEmpty())
                {
                    etregisteremail.setError("Please Enter Your Email Address.");
                }
                else if (!etregisteremail.getText().toString().contains("@") || !etregisteremail.getText().toString().contains(".com"))
                {
                    etregisteremail.setError("Please enter Valid Email Id");
                }
                else if (etregisterphone.getText().toString().isEmpty())
                {
                    etregisterphone.setError("Please Enter Your Mobile No.");
                }
                else if (etregisterphone.getText().toString().length()!=10)
                {
                    etregisterphone.setError("Mobile No. Length Must be 10");
                }
                else if (etregisterpass.getText().toString().isEmpty())
                {
                    etregisterpass.setError("Please Enter Your Password");
                }
                else if (etregisterpass.getText().toString().length() < 8)
                {
                    etregisterpass.setError("Password must be more than 8");
                }
                else if (!etregisterpass.getText().toString().matches("(.*[A-Z].*)(.*[a-z].*)(.*[@#$%^&+=!].*)(.*[0-9].*)"))
                {
                    etregisterpass.setError("Password must contain Uppercase, Lowercase, Number and Special Character");
                }
                else if (etregisterconfirmpass.getText().toString().isEmpty())
                {
                    etregisterconfirmpass.setError("Please Enter Your Password");
                }
                else if (etregisterconfirmpass.getText().toString().length() < 8)
                {
                    etregisterconfirmpass.setError("Password must be more than 8");
                }
                else if (!etregisterconfirmpass.getText().toString().matches("(.*[A-Z].*)(.*[a-z].*)(.*[@#$%^&+=!].*)(.*[0-9].*)"))
                {
                    etregisterconfirmpass.setError("Password must contain Uppercase, Lowercase, Number and Special Character");
                }
                else if (!etregisterpass.getText().toString().equals(etregisterconfirmpass.getText().toString()))
                {
                    etregisterconfirmpass.setError("Password and Confirm Password not match");
                }
                else
                {
                    progressDialog.setTitle("Registration Process...");
                    progressDialog.setMessage("Plzz wait");
                    progressDialog.show();
                    registerUser();
                }

            }
        });
        tvregister_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Optional: prevents returning to RegisterActivity
                    }
                }
        );
    }

    private void registerUser()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",etregistername.getText().toString());
        params.put("username",etregisterusername.getText().toString());
        params.put("email",etregisteremail.getText().toString());
        params.put("phone",etregisterphone.getText().toString());
        params.put("password",etregisterpass.getText().toString());

        client.post(Urls.registerUserAPI,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        progressDialog.dismiss();

                        if (response == null) {
                            Toast.makeText(RegisterActivity.this, "Response is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            String status = response.getString("success");
                            String message = response.getString("message");

                            if (status.equals("1"))
                            {
                                Toast.makeText(RegisterActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,""+message,Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {


                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        String error = (errorResponse != null) ? errorResponse.toString() : throwable.getMessage();
                        Toast.makeText(RegisterActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }
}
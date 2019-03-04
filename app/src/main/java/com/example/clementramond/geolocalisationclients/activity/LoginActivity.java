package com.example.clementramond.geolocalisationclients.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.database.dao.DossierDAO;
import com.example.clementramond.geolocalisationclients.database.dao.UtilisateurDAO;
import com.example.clementramond.geolocalisationclients.modele.Dossier;
import com.example.clementramond.geolocalisationclients.modele.Droit;
import com.example.clementramond.geolocalisationclients.modele.Utilisateur;

import java.util.ArrayList;

/**
 * A login screen that offers login via pseudo/password.
 */
public class LoginActivity extends OptionsActivity implements AdapterView.OnItemSelectedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPseudoView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner mDossierSpinner;

    private ArrayAdapter<Dossier> mDossierAdapter;

    private UtilisateurDAO utilisateurDAO;

    private ArrayList<Dossier> dossiers;
    private Dossier dossier = null;
    private boolean selectionDossier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        super.setActivity(R.id.login_form);
        super.setLoading(R.id.loading);

        // Set up the login form.
        mPseudoView = (EditText) findViewById(R.id.pseudo);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mPseudoSignInButton = (Button) findViewById(R.id.pseudo_sign_in_button);
        mPseudoSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mDossierSpinner = findViewById(R.id.dossier);
        dossiers = new DossierDAO(this).getAll();
        mDossierAdapter = new ArrayAdapter<>(this, R.layout.custom_dropdown_item, dossiers);

        mDossierSpinner.setAdapter(mDossierAdapter);
        mDossierSpinner.setOnItemSelectedListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        utilisateurDAO = new UtilisateurDAO(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid pseudo, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPseudoView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String pseudo = mPseudoView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid pseudo.
        if (TextUtils.isEmpty(pseudo)) {
            mPseudoView.setError(getString(R.string.error_field_required));
            focusView = mPseudoView;
            cancel = true;
        }
        

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(pseudo, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void selectDossier(boolean b) {
        selectionDossier = b;
        dossier = b?(Dossier) mDossierSpinner.getSelectedItem():null;
        mDossierSpinner.setVisibility(b?View.VISIBLE:View.GONE);
        mPseudoView.setEnabled(!b);
        mPasswordView.setEnabled(!b);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        dossier = dossiers.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void accueil() {
        Intent intent = new Intent(this, ListeClientActivity.class);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mPseudo;
        private final String mPassword;

        private final int CONNEXION_OK = 0;
        private final int INVALID_PSEUDO = 1;
        private final int INVALID_PASSWORD = 2;
        private final int DB_ERROR = 3;

        private Utilisateur utilisateur;

        UserLoginTask(String pseudo, String password) {
            mPseudo = pseudo;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            ArrayList<Utilisateur> utilisateurs;

            try {
                utilisateurs = utilisateurDAO.getAll();
                // Simulate network access.
                //Thread.sleep(2000);
            } catch (Exception e) {
                return DB_ERROR;
            }

            for (Utilisateur u : utilisateurs) {
                if (mPseudo.equals(u.getPseudo())) {
                    if (mPassword.equals(u.getMdp())) {
                        utilisateur = u;
                        return CONNEXION_OK;
                    } else {
                        return INVALID_PASSWORD;
                    }
                }
            }
            return INVALID_PSEUDO;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);

            if (success == CONNEXION_OK) {
                Params.connectedUser = utilisateur;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Params.PREF_USER, utilisateur.getPseudo());
                if (utilisateur.getDroit().getDroit().equals(Droit.DROITS[2])) {
                    if (!selectionDossier) {
                        selectDossier(true);
                        editor.apply();
                        editor = null;
                    }
                } else {
                    dossier = utilisateur.getDossier();
                }
                if (editor != null) {
                    Params.dossier = dossier;
                    editor.putString(Params.PREF_DOSSIER, String.valueOf(dossier.getId())).apply();
                    accueil();
                }
            } else if (success == INVALID_PSEUDO) {
                mPseudoView.setError(getString(R.string.error_incorrect_pseudo));
                mPseudoView.requestFocus();
            } else if (success == INVALID_PASSWORD) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            } else {
                Toast.makeText(getApplicationContext(),R.string.error_cannot_access_db, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


package com.example.clementramond.geolocalisationclients.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.clementramond.geolocalisationclients.Params;
import com.example.clementramond.geolocalisationclients.R;
import com.example.clementramond.geolocalisationclients.database.dao.CategorieDAO;
import com.example.clementramond.geolocalisationclients.database.dao.ClientDAO;
import com.example.clementramond.geolocalisationclients.database.dao.SousCategorieDAO;
import com.example.clementramond.geolocalisationclients.modele.Categorie;
import com.example.clementramond.geolocalisationclients.modele.Client;
import com.example.clementramond.geolocalisationclients.modele.SousCategorie;

import java.util.ArrayList;

public class ListeClientActivity extends OptionsActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private EditText mRechercheView;

    private Spinner mCategorieView, mSousCategorieView;

    private ListView mListeClientView;

    private ArrayAdapter<Categorie> mCategorieAdapter;
    private ArrayAdapter<SousCategorie> mSousCategorieAdapter;
    private ArrayAdapter<Client> mClientAdapter;

    private CategorieDAO categorieDAO;
    private SousCategorieDAO sousCategorieDAO;
    private ClientDAO clientDAO;

    private ArrayList<Categorie> listCategories;
    private ArrayList<SousCategorie> listSousCategories;
    private ArrayList<Client> listClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_client);

        super.setActivity(R.id.activity);
        super.setLoading(R.id.loading);

        listCategories = new ArrayList<>();
        listSousCategories = new ArrayList<>();
        listClients = new ArrayList<>();

        categorieDAO = new CategorieDAO(this);
        sousCategorieDAO = new SousCategorieDAO(this);
        clientDAO = new ClientDAO(this);

        mRechercheView = findViewById(R.id.nom_prenom);

        mCategorieView = findViewById(R.id.categorie);

        mSousCategorieView = findViewById(R.id.sous_categorie);

        mListeClientView = findViewById(R.id.liste_clients);

        mCategorieAdapter = new ArrayAdapter<>(this, R.layout.custom_dropdown_item,
                listCategories);
        setCategories(categorieDAO.getAll());

        mSousCategorieAdapter = new ArrayAdapter<>(this, R.layout.custom_dropdown_item,
                listSousCategories);
        setSousCategories(new ArrayList<SousCategorie>());

        mClientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                listClients);
        setClients(clientDAO.getAll());

        mCategorieView.setAdapter(mCategorieAdapter);
        mCategorieView.setOnItemSelectedListener(this);

        mSousCategorieView.setAdapter(mSousCategorieAdapter);
        mSousCategorieView.setOnItemSelectedListener(this);

        mListeClientView.setAdapter(mClientAdapter);
        mListeClientView.setOnItemClickListener(this);

        mRechercheView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    String[] keywords = charSequence.toString().toLowerCase().split(" ");
                    ArrayList<Client> resultat = new ArrayList<>();
                    for (Client c : listClients) {
                        if (c != null) {
                            for (String k : keywords) {
                                if (c.getNom() != null && c.getNom().toLowerCase().trim().startsWith(k)) {
                                    resultat.add(c);
                                    break;
                                } else if (c.getPrenom() != null && c.getPrenom().toLowerCase().trim().startsWith(k)) {
                                    resultat.add(c);
                                    break;
                                } else if (c.getCodePostal() != null && c.getCodePostal().toLowerCase().trim().startsWith(k)) {
                                    resultat.add(c);
                                    break;
                                } else if (c.getTelephoneFixe() != null && c.getTelephoneFixe().toLowerCase().trim().startsWith(k)) {
                                    resultat.add(c);
                                    break;
                                } else if (c.getTelephonePortable() != null && c.getTelephonePortable().toLowerCase().trim().startsWith(k)) {
                                    resultat.add(c);
                                    break;
                                }
                            }
                        }
                    }
                    setClients(resultat);
                } else {
                    if (mSousCategorieView.getSelectedItem().equals(listSousCategories.get(0))) {
                        if (mCategorieView.getSelectedItem().equals(listCategories.get(0))) {
                            setClients(clientDAO.getAll());
                        } else {
                            setClients(clientDAO.getFromCategorie((Categorie)mCategorieView.getSelectedItem()));
                        }
                    } else {
                        setClients(clientDAO.getFromSousCategorie((SousCategorie)mSousCategorieView.getSelectedItem()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setCategories(ArrayList<Categorie> newList) {
        listCategories.clear();
        listCategories.add(new Categorie("-- Pas de sélection --"));
        listCategories.addAll(newList);
        mCategorieAdapter.notifyDataSetChanged();
    }

    private void setSousCategories(ArrayList<SousCategorie> newList) {
        listSousCategories.clear();
        listSousCategories.add(new SousCategorie("-- Pas de sélection --"));
        listSousCategories.addAll(newList);
        mSousCategorieAdapter.notifyDataSetChanged();
    }

    private void setClients(ArrayList<Client> newList) {
        listClients.clear();
        listClients.addAll(newList);
        mClientAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.categorie :
                setSousCategories(sousCategorieDAO.getFromCategorie(listCategories.get(i)));
                mSousCategorieView.setSelection(0);
                onItemSelected(mSousCategorieView, null, 0, -1);
                break;
            case R.id.sous_categorie :
                if (i != 0) {
                    setClients(clientDAO.getFromSousCategorie(listSousCategories.get(i)));
                } else if (mCategorieView.getSelectedItem().equals(listCategories.get(0))) {
                    setClients(clientDAO.getAll());
                } else {
                    setClients(clientDAO.getFromCategorie((Categorie) mCategorieView.getSelectedItem()));
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.liste_clients :
                Intent intent = new Intent(this, FicheClientActivity.class);
                intent.putExtra(Params.EXT_CLIENT, listClients.get(i));
                startActivity(intent);
                break;
        }
    }
}

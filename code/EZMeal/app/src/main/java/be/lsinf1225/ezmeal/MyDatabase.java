package be.lsinf1225.ezmeal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Laurent on 25/04/2017.
 */

public class MyDatabase extends SQLiteOpenHelper {
    private static final String TAG="MyDatabase";
    private static final String DATABASE_NAME="database.sqlite";
    private static final int DATABASE_VERSION=2;
    private static Context context;

    private static final String AVIS_TABLE="avis";
    private static final String AVIS_AUTHOR_COLUMN="username";
    private static final String AVIS_RECETTE_COLUMN="recette_name";
    private static final String AVIS_NOTE_COLUMN="note";
    private static final String AVIS_COMMENTAIRE_COLUMN="commentaire";

    private static final String STEP_TABLE="etape";
    private static final String STEP_NAME_COLUMN="etape_name";
    private static final String STEP_NUMBER_COLUMN="numero";
    private static final String STEP_EXPLANATION_COLUMN="explication";
    private static final String STEP_PICTURE_COLUMN="image";
    private static final String STEP_VIDEO_COLUMN="video";
    private static final String STEP_TIME_COLUMN="temps";
    private static final String STEP_TYPE_COLUMN="type";

    private static final String INGREDIENT_UNITE_TABLE="ingr_unite";
    private static final String INGREDIENT_UNITE_NAME_COLUMN="ingr_name";
    private static final String INGREDIENT_UNITE_COLUMN="unite";

    private static final String INGREDIENT_TABLE="ingredient";
    private static final String INGREDIENT_RECETTE_COLUMN="recette_name";
    private static final String INGREDIENT_NAME_COLUMN="ingr_name";
    private static final String INGREDIENT_QUANTITY_COLUMN="quantite";

    private static final String RECETTE_TABLE="recette";
    private static final String RECETTE_NAME_COLUMN="recette_name";
    private static final String RECETTE_PICTURE_COLUMN="photo";
    private static final String RECETTE_DESCRIPTION_COLUMN="description";
    private static final String RECETTE_DATE_COLUMN="date";
    private static final String RECETTE_AUTHOR_COLUMN="auteur_name";
    private static final String RECETTE_NBRE_PERS_COLUMN="nbre_pers";
    private static final String RECETTE_DIFFICULTY_COLUMN="difficulte";
    private static final String RECETTE_TYPE_COLUMN="type";
    private static final String RECETTE_SOUS_TYPE_COLUMN="sous_type";

    private static final String USER_TABLE="user";
    private static final String USER_USERNAME_COLUMN="username";
    private static final String USER_PASWD_COLUMN="password";
    private static final String USER_AGE_COLUMN="age";
    private static final String USER_ADDRESS_COLUMN="residence";
    private static final String USER_GENDER_COLUMN="sexe";

    public MyDatabase(Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS '"+USER_TABLE+"';");
        db.execSQL("CREATE TABLE '"+USER_TABLE+"' ('"+
                USER_USERNAME_COLUMN+"' TEXT NOT NULL PRIMARY KEY, '"+
                USER_PASWD_COLUMN+"' TEXT NOT NULL, '"+
                USER_AGE_COLUMN+"' INTEGER NOT NULL, '"+
                USER_ADDRESS_COLUMN+"' TEXT NOT NULL, '"+
                USER_GENDER_COLUMN+"' TEXT NOT NULL);"
        );
        db.execSQL("INSERT INTO "+USER_TABLE+"("+USER_USERNAME_COLUMN+","+USER_PASWD_COLUMN+","+USER_AGE_COLUMN+","+USER_ADDRESS_COLUMN+","+USER_GENDER_COLUMN+")"+
                "VALUES ('Laurent', 'laurent', 19, 'Rue antoine 10', 'Homme')," +
                "('Morgane', 'momo4ever', 38, 'Rue du gateau 19a', 'Femme')," +
                "('Hadrien', 'hadoufume', 29, 'Rue nationale 1', 'Homme')," +
                "('Sophie', '1234', 20, 'Rue des champs 1, 7500 Tournai', 'Femme')," +
                "('Topichef', 'toptop', 53, 'Avenue des plats 10', 'Homme');"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.deleteDatabase(db);
        this.onCreate(db);
    }
    private void deleteDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS '"+USER_TABLE+"';");
    }

    public boolean open() {
        try {
            getWritableDatabase();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    public boolean checkDataLogin(String username, String password) {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(USER_TABLE,
                new String[]{USER_PASWD_COLUMN},
                USER_USERNAME_COLUMN+" ='"+username+"'",
                null,
                null,
                null,
                null);
        if(cursor.getCount()==0) {
            cursor.close();
            return false;
        }
        if(cursor.moveToFirst()) {
            boolean flag = cursor.getString(cursor.getColumnIndex(USER_PASWD_COLUMN)).equals(password);
            cursor.close();
            return flag;
        }
        return false;
    }
    public boolean checkUsernameDB(String username) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(USER_TABLE,
                new String[]{USER_USERNAME_COLUMN},
                USER_USERNAME_COLUMN+" ='"+username+"'",
                null,
                null,
                null,
                null);

        if(cursor.getCount()==0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }
    public boolean addDataRegister(String username, String password, String age, String address, String gender) {
        int index=this.getIndex(this.context.getResources().getStringArray(R.array.gender),gender);
        switch(index) {
            case 0:
                gender="Homme";
                break;
            case 1:
                gender="Femme";
                break;
            case 2:
                gender="Autre";
                break;
        }

        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("INSERT INTO "+USER_TABLE+"("+USER_USERNAME_COLUMN+","+USER_PASWD_COLUMN+","+USER_AGE_COLUMN+","+USER_ADDRESS_COLUMN+","+USER_GENDER_COLUMN+")"+
                "VALUES ('"+username+"','"+password+"','"+age+"','"+address+"','"+gender+"');");
        return true;
    }
    public int getIndex(String[] array, String elem) {
        int size=array.length;
        for(int i=0; i<size; i++) {
            if(elem==array[i])
                return i;
        }
        return -1;
    }
}
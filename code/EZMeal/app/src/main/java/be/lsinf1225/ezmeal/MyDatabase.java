package be.lsinf1225.ezmeal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurent on 25/04/2017.
 */

public class MyDatabase extends SQLiteOpenHelper {

    private static final String TAG="MyDatabase";
    private static final String DATABASE_NAME="database.sqlite";
    private static final int DATABASE_VERSION=3;
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

        //creation table user
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

        //creation table step
        db.execSQL("DROP TABLE IF EXISTS '"+STEP_TABLE+"';");
        db.execSQL("CREATE TABLE '"+STEP_TABLE+"' ('"+
                STEP_NAME_COLUMN+"' TEXT NOT NULL PRIMARY KEY, '"+
                RECETTE_NAME_COLUMN+"' TEXT NOT NULL, '"+
                STEP_NUMBER_COLUMN+"' INTEGER NOT NULL, '"+
                STEP_EXPLANATION_COLUMN+"' TEXT NOT NULL, '"+
                STEP_PICTURE_COLUMN+"' TEXT, '"+
                STEP_VIDEO_COLUMN+"' TEXT, '"+
                STEP_TIME_COLUMN+"' INTEGER NOT NULL, '"+
                STEP_TYPE_COLUMN+"' TEXT NOT NULL)");

        db.execSQL("INSERT INTO "+STEP_TABLE+"( "+STEP_NAME_COLUMN+", "+RECETTE_NAME_COLUMN+", "+STEP_NUMBER_COLUMN+", "+STEP_EXPLANATION_COLUMN+", "+STEP_TIME_COLUMN+", "+STEP_TYPE_COLUMN+")"+
                "VALUES ('Cuire la lasagne', 'Lasagne', 2, 'Mettre la lasagne au four à 200', 30, 'cuisson')," +
                "('Couper les oignons', 'Lasagne', 1, 'Couper les oignons en petits cubes de 1cm de côté', 10, 'preparation')"
        );
        

        //creation table recette
        /*db.execSQL("DROP TABLE IF EXISTS '"+RECETTE_TABLE+"';");
        db.execSQL("CREATE TABLE '"+RECETTE_TABLE+"' ('"+
                RECETTE_NAME_COLUMN+"' TEXT NOT NULL PRIMARY KEY, '"+
                RECETTE_PICTURE_COLUMN+"' TEXT" + ", '"+
                RECETTE_DESCRIPTION_COLUMN+"' TEXT NOT NULL, '"+
                RECETTE_DATE_COLUMN+"' DATETIME NOT NULL, '"+
                RECETTE_AUTHOR_COLUMN+"' TEXT NOT NULL, '"+
                RECETTE_NBRE_PERS_COLUMN+"' INTEGER NOT NULL, '"+
                RECETTE_DIFFICULTY_COLUMN+"' INTEGER NOT NULL, '"+
                RECETTE_TYPE_COLUMN+"' TEXT NOT NULL, '"+
                RECETTE_SOUS_TYPE_COLUMN+"' TEXT NOT NULL);"
        );
        db.execSQL("INSERT INTO "+RECETTE_TABLE+"("+RECETTE_NAME_COLUMN+","+RECETTE_DESCRIPTION_COLUMN+","+RECETTE_DATE_COLUMN+","+RECETTE_AUTHOR_COLUMN+","+RECETTE_NBRE_PERS_COLUMN+","+RECETTE_DIFFICULTY_COLUMN+","+RECETTE_TYPE_COLUMN+","+RECETTE_SOUS_TYPE_COLUMN+")"+
                "VALUES ('Lasagne','Cette recette traditionelle de lasagnes vous donnera le vrai goût Italien.', '2017-05-01', 'MamaItalia',6,3,'plat','italien'), "+
                "('Muffins au chocolat','Ces délicieux muffins au chocolats vous ferons retrouver les saveurs de votre enfance.', '2017-04-04', 'DeliDessert',8,2,'dessert','chocolaté'), "+
                "('Toats aux champignons','Cette entrée de fête est parfaite pour commencer un repas royal !', '2016-09-21', 'PapyCuistot',5,4,'entrée','forestier'),"
        );

        //creation table avis
        db.execSQL("DROP TABLE IF EXISTS '"+AVIS_TABLE+"';");
        db.execSQL(String.format("CREATE TABLE %1$s(%2$s TEXT PRIMARY KEY REFERENCES %6$s,%3$s TEXT PRIMARY KEY REFERENCES %7$s,%4$s TEXT,%5$s INTEGER);",
                AVIS_TABLE,AVIS_AUTHOR_COLUMN,AVIS_RECETTE_COLUMN,AVIS_COMMENTAIRE_COLUMN,AVIS_NOTE_COLUMN,USER_TABLE,RECETTE_TABLE));
        */

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.deleteDatabase(db);
        this.onCreate(db);
    }
    private void deleteDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS '"+USER_TABLE+"';");
        db.execSQL("DROP TABLE IF EXISTS '"+RECETTE_TABLE+"';");
        db.execSQL("DROP TABLE IF EXISTS '"+STEP_TABLE+"';");
    }

    public boolean open() {
        try {
            this.getWritableDatabase();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    //verification de sign in username+password
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


    /**
     * Ajoute ou écrase une commentaire d'un utilsateur sur un recette
     * @param comment texte du commentaire
     * @param username nom de l'utilisateur postant le commentaire
     * @param recipe nom de la recette commentée
     */
    public void addComment(String comment, String username, String recipe){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(AVIS_TABLE,
                new String[]{AVIS_AUTHOR_COLUMN}, //on prend une seule colonne car on n'est interessé que par le nombre de lignes
                AVIS_AUTHOR_COLUMN+" ='"+username+"'AND"+AVIS_RECETTE_COLUMN+" ='"+recipe+"'",
                null,
                null,
                null,
                null);

        if(cursor.getCount()==0) {//si il n'y as pas d'avis de l'utilsateur
            db.execSQL(String.format("INSERT INTO %1$s(%2$s,%3$s,%4$s,%5$s) VALUES ('%6$s','%7$s',NULL,'%8$s');",
                    AVIS_TABLE,AVIS_AUTHOR_COLUMN,AVIS_RECETTE_COLUMN,AVIS_NOTE_COLUMN,AVIS_COMMENTAIRE_COLUMN,username,recipe,comment));
        }
        else{//si il ya déja un avis créé
            db.execSQL(String.format("UPDATE %1$s SET %2$s = '%3$s' WHERE %4$s = '%5$s' AND %6$s = '%7$s'"
                    ,AVIS_TABLE,AVIS_COMMENTAIRE_COLUMN,comment,AVIS_AUTHOR_COLUMN,username,AVIS_RECETTE_COLUMN,recipe));
        }
    }

    /**
     * Ajoute ou écrase une note d'un utilsateur sur un recette
     * @param grade note, inclus dans [0 , 5]
     * @param username nom de l'utilisateur postant le commentaire
     * @param recipe nom de la recette commentée
     * @throws Exception si grade n'est pas inclus dans [0 , 5]
     */
    public void addGrade(int grade, String username, String recipe) throws Exception {
        if(grade>5||grade<0)
            throw new Exception("grade hors des limites");
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(AVIS_TABLE,
                new String[]{AVIS_AUTHOR_COLUMN}, //on prend une seule colonne car on n'est interessé que par le nombre de lignes
                AVIS_AUTHOR_COLUMN+" ='"+username+"'AND "+AVIS_RECETTE_COLUMN+" ='"+recipe+"'",
                null,
                null,
                null,
                null);

        if(cursor.getCount()==0) {//si il n'y as pas d'avis de l'utilsateur
            db.execSQL(String.format("INSERT INTO %1$s(%2$s,%3$s,%4$s,%5$s) VALUES ('%6$s','%7$s','%8$s',NULL);",
                    AVIS_TABLE,AVIS_AUTHOR_COLUMN,AVIS_RECETTE_COLUMN,AVIS_NOTE_COLUMN,AVIS_COMMENTAIRE_COLUMN,username,recipe,grade));
        }
        else{//si il ya déja un avis créé
            db.execSQL(String.format("UPDATE %1$s SET %2$s = %3$s WHERE %4$s = %5$s AND %6$s = %7$s"
                    ,AVIS_TABLE,AVIS_NOTE_COLUMN,grade,AVIS_AUTHOR_COLUMN,username,AVIS_RECETTE_COLUMN,recipe));
        }
    }
    public int getIndex(String[] array, String elem) {
        int size=array.length;
        for(int i=0; i<size; i++) {
            if(elem==array[i])
                return i;
        }
        return -1;
    }

    public String getStepNameColumn(String recette_name, int step_number){
        Log.d(this.TAG, "Rentree dans get step_name");
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(this.TAG, "db en lecture");
        Cursor cursor = db.query(STEP_TABLE,
                new String[]{STEP_NAME_COLUMN, STEP_NUMBER_COLUMN},
                RECETTE_NAME_COLUMN + " ='"+recette_name+"'",
                null, null, null,null);
        Log.d(this.TAG, "Cursor cree");
        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                if (step_number == cursor.getInt(cursor.getColumnIndex(STEP_NUMBER_COLUMN))){
                    String res = cursor.getString(cursor.getColumnIndex(STEP_NAME_COLUMN));

                    cursor.close();

                    return res;
                }

                cursor.moveToNext();
            }
        }
        cursor.close();

        return "Pas de nom d'étape disponible";
    }

    public String getStepExplanationColumn(String recette_name, int step_number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STEP_TABLE,
                new String[]{STEP_EXPLANATION_COLUMN,STEP_NUMBER_COLUMN},
                RECETTE_NAME_COLUMN + " ='"+recette_name+"'",
                null, null, null, null);

        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                if (step_number == cursor.getInt(cursor.getColumnIndex(STEP_NUMBER_COLUMN))){
                    String res = cursor.getString(cursor.getColumnIndex(STEP_EXPLANATION_COLUMN));

                    cursor.close();

                    return res;
                }

                cursor.moveToNext();
            }
        }
        cursor.close();

        return "Pas d'explication d'étape disponible";
    }

    public int getNbStep(String recette_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STEP_TABLE,
                new String[]{STEP_EXPLANATION_COLUMN},
                RECETTE_NAME_COLUMN + " ='"+recette_name+"'",
                null, null, null,null);
        int count = cursor.getCount();

        cursor.close();

        return count-1;
    }

    public List<String> getTypes(){
        List<String> types = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  DISTINCT "+RECETTE_TYPE_COLUMN+" FROM " +RECETTE_TABLE+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Cursor cursor = db.query(RECETTE_TABLE,new String[]{RECETTE_TYPE_COLUMN},null,null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                types.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning types
        return types;
    }

    public List<String> getSubTypes(){
        List<String> sub_types = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  DISTINCT "+RECETTE_SOUS_TYPE_COLUMN+" FROM " +RECETTE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Cursor cursor = db.query(RECETTE_TABLE,new String[]{RECETTE_SOUS_TYPE_COLUMN},null,null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                sub_types.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning types
        return sub_types;
    }
}

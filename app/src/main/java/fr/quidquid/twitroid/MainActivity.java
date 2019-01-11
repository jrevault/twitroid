package fr.quidquid.twitroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import fr.quidquid.twitroid.fragments.TwitdroidSearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar main_toolbar = findViewById( R.id.main_toolbar );
        setSupportActionBar( main_toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( false );

        Toast.makeText( this, "Welcome to my andronode app", Toast.LENGTH_SHORT ).show();

        load_fragment( TwitdroidSearchFragment.class, getIntent().getExtras() );
    }

    public void load_fragment( Class clazz ) {
        load_fragment( clazz, new Bundle() );
    }
    public void load_fragment( Class clazz, Bundle args ) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment current = getSupportFragmentManager().findFragmentById( R.id.fragment_container );
            Fragment fragment = ( Fragment ) clazz.newInstance();
            String fragment_tag = fragment.getClass().getName();
            fragment.setArguments( args );
            if ( current == null ) {
                transaction.add( R.id.fragment_container, fragment, fragment_tag );
            }
            else {
                transaction.replace( R.id.fragment_container, fragment, fragment_tag );
                transaction.addToBackStack( fragment_tag );
            }
            transaction.commit();
        }
        catch ( Exception e ) {
            e.printStackTrace();
            // you should treat your exceptions with respect, you can't just let them go !
            // ... except when I'm doing tutorials...
        }
    }
}

package com.muddaflappers.hacktech;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	//Jerry is gonna eat all the fried chicken in America
	//Kevin likes beef
	//Jerry ate spaghetti for christmas
	//KEVIN EAT FOOD
	//HE ALSO LIKES SPAGHETTI
	//Justin test

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toast t = Toast.makeText(getApplicationContext(), "wheeee", Toast.LENGTH_LONG);
      t.show();
      
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}

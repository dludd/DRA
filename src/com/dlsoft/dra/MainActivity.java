package com.dlsoft.dra;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Hauptklasse und einzige Activity der DRA-Dienstreiseabrechung-Anwendung.
 * 
 * Die Anwendung soll die für eine Dienstreiseabrechnung relevanten Daten 
 * einfach erfassen lassen und diese dann per email verschicken
 * 
 * @author Danilo Ludwig
 * 2013
 *
 */

public class MainActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// die Startmethode, die beim ersten Start der Activity aufgerufen wird,
		// und auch bei Änderung der Orientierung (horizontal/vertikal).
		// Hier wird das GUI-Layout definiert und die Event-Handler mit den GUI Elementen verknüpft.	

		super.onCreate(savedInstanceState);
		//GUI aus dem XML File erzeugen
		setContentView(R.layout.activity_main);

		//verknüpfen der GUI Elemente mit den entsprechenden Eventhandles

		// Button-Klick-Handler an den send email Button setzen
		Button send_email_button = (Button) findViewById(R.id.button_send_email);				
		send_email_button.setOnClickListener(onsendemailButtonClick);
		// Button-Klick-Handler an den jetzt Button für die Startzeit setzen
		Button now_start_button = (Button) findViewById(R.id.button_now_start);				
		now_start_button.setOnClickListener(onnowstartButtonClick);
		// Button-Klick-Handler an den jetzt Button für die Endezeit setzen
		Button now_stop_button = (Button) findViewById(R.id.button_now_stop);				
		now_stop_button.setOnClickListener(onnowstopButtonClick);
		
		//Focus auf das erste Eingabefeld setzen
		//Workaround für den Bug, das die Anweisung <requestFocus /> im GUI-XML nicht geht
		EditText firsteditcontrol = (EditText)findViewById(R.id.editText_start_date);
		firsteditcontrol.requestFocus();
	}

	//send email Button-Klick-Handler
	final View.OnClickListener onsendemailButtonClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText mEdit;	
			String start_date;
			String start_time;
			int start_milage;
			String stop_date;
			String stop_time;
			int stop_milage;
			String travel_to;
			String travel_why;
			String s;
			
			// Startdaten aus den Controls holen
			mEdit = (EditText)findViewById(R.id.editText_start_date);
			start_date = mEdit.getText().toString();
			mEdit = (EditText)findViewById(R.id.editText_start_time);
			start_time = mEdit.getText().toString();
			mEdit = (EditText)findViewById(R.id.editText_start_milage);
			s = mEdit.getText().toString();
			if ("".equals(s)) 
				start_milage = 0;
			else
				start_milage = Integer.parseInt(s);			
			
			// Stopdaten aus den Controls holen
			mEdit = (EditText)findViewById(R.id.editText_stop_date);
			stop_date = mEdit.getText().toString();
			mEdit = (EditText)findViewById(R.id.editText_stop_time);
			stop_time = mEdit.getText().toString();
			mEdit = (EditText)findViewById(R.id.editText_stop_milage);
			s = mEdit.getText().toString();
			if ("".equals(s))
				stop_milage = 0;
			else
				stop_milage = Integer.parseInt(s);
			
			// Reiseziel und Grund aus den Controls holen
			mEdit = (EditText)findViewById(R.id.editText_travel_to);
			travel_to = mEdit.getText().toString();
			mEdit = (EditText)findViewById(R.id.editText_travel_why);
			travel_why = mEdit.getText().toString();

			// email-Inhalt per Intent setzen
			Intent sendEmailIntent = new Intent(android.content.Intent.ACTION_SEND);
			sendEmailIntent.setType("text/html");
			sendEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"emailAddress"});  
			s = "Dienstreise nach " + travel_to + " vom " + start_date + " bis " + stop_date;
			sendEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, s);
			s = "Dienstreise nach " + travel_to + "\r\n";
			if (!"".equals(s)) s = s + "Grund: " + travel_why + "\r\n";
			s = s + "Start: " + start_date + " " + start_time + "\r\n";
			s = s + "Ende:  " + stop_date + " " + stop_time + "\r\n";
			s = s + "km:    " + Integer.toString(start_milage) + " bis " + Integer.toString(stop_milage) + " (Summe: " + Integer.toString(stop_milage-start_milage) + ")\r\n";
			sendEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
			// email Programm auswählen lassen und den ganzen Kram abschicken (lassen)
			startActivity(Intent.createChooser(sendEmailIntent, "DRA per email senden..."));
		}
	}
	;
	// der jetzt Start Button-Klick-Handler
	final View.OnClickListener onnowstartButtonClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setnow_incontrols(R.id.editText_start_date, R.id.editText_start_time);
		}
	}
	;
	// der jetzt Stop Button-Klick-Handler
		final View.OnClickListener onnowstopButtonClick=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setnow_incontrols(R.id.editText_stop_date, R.id.editText_stop_time);
			}
		}
	;

	private void setnow_incontrols(int date_control, int time_control ){
		// setzt die aktuelle Datum und Uhrzeit in die Controls mit den übergebenen id's 
		EditText editText;
		DateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy");
		DateFormat zeitFormat = new SimpleDateFormat("HH:mm");
		// aktuelle Zeit ermitteln
		Date datumzeit = new Date();
		// Datum und Uhrzeit in die entsprechenden Eingabefelder setzen
		editText = (EditText)findViewById(date_control);
		editText.setText(datumsFormat.format(datumzeit));
		editText = (EditText)findViewById(time_control);
		editText.setText(zeitFormat.format(datumzeit));
	}
}
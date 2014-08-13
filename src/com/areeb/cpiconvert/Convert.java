package com.areeb.cpiconvert;


import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Convert extends Activity {

	double cpi;
	
	String percentage;
	
	EditText cpiInput;
	TextView percText;
	ImageView button;
	
	Shader bigTextShader;
	Shader textShader;
	
	TextView circleView;
	
	LinearLayout hiddenLayout;
	TextView remarkText;
	TextView divisionText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_convert);
		
		hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
		hiddenLayout.setVisibility(View.GONE);
		
		remarkText = (TextView) findViewById(R.id.remarkText);
		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		remarkText.setTypeface(tf);
		
		divisionText = (TextView) findViewById(R.id.divisionText);
		
		circleView = (TextView) findViewById(R.id.circleView);
		
		percText = (TextView) findViewById(R.id.percText);
		cpiInput = (EditText) findViewById(R.id.cpiInput);
		cpiInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence cptemp, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

				String str = String.valueOf(cptemp);

				if (!str.equals("")) {

					Double in = Double.parseDouble(str);

					if (in > 10) {

						cpiInput.setError("Enter value under 10");
						cpiInput.setText("10");
						cpiInput.setSelection(2);

					}

				}

			}
			
			
			
		});
		
		bigTextShader = new LinearGradient(0, 0, 0, 3*percText.getPaint().getTextSize(), Color.parseColor("#eeeeee"), Color.parseColor("#adadad"), TileMode.CLAMP);
		percText.getPaint().setShader(bigTextShader);

		
	}
	
	@SuppressLint("NewApi")
	public String getPerc(){
		
		cpi = Double.parseDouble(cpiInput.getText().toString());
		double percentage = (20*Math.pow(cpi, 3) - 380*Math.pow(cpi, 2) + 2725*cpi -1690)/84;
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.DOWN);
		
		return df.format(percentage);
	}
	
	

	@SuppressLint("DefaultLocale")
	public void calculate(View view){
	
		try{
			
			percentage = getPerc();
		
		} catch (NumberFormatException e){
			
			Toast.makeText(this, "Enter valid CPI!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return;
		}
		
		if (Double.parseDouble(percentage) > 90){
		
			String temp = "Whatever you try, you won\'t get above 90%";
			percText.setTextSize(15f);
			SpannableString perc = new SpannableString(temp);
			perc.setSpan(new RelativeSizeSpan(3f), 38, temp.length() - 1, 0);
			percText.setText(perc);
			
			hiddenLayout.setVisibility(View.VISIBLE);
			circleView.setText("O");
			circleView.setBackgroundResource(R.drawable.circle_red);
			remarkText.setText("0 < CPI < 10");
			remarkText.setTextColor(Color.parseColor("#ff5858"));
			divisionText.setText("Enter valid value!");
		
		} else if (Double.parseDouble(percentage) < 0){
			
			String temp = "You scored " + 0 + "%";
			SpannableString perc = new SpannableString(temp);
			perc.setSpan(new RelativeSizeSpan(3f), 11, temp.length() - 1, 0);
			percText.setText(perc);
			
			printGrade(percentage);
			
		} else {
			
			String temp = "You scored " + percentage + "%";
			SpannableString perc = new SpannableString(temp);
			perc.setSpan(new RelativeSizeSpan(3f), 11, temp.length() - 1, 0);
			percText.setText(perc);
			
			printGrade(percentage);
			
		}
		
	}

	private void printGrade(String perc) {
		
		hiddenLayout.setVisibility(View.VISIBLE);
		
		Double per = Double.parseDouble(perc);
		
		if(per >= 75){
			
			circleView.setText("A");
			circleView.setBackgroundResource(R.drawable.circle_purple);
			remarkText.setText("Outstanding");
			remarkText.setTextColor(Color.parseColor("#7f1fea"));
			
		} else if (per >= 60 && per < 75 ){
			
			circleView.setText("B");
			circleView.setBackgroundResource(R.drawable.circle_blue);
			remarkText.setText("Very Good");
			remarkText.setTextColor(Color.parseColor("#33b5e5"));
			
		} else if (per >= 45 && per < 60 ){
			
			circleView.setText("C");
			circleView.setBackgroundResource(R.drawable.circle_green);
			remarkText.setText("Good");
			remarkText.setTextColor(Color.parseColor("#0cc4a1"));
			
		} else if (per >= 35 && per < 45 ){
			
			circleView.setText("D");
			circleView.setBackgroundResource(R.drawable.circle_yellow);
			remarkText.setText("Satisfactory");
			remarkText.setTextColor(Color.parseColor("#ffcc00"));
			
		} else if (per < 35){
			
			circleView.setText("E");
			circleView.setBackgroundResource(R.drawable.circle_red);
			remarkText.setText("Fail");
			remarkText.setTextColor(Color.parseColor("#ff5858"));
			
			//Toast.makeText(this, "Abe kuchh padhta nahi hai kya?", Toast.LENGTH_LONG).show();
			
		}
		
		if (cpi >= 8.5){
			
			divisionText.setText("1st Division (Honours)");
			
		} else if (cpi >=6.5 && cpi < 8.5){
			
			divisionText.setText("1st Division");
			
		} else if (cpi < 6.5){
			
			divisionText.setText("2nd Division");
		}
		
	}

	

}

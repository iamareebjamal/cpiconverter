package com.areeb.cpiconvert;


import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nineoldandroids.animation.ObjectAnimator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Convert extends Activity {

	double cpi;
	
	String percentage;
	
	EditText cpiInput;
	TextView percText, remarkText, divisionText, circleView;
	ImageView button, about;
	
	Shader bigTextShader, textShader;
	GradientDrawable circle;
	Typeface tf;
	
	LinearLayout hiddenLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_convert);
		
		
		
		
		hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
		hiddenLayout.setVisibility(View.GONE);
		
		remarkText = (TextView) findViewById(R.id.remarkText);
		tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		remarkText.setTypeface(tf);
		
		button = (ImageView) findViewById(R.id.imageView1);
		divisionText = (TextView) findViewById(R.id.divisionText);
		percText = (TextView) findViewById(R.id.percText);
		cpiInput = (EditText) findViewById(R.id.cpiInput);
		about = (ImageView) findViewById(R.id.aboutButton);
		
		circleView = (TextView) findViewById(R.id.circleView);
		circle = ((GradientDrawable) circleView.getBackground());
		
		
		
		cpiInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@SuppressLint("NewApi")
			@Override
			public void onTextChanged(CharSequence cptemp, int arg1, int arg2,
					int arg3) {
				
				
				String str = String.valueOf(cptemp);
				Double in;

				if (!str.equals("")) {
					
					try{
						in = Double.parseDouble(str);
					} catch (NumberFormatException e){
						cpiInput.setError("Enter valid value!");
						return;
					}
					

					if (in > 10) {

						if(Build.VERSION.SDK_INT>=16)
							cpiInput.setBackground(getResources().getDrawable((R.drawable.textfield_red_mtrl)));
						cpiInput.setError("Enter value under 10");
						cpiInput.setText("10");
						cpiInput.setSelection(2);

					} else if(in < 10) {
						if(Build.VERSION.SDK_INT>=16)
							cpiInput.setBackground(getResources().getDrawable((R.drawable.edit_text)));
					}
						
					

				} 

			}
			
			
			
		});
		
		bigTextShader = new LinearGradient(0, 0, 0, 3*percText.getPaint().getTextSize(), Color.parseColor("#eeeeee"), Color.parseColor("#adadad"), TileMode.CLAMP);
		percText.getPaint().setShader(bigTextShader);
		
		about.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View img) {
				View view = getLayoutInflater().inflate(R.layout.layout_about, null);
				
				TextView tv = (TextView) view.findViewById(R.id.textView2);
				Date date = new Date();
				SimpleDateFormat mn = new SimpleDateFormat("MM", Locale.US);
				SimpleDateFormat dy = new SimpleDateFormat("dd", Locale.US);
				SimpleDateFormat yr = new SimpleDateFormat("yyyy", Locale.US);
				int month = Integer.valueOf(mn.format(date));
				int day = Integer.valueOf(dy.format(date));
				int age = Integer.valueOf(yr.format(date)) - 1996;
				
				if (month<10 && day<15)
					age--;
				
				tv.setText(getResources().getString(R.string.about_dev, age));
				
            	AlertDialog.Builder dialog = new AlertDialog.Builder(Convert.this);
            	dialog.setView(view);
            	dialog.show();
            	
			}
		});
		
		about.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		
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
	
		hideKeyboard();
		
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
			circle.setColor(Color.parseColor("#ff5858"));;
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
			
			
			
			ObjectAnimator anim3 = ObjectAnimator.ofFloat(percText, "translationX", -2000,0);
			anim3.setInterpolator(new DecelerateInterpolator());
			anim3.start();
			ObjectAnimator anim4 = ObjectAnimator.ofFloat(percText, "alpha", 0,1f);
			anim4.setDuration(1000);
			anim4.setInterpolator(new DecelerateInterpolator());
			anim4.start();
			
			
			printGrade(percentage);
			
		}
		
	}

	private void printGrade(String perc) {
		

		
		hiddenLayout.setVisibility(View.VISIBLE);
		
		Double per = Double.parseDouble(perc);
		
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(circleView, "scaleX", 0,1f);
		anim1.setInterpolator(new BounceInterpolator());
		anim1.setDuration(1000);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(circleView, "scaleY", 0,1f);
		anim2.setInterpolator(new BounceInterpolator());
		anim2.setDuration(1000);
		anim1.start();
		anim2.start();
		
		
		
		
		if(per >= 75){
			
			circleView.setText("A");
			circle.setColor(Color.parseColor("#7f1fea"));;
			remarkText.setText("Outstanding");
			remarkText.setTextColor(Color.parseColor("#7f1fea"));
			
		} else if (per >= 60 && per < 75 ){
			
			circleView.setText("B");
			circle.setColor(Color.parseColor("#33b5e5"));;
			remarkText.setText("Very Good");
			remarkText.setTextColor(Color.parseColor("#33b5e5"));
			
		} else if (per >= 45 && per < 60 ){
			
			circleView.setText("C");
			circle.setColor(Color.parseColor("#0cc4a1"));;
			remarkText.setText("Good");
			remarkText.setTextColor(Color.parseColor("#0cc4a1"));
			
		} else if (per >= 35 && per < 45 ){
			
			circleView.setText("D");
			circle.setColor(Color.parseColor("#ffcc00"));;
			remarkText.setText("Satisfactory");
			remarkText.setTextColor(Color.parseColor("#ffcc00"));
			
		} else if (per < 35){
			
			circleView.setText("E");
			circle.setColor(Color.parseColor("#ff5858"));;
			remarkText.setText("Fail");
			remarkText.setTextColor(Color.parseColor("#ff5858"));
			
			Toast.makeText(this, "Abe kuchh padh bhi liya kar!", Toast.LENGTH_LONG).show();
			
		}
		
		if (cpi >= 8.5){
			
			divisionText.setText("1st Division (Honours)");
			
		} else if (cpi >=6.5 && cpi < 8.5){
			
			divisionText.setText("1st Division");
			
		} else if (cpi < 6.5){
			
			divisionText.setText("2nd Division");
		}
		
		
		ObjectAnimator anim3 = ObjectAnimator.ofFloat(remarkText, "translationY", 500,0);
		anim3.setInterpolator(new DecelerateInterpolator());
		ObjectAnimator anim4 = ObjectAnimator.ofFloat(remarkText, "alpha", 0,1f);
		anim3.setInterpolator(new DecelerateInterpolator());
		anim4.setDuration(1000);
		anim4.start();
		anim3.start();
		ObjectAnimator anim5 = ObjectAnimator.ofFloat(divisionText, "translationY", 500,0);
		anim5.setInterpolator(new DecelerateInterpolator());
		ObjectAnimator anim6 = ObjectAnimator.ofFloat(divisionText, "alpha", 0,1f);
		anim6.setInterpolator(new DecelerateInterpolator());
		anim6.setDuration(1000);
		anim5.start();
		anim6.start();
		
	}

	
	public void iamareebjamal(View view){
		
		String[] choices = {"Facebook", "XDA", "Google+", "Twitter"};


		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Contact me");
		builder.setItems(choices , new DialogInterface.OnClickListener(){


				public void onClick(DialogInterface dialog , int which)
				{
					
					String string = null;

					switch (which)
					{

						case (0) :
							string = "http://www.facebook.com/iamareebjamal";
							break;
						case (1) :
							string = "http://forum.xda-developers.com/member.php?u=4782403";
							break;
						case (2) :
							string = "http://plus.google.com/+areebjamaiam";
							break;
						case (3) :
							string = "http://www.twitter.com/iamareebjamal";
							break;


					}
					
					if (!string.equals(null))
					{
						go(string);
					}
				}
			});
		builder.show();
		
	}
	
	public void hide(View... views){
		
		for(int i = 0; i < views.length; i++)
			views[i].setVisibility(View.GONE);
		
	}
	
	public void show(View... views){
		
		for(int i = 0; i < views.length; i++)
			views[i].setVisibility(View.VISIBLE);
		
	}
		
	public void showMessage(Context ctx, String message){
		
		TextView tv = new TextView(ctx);
		tv.setText(message);
		tv.setTextColor(Color.parseColor("#33b5e5"));
		tv.setTypeface(tf);
		tv.setTextSize(30f);
		Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, pixels(-150));
		toast.setView(tv);
		toast.show();
	}	
	
	private void hideKeyboard() {
	    InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

	    // check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}

	private int pixels(float dip){
	    float DP = getResources().getDisplayMetrics().density;
	    return Math.round(dip * DP);
	}
	
	public void go(String url) {

		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));

		try {

			startActivity(intent);

		} catch (ActivityNotFoundException e) {

			Toast.makeText(this, "No browser found!", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();

		}

	}
	
	
	
	/* Don't tell my mom I made this :p */
	int freaky_touches = 0;
	public void shhh(View view) {
		
		freaky_touches++;
		if(freaky_touches>10){
			freaky_touches=0;
			

			final Context ctx = getApplicationContext();
			
			try {
				
				AssetFileDescriptor afd = getAssets().openFd("applause.mp3");
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor());
				player.prepare();
				player.start();
				
		    } catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		   
			
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.parentView);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pixels(200), pixels(200));
			//params.topMargin = x; + circleView.getHeight()/pixels(5); //Dirty fucking fix for centering the image on view
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			final ImageView iv = new ImageView(ctx);
			iv.setImageResource(R.drawable.grats_fag);
			iv.setLayoutParams(params);
			rl.addView(iv);
			
			
			RelativeLayout.LayoutParams imgparams = new RelativeLayout.LayoutParams(pixels(100), pixels(100));
			imgparams.addRule(RelativeLayout.CENTER_IN_PARENT);
			final ImageView img = new ImageView(ctx);
			img.setImageResource(R.drawable.claps);
			img.setLayoutParams(imgparams);
			rl.addView(img);
			
			int currentRotation = 0;
            final Animation anim = new RotateAnimation(0, (360*4), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            currentRotation = (currentRotation + 45) % 360;
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(10000);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setFillEnabled(true);
            anim.setFillAfter(true);
            iv.startAnimation(anim);
			
			hide(remarkText, divisionText, cpiInput, percText, button, circleView);
			showMessage(ctx, "Congratulations!");
			
			//TODO Read with caution
			/* To whomsoever it may concern,
			 * the reader of the code, to you
			 * I apologize in advance, but I can't explain what I have done below 
			 */
			
			Runnable run1 = new Runnable() {
				
				@Override
				public void run() {
					showMessage(ctx, "You found the Surprise!");
					img.setImageResource(R.drawable.great);
					
					Runnable run2 = new Runnable() {
						
						@Override
						public void run() {
							showMessage(ctx, "Loading the prize... Please Wait!");
							img.setImageResource(R.drawable.wait);
							
							Runnable run3 = new Runnable() {
								
								@Override
								public void run() {
									showMessage(ctx, "You got a...");
									
									Runnable run4 = new Runnable() {
										
										@Override
										public void run() {
											showMessage(ctx, "Babaji ka Thullu!");
											img.setImageResource(R.drawable.bebaji);
											
											Runnable run5 = new Runnable() {
												
												@Override
												public void run() {
													showMessage(ctx, "Hue Hue Hue");
													showMessage(ctx, "Now get Rekt...");
													img.setImageResource(R.drawable.pigger);
													
													Runnable run6 = new Runnable() {
														
														@Override
														public void run() {
															iv.clearAnimation();
															hide(iv, img);
															iv.setImageResource(R.drawable.more);
															show(remarkText, divisionText, cpiInput, percText, button, circleView);
														}
													};
													
													new Handler().postDelayed(run6, 8000);
												}
												
											};
											
											new Handler().postDelayed(run5, 4000);
										}
										
									};
									
									new Handler().postDelayed(run4, 6000);
								}
							};
							
							new Handler().postDelayed(run3, 2000);
						}
					};
					
					new Handler().postDelayed(run2, 4000);
				}
			};
			
			new Handler().postDelayed(run1, 4000);
			
		}
	}


}

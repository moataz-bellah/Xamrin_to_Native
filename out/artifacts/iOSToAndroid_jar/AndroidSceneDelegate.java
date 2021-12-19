import android.content.Intent; 
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity; 
import android.view.View.OnClickListener;
import android.view.View;

public class Main2Activity extends AppCompatActivity{
TextView timetxt ,TextView scoretxt ;
Button exit ,Button playAgain ;
@Override  
void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState); 
 setContentView(R.layout.activity_main2);
showScoreAndTime();

private void mainWindow() 
{
Intent intent = new Intent(this,MainActivity.class);
 startActivity(intent);

}
private void showScoreAndTime() 
{
String score  = 10;
String time  = 5;
 if(int(time) < 59) {
scoretxt.setText(scoretxt.getText() + " " + score);
timetxt.setText(timetxt.getText() + " " + time + "secs");

}
else{
scoretxt.setText(scoretxt.getText() + " " + "0");
timetxt.setText("You didn't finish in time!");

}

}
Button targetMethod0;
targetMethod0 = (Button) findViewById(R.id.targetMethod0);
targetMethod0.setOnClickListener(new View.OnClickListener() {  
@Override  
            public void onClick(View view) 
{
finish();
System.exit(0);

} 
 });
Button targetMethod1;
targetMethod1 = (Button) findViewById(R.id.targetMethod1);
targetMethod1.setOnClickListener(new View.OnClickListener() {  
@Override  
            public void onClick(View view) 
{
mainWindow();
finish();

} 
 });

}

}

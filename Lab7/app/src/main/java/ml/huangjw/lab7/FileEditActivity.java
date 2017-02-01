package ml.huangjw.lab7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEditActivity extends AppCompatActivity {

  private final String FILENAME = "Lab7.txt";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_file_edit);
    init();
  }

  private void init() {
    final EditText et = (EditText) findViewById(R.id.write2file);
    Button save = (Button) findViewById(R.id.save), load = (Button) findViewById(R.id.load), clear = (Button) findViewById(R.id.clear);

    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
          fos.write(et.getText().toString().getBytes());
          fos.close();
          showToast(R.string.success2);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    load.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try (FileInputStream fis = openFileInput(FILENAME)) {
          byte[] contents = new byte[fis.available()];
          fis.read(contents);
          fis.close();
          et.setText(new String(contents));
          showToast(R.string.success1);
        } catch (IOException ex) {
          showToast(R.string.error4);
          ex.printStackTrace();
        }
      }
    });

    clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        et.setText("");
      }
    });
  }

  private void showToast(int str) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
  }
}
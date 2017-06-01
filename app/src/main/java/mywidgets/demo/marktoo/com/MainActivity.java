package mywidgets.demo.marktoo.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.marktoo.widget.NineBlockLockView;

public class MainActivity extends AppCompatActivity implements NineBlockLockView.OnLockListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((NineBlockLockView)findViewById(R.id.nineLock)).setLockListener(this);
    }

    @Override
    public void onPassSuccess(String passCode) {
        Toast.makeText(this, "密码:"+passCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPassFailed() {
        Toast.makeText(this, "密码不符合规则", Toast.LENGTH_SHORT).show();
    }
}

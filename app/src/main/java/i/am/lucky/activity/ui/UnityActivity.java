package i.am.lucky.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thejoyrun.router.RouterActivity;

import butterknife.ButterKnife;
import i.am.lucky.R;

/*
 * @author Cazaea
 * @time 2017/11/19 15:36
 * @mail wistorm@sina.com
 *
 *  .--,       .--,
 * ( (  \.---./  ) )
 *  '.__/o   o\__.'
 *     {=  ^  =}
 *      >  -  <
 *     /       \
 *    //       \\
 *   //|   .   |\\
 *   "'\       /'"_.-~^`'-.
 *      \  _  /--'         `
 *    ___)( )(___
 *   (((__) (__)))
 *
 *   一直相信：越努力，越幸运；越沟通，越亲近。
 */
@RouterActivity("unity")
public class UnityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity);
        ButterKnife.bind(this);

    }

}

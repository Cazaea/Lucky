package i.am.lucky.activity.scan;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.clipboard.ClipboardInterface;
import com.google.zxing.client.android.share.BaseShareActivity;
import com.thejoyrun.router.RouterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import i.am.lucky.R;

/**
 * @author Cazaea
 * @time 2017/12/14 11:05
 * @mail wistorm@sina.com
 */
@RouterActivity("create")
public class CreateQRCodeActivity extends BaseShareActivity implements View.OnKeyListener {

    @BindView(R.id.btn_share_app)
    Button btnShareApp;
    @BindView(R.id.btn_share_bookmark)
    Button btnShareBookmark;
    @BindView(R.id.btn_share_contact)
    Button btnShareContact;
    @BindView(R.id.btn_share_clipboard)
    Button btnShareClipboard;
    @BindView(R.id.share_text_view)
    EditText shareTextView;
    @BindView(R.id.btn_share_text)
    Button btnShareText;
    @BindView(R.id.my_qrcode)
    ImageView myQRCode;
    @BindView(R.id.contents_text_view)
    TextView contentsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_code);
        ButterKnife.bind(this);
        shareTextView.setOnKeyListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnShareClipboard.setEnabled(ClipboardInterface.hasText(this));
        if (contactsBitmap != null) {
            myQRCode.setImageBitmap(contactsBitmap);
        }
        if (bookMarkBitmap != null) {
            myQRCode.setImageBitmap(bookMarkBitmap);
        }
        if (appBitmap != null) {
            myQRCode.setImageBitmap(appBitmap);
        }
        if (textContent != null && !textContent.isEmpty()){
            contentsTextView.setText(textContent);
        }
    }

    @OnClick(R.id.btn_share_app)
    public void onBtnShareAppClicked() {
        shareAPP();
    }

    @OnClick(R.id.btn_share_bookmark)
    public void onBtnShareBookmarkClicked() {
        shareBookMark();
    }

    @OnClick(R.id.btn_share_contact)
    public void onBtnShareContactClicked() {
        shareContact();
    }

    @OnClick(R.id.btn_share_clipboard)
    public void onBtnShareClipboardClicked() {
        // Should always be true, because we grey out the clipboard button in onResume() if it's empty
        String text = String.valueOf(ClipboardInterface.getText(CreateQRCodeActivity.this));
        if (text.isEmpty()) {
            return;
        }
        myQRCode.setImageBitmap(shareClipboard(text));
        contentsTextView.setText(text);
    }

    @OnClick(R.id.btn_share_text)
    public void onBtnShareTextClicked() {
        String text = shareTextView.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        myQRCode.setImageBitmap(shareText(text));
        contentsTextView.setText(text);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            String text = ((TextView) view).getText().toString();
            if (!text.isEmpty()) {
                myQRCode.setImageBitmap(shareText(text));
                contentsTextView.setText(text);
            }
            return true;
        }
        return false;
    }

}

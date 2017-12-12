package specup.fanmind.common.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import specup.fanmind.R;


public class CustomDialog2 extends Dialog {

	String mLeftText = null;
	String mRightText = null;

	Context mContext;
    CustomDialog2 customDialog;
	int mSize = 0;

    EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        lpWindow.softInputMode =  WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getWindow().setAttributes(lpWindow);



        setContentView(R.layout.custom_dialog2);

        setLayout();
        setTitle(mTitle);
        setContent(mContent);

        setClickListener(mLeftClickListener , mRightClickListener);
	}

	public CustomDialog2(Context context) {

        super(context , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        customDialog = this;
    }


    public CustomDialog2(Context context, String title, String content, View.OnClickListener singleListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mContext = context;
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = singleListener;
        customDialog = this;
    }

    public CustomDialog2(Context context, String title, String content, View.OnClickListener singleListener, String lBtnText) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        this.mTitle = title;
        this.mLeftClickListener = singleListener;
        this.mContent = content;
        this.mLeftText = lBtnText;
        customDialog = this;
    }

    public CustomDialog2(Context context, String title, String content,
                         View.OnClickListener leftListener, View.OnClickListener rightListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        customDialog = this;
    }

    private void setTitle(String title){
        mTitleView.setText(title);
        if(mSize!=0){
        	mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mSize);
        }
    }

    public CustomDialog2(Context context, String title, String content, View.OnClickListener leftListener, View.OnClickListener rightListener, String lBtnText, String rBtnText) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftText = lBtnText;
        this.mRightText = rBtnText;
    }

    public CustomDialog2(Context context, String title, String content,
                         View.OnClickListener leftListener, View.OnClickListener rightListener, String lBtnText, String rBtnText, int size) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftText = lBtnText;
        this.mRightText = rBtnText;
        mSize = size;
    }

    public CustomDialog2 getCustomDialog(){
        return customDialog;
    }
    
    private void setContent(String content){
        mContentView.setText(content);
    }
     
    private void setClickListener(View.OnClickListener left , View.OnClickListener right){
        if(left!=null && right!=null){
        	if(!TextUtils.isEmpty(mLeftText))
        		mLeftView.setText(mLeftText);
        	if(!TextUtils.isEmpty(mRightText))
            mRightView.setText(mRightText);
        	mLeftButton.setOnClickListener(left);
            mRightButton.setOnClickListener(right);
        }else if(left!=null && right==null){ //�̱� ��ư.
        	if(!TextUtils.isEmpty(mLeftText))
            mLeftView.setText(mLeftText);
        	mLeftButton.setBackgroundResource(R.drawable.pop_btn03);
        	mLeftButton.setOnClickListener(left);
        	mRightButton.setVisibility(View.GONE);
        }else {
             
        }
    }
     
    private TextView mTitleView, mLeftView, mRightView;
    private TextView mContentView;
    private RelativeLayout mLeftButton;
    private RelativeLayout mRightButton;
    private String mTitle;
    private String mContent;
     
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;


    public String getEditTextContent() {
        return editText.getText().toString();
    }

    /*
         * Layout
         */
    private void setLayout(){
    	//�?��?�트
//    	Typeface font = GlobalValues.getFont(mContext);
    	
//    	Fonts font = new Fonts(mContext);
//    	
        mTitleView = (TextView) findViewById(R.id.pop_tv01);
        mLeftView = (TextView) findViewById(R.id.pop_tv03);
        mRightView =  (TextView) findViewById(R.id.pop_tv04);
//        font.setBareunDotum2Text(mContext, mTitleView);
        mContentView = (TextView) findViewById(R.id.pop_tv02);
//        font.setBareunDotum2Text(mContext, mContentView);
        mLeftButton = (RelativeLayout) findViewById(R.id.pop_layout03);
//        font.setBareunDotum2Text(mContext, mLeftButton);
        mRightButton = (RelativeLayout) findViewById(R.id.pop_layout04);
//        font.setBareunDotum2Text(mContext, mRightButton);
        RelativeLayout mLayout =(RelativeLayout)findViewById(R.id.pop_layoutbasic);
        mLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
        editText  = (EditText)findViewById(R.id.findpw_et01);
    }

}

package specup.fanmind.common.twitter.auto.upload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import specup.fanmind.R;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCore extends Activity implements View.OnClickListener
{
	private Twitter g_Twitter;
	private RequestToken g_reqToken;
	private AccessToken g_AccessToken;
	private Button mBtnLogin, mBtnFeed, mBtnLogout;
	private EditText mEtContent;
  
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_main);
		    
		mEtContent = (EditText) findViewById(R.id.etContent);
		    
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtnFeed = (Button) findViewById(R.id.btnFeed);
		mBtnLogout = (Button) findViewById(R.id.btnLogout);
		    
		mBtnLogin.setOnClickListener(this);
		mBtnFeed.setOnClickListener(this);
		mBtnLogout.setOnClickListener(this);
	}
  
  	// 버튼 클릭 이벤트 처리
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		  case R.id.btnLogin: // Twitter login
		    login();
		    break;
		  case R.id.btnFeed:  // Twitter에 글쓰기
		    write();
		    break;
		  case R.id.btnLogout: // Twitter logout
		    logout();
		    break;
		  default:
		    break;
		}
	}
  
	private Configuration getConfiguration(String apiKey)
	{
		return new ConfigurationBuilder().setMediaProviderAPIKey(apiKey).build();
	}
  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		    
		// 액티비티가 정상적으로 종료되었을 경우
		if(resultCode == RESULT_OK) 
		{
			if (requestCode == Model.TWITTER_LOGIN_CODE)
			{
				try
				{
					g_AccessToken = g_Twitter.getOAuthAccessToken(g_reqToken, data.getStringExtra("oauth_verifier"));
					  
					Log.v(Model.LOG_TAG, "Twitter Access Token : " + g_AccessToken.getToken());
					Log.v(Model.LOG_TAG, "Twitter Access Token Secret : " + g_AccessToken.getTokenSecret());
					
					Util.setAppPreferences(this, Model.TWITTER_ACCESS_TOKEN, g_AccessToken.getToken());
					Util.setAppPreferences(this, Model.TWITTER_ACCESS_TOKEN_SECRET, g_AccessToken.getTokenSecret());					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/***********************************************************************************
	 * 
	 * Twitter Logic
	 * 
	 ***********************************************************************************/
	private void login()
	{
		try
		{
/*			
			String accessToken = Model.TWITTER_ACCESS_TOKEN.toString();
			String accessTokenSecret = Model.TWITTER_ACCESS_TOKEN_SECRET.toString();
			
			if (accessToken != null && accessTokenSecret != null)
			{
				g_AccessToken = new AccessToken(accessToken, accessTokenSecret);
		
				Log.v(Model.LOG_TAG, "accessToken : " + g_AccessToken.getToken());
				Log.v(Model.LOG_TAG, "accessTokenSecret : " + g_AccessToken.getTokenSecret());
			}
			else
	*/			
			{
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true);
				cb.setOAuthConsumerKey(Model.TWITTER_CONSUMER_KEY);
				cb.setOAuthConsumerSecret(Model.TWITTER_CONSUMER_SECRET);
				
				TwitterFactory factory = new TwitterFactory(cb.build());
				
				g_Twitter = factory.getInstance();
				
				g_reqToken = g_Twitter.getOAuthRequestToken(Model.TWITTER_CALLBACK_URL);
				

				Intent intent = new Intent(this, TwitterLogin.class);
				intent.putExtra("auth_url", g_reqToken.getAuthorizationURL());
				
				startActivityForResult(intent, Model.TWITTER_LOGIN_CODE); 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	private void write()
	{
		//String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		//String fileName = "example.jpg";
		//InputStream is = null;
	
		try
		{
			/*
			if (new File(path + File.separator + fileName).exists())
			{
				is = new FileInputStream(path + File.separator + fileName);
			}
			else
			{
				is = null;
			}
			*/
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			
			String oAuthAccessToken = g_AccessToken.getToken();
			String oAuthAccessTokenSecret = g_AccessToken.getTokenSecret();
			String oAuthConsumerKey = Model.TWITTER_CONSUMER_KEY;
			String oAuthConsumerSecret = Model.TWITTER_CONSUMER_SECRET;
			
			cb.setOAuthAccessToken(oAuthAccessToken);
			cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
			cb.setOAuthConsumerKey(oAuthConsumerKey);
			cb.setOAuthConsumerSecret(oAuthConsumerSecret);
			Configuration config = cb.build();
			//OAuthAuthorization auth = new OAuthAuthorization(config);
			  
			TwitterFactory tFactory = new TwitterFactory(config);
			Twitter twitter = tFactory.getInstance();
			
			/*
			ImageUploadFactory iFactory = new ImageUploadFactory(getConfiguration(Model.TWITPIC_API_KEY));
			ImageUpload upload = iFactory.getInstance(MediaProvider.TWITPIC, auth);
			  
			if (is != null)
			{
				String strResult = upload.upload("example.jpg", is, mEtContent.getText().toString());
				twitter.updateStatus(mEtContent.getText().toString() + " " + strResult);
			}
			else
			{
				twitter.updateStatus(mEtContent.getText().toString());
			}
			*/

			Log.d(Model.LOG_TAG, mEtContent.getText().toString());			
			twitter.updateStatus(mEtContent.getText().toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				; //is.close();
			}
			catch (Exception e) 
			{
				;
			}
		}
	}
	
	private void logout()
	{
 		Intent intent = new Intent(Model.MOVE_TWITTER_LOGIN);
		intent.putExtra("auth_url", Model.TWITTER_LOGOUT_URL);
		startActivity(intent);	
	}
	
  	
	
/*	
	public String getRequestToken(){
		g_Twitter = new TwitterFactory().getInstance();
		g_Twitter.setOAuthConsumer(Model.TWITTER_CONSUMER_KEY, Model.TWITTER_CONSUMER_SECRET);

		try {
			g_reqToken = twitter.getOAuthRequestToken();
		}
		catch(TwitterException te)	{
			te.printStackTrace();
		}

		try{
			//Request Token 파일로 저장
			FileOutputStream fileStream = new FileOutputStream(new File(REQUEST_FILE));
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(g_reqToken);
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		//트위터 인증 URL 리턴
		return g_reqToken.getAuthorizationURL();
	}

	public void getAccessToken(){
		g_Twitter = new TwitterFactory().getInstance();
		g_Twitter.setOAuthConsumer(Model.TWITTER_CONSUMER_KEY, Model.TWITTER_CONSUMER_SECRET);

		try{
			//아까 저장했던 Request Token파일을 가져오기
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(REQUEST_FILE));
			g_reqToken = (RequestToken) os.readObject();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		try {
			g_AccessToken = g_Twitter.getOAuthAccessToken(g_reqToken, PINCODE);
					 
			try{
				//Access Token 파일로 저장
				FileOutputStream fileStream = new FileOutputStream(new File(ACCESS_FILE));
				ObjectOutputStream os = new ObjectOutputStream(fileStream);
				os.writeObject(g_AccessToken);
				os.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		catch(TwitterException te) {
			te.printStackTrace();
		}
	}

	public void goAccess(AccessToken accesstoken){
		g_Twitter = new TwitterFactory().getInstance();
		g_Twitter.setOAuthConsumer(Model.TWITTER_CONSUMER_KEY, Model.TWITTER_CONSUMER_SECRET);
		g_Twitter.setOAuthAccessToken(accesstoken);
	}

	public void sendmessage(String id, String str){
		try {
			g_Twitter.sendDirectMessage(id,str);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public void search(){
		int	fileFlag;
		fileFlag = 0;

		//오늘날짜 yyyy-mm-dd
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date today = new Date();
		Date yesterDay = new Date(today.getTime() - (long)(1000 * 60 * 60 * 24));
		String dTime = formatter.format(yesterDay);
		
		Query query = new Query("CF");	//검색어
		query.setCount(100);			//가져올 트윗수
		query.setLang("ko");			//언어
		query.setLocale("korea");		//검색 지역
		query.setSince(dTime);			//이날 이후 데이터
		
		QueryResult result = null;
		try {
			result = g_Twitter.search(query);
		
			String str = "";
			str = "<?xml version='1.0' encoding='UTF-8'?>\n";
			str	= str + "<wrap>\n";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (Status status : result.getTweets()) {
				fileFlag = 1;
				String getName 				= "";
				String getScreenName 		= "";
				String getText 				= "";
				String getProfileImageURL 	= "";
				String getCreatedAt			= "";
				
				getName 			= "<name><![CDATA["	+ status.getUser().getName() 			+ "]]></name>\n";
				getScreenName 		= "<id><![CDATA[" 	+ status.getUser().getScreenName() 		+ "]]></id>\n";
				getText				= "<text><![CDATA[" + status.getText() 						+ "]]></text>\n";
				getProfileImageURL 	= "<imgurl>" 		+ status.getUser().getProfileImageURL() + "</imgurl>\n";
				getCreatedAt		= "<cdate>"			+ sdf.format(status.getCreatedAt()) 	+ "</cdate>\n";
				
				str	= str + "<data>\n";
				str = str + getName + getScreenName + getText + getProfileImageURL + getCreatedAt;
				str	= str + "</data>\n";
			}
			str = str + "</wrap>";
		
			//String fileName = "/home/ottomo/public_html/Twit.xml";
			String fileName = "C:/Users/ksc/Desktop/TestXml.xml";
			
			if(fileFlag == 1){
				File file = new File(fileName);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
				out.write(str);
				out.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace(); 
		}
	}

	private void write()
	{
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		String fileName = "example.jpg";
		InputStream is = null;
	
		try
		{
			if (new File(path + File.separator + fileName).exists())
			{
				is = new FileInputStream(path + File.separator + fileName);
			}
			else
			{
				is = null;
			}
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			
			String oAuthAccessToken = g_AccessToken.getToken();
			String oAuthAccessTokenSecret = g_AccessToken.getTokenSecret();
			String oAuthConsumerKey = Model.TWITTER_CONSUMER_KEY;
			String oAuthConsumerSecret = Model.TWITTER_CONSUMER_SECRET;
			
			cb.setOAuthAccessToken(oAuthAccessToken);
			cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
			cb.setOAuthConsumerKey(oAuthConsumerKey);
			cb.setOAuthConsumerSecret(oAuthConsumerSecret);
			Configuration config = cb.build();
			OAuthAuthorization auth = new OAuthAuthorization(config);
			  
			TwitterFactory tFactory = new TwitterFactory(config);
			Twitter twitter = tFactory.getInstance();
			ImageUploadFactory iFactory = new ImageUploadFactory(getConfiguration(Model.TWITPIC_API_KEY));
			ImageUpload upload = iFactory.getInstance(MediaProvider.TWITPIC, auth);
			  
			if (is != null)
			{
				String strResult = upload.upload("example.jpg", is, mEtContent.getText().toString());
				twitter.updateStatus(mEtContent.getText().toString() + " " + strResult);
			}
			else
			{
				twitter.updateStatus(mEtContent.getText().toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (Exception e) 
			{
				;
			}
		}
	}
  
	
	public void printStatuses()	{
		ResponseList<Status> statuses;   
		Paging page = new Paging(); 
		page.count(20); 
		page.setPage(1);   
		try {   
			statuses = g_Twitter.getHomeTimeline(page);
			for (Status status : statuses) {    
				System.out.println(status.getUser().getScreenName() + ":" + status.getText());     
			}			
		} 
		catch(TwitterException te) {
			te.printStackTrace();
		} 
	}	
	
	*/
}
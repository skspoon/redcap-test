// package 

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


public class ExportRecords
{
	private final List<NameValuePair> params;
	private final HttpPost post;
	private HttpResponse resp;
	private final HttpClient client;
	private int respCode;
	private BufferedReader reader;
	private final StringBuffer result;
	private String line;

	public ExportRecords(final Config c)
	{

		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", c.API_TOKEN));
		params.add(new BasicNameValuePair("content", "record"));
		params.add(new BasicNameValuePair("format", "csv"));
		params.add(new BasicNameValuePair("type", "flat"));
		params.add(new BasicNameValuePair("fields", c.API_FIELDS));
		//params.add(new BasicNameValuePair("returnFormat", "csv"));
		//params.add(new BasicNameValuePair("csvDelimiter", "|"));

		post = new HttpPost(c.API_URL);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try
		{
			post.setEntity(new UrlEncodedFormEntity(params));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
	}

	public void doPost()
	{
		resp = null;

		try
		{
			resp = client.execute(post);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		if(resp != null)
		{
			respCode = resp.getStatusLine().getStatusCode();

			try
			{
				reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}

		if(reader != null)
		{
			try
			{
				while((line = reader.readLine()) != null)
				{
				    result.append(line).append("\n");
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
	}

        public final String toString() {
	    if (result == null)
		return null;
	    else
		return result.toString();
	}


	public static void main(String[] args) throws IOException {

	    // a short cut, should fetch from an external properties file that stores the configurations
	    Config conf = new Config("https://redcap.dogagingproject.org/api/",
				     "...request a token of the REDCap project from the Adminstrator...".
				     "subject_id, fu_df_frequency, fu_df_prim_org, fu_df_prim_brand, fu_df_overrweight");

	    ExportRecords tst = new ExportRecords(conf);
	    tst.doPost();
	    System.out.println(tst.toString());
	}
}

public final class Config {

    public final String API_TOKEN;
    public final String API_URL;
    public final String API_FIELDS;

    public Config (String url, String token, String fields) {
	API_URL = url;
	API_TOKEN = token;
	API_FIELDS = fields;
    }
}

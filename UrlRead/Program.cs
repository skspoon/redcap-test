using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;

namespace UrlRead
{
    public class UrlRead {
        private String API_TOKEN;
        private String API_URL;
        private String API_FIELDS;

        public UrlRead (String url, String token, String fields) {
            API_URL = url;
            API_TOKEN = token;
            API_FIELDS = fields;
        }

        public static async Task MainAsync(UrlRead c)
        {
            var values = new Dictionary<string, string> {
                {"token", c.API_TOKEN},
                {"content", "record"},
                {"format", "csv"},
                {"type", "flat"},
                {"fields", c.API_FIELDS} 
            };

            try {
                // using (WebClient client = new WebClient())
                using (var client = new HttpClient()) {

                    var content = new FormUrlEncodedContent(values);
                    var response = await client.PostAsync(c.API_URL, content);
                    var responseString = await response.Content.ReadAsStringAsync();

                    // Console.WriteLine(responseString);
                    Console.Write(responseString);
                }
            }
            finally {
                // Console.WriteLine("[Done]");
            }
        }
        public static void Main(string[] args)
        {
            // Should fetch from an external properties file that stores the configurations.
            // This is a short cut, to just initialize a Config obj, (see Config.java), like this.

            var conf = new UrlRead("https://redcap.dogagingproject.org/api/",
                                    "...request a REAL token of the REDCap project from the Adminstrator...",
                                    "subject_id, fu_df_frequency, fu_df_prim_org, fu_df_prim_brand, fu_df_overrweight");

            // Task.Run(() => MainAsync(conf));
            // Console.ReadLine();
            Task.Run(async() => await MainAsync(conf)).Wait();
        }
    }
}

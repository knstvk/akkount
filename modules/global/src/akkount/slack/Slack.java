package akkount.slack;

import akkount.config.AkkConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

@Component(Slack.NAME)
public class Slack {

    public static final String NAME = "akk_Slack";

    @Inject
    private AkkConfig akkConfig;

    private static final Logger log = LoggerFactory.getLogger(Slack.class);

    public String sendMessage(String channel, String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpPost httpPost = new HttpPost("https://slack.com/api/chat.postMessage");
            httpPost.setHeader("Authorization", "Bearer " + akkConfig.getSlackToken());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channel", channel);
            jsonObject.put("text", message);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);

            log.info("Executing request " + httpPost.getRequestLine() + (log.isDebugEnabled() ? "\n" + jsonObject : ""));

            String response = httpClient.execute(httpPost, new StringResponseHandler());
            log.info("Response: " + response);
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Error sending message", e);
        }
    }

    public static class StringResponseHandler implements ResponseHandler<String> {
        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new HttpResponseException(status, "Unexpected response status: " + status);
            }
        }
    }
}

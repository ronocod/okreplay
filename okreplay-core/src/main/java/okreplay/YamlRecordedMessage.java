package okreplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import static okreplay.AbstractMessage.DEFAULT_CONTENT_TYPE;
import static okreplay.Util.CONTENT_TYPE;
import static okreplay.Util.isNullOrEmpty;

public abstract class YamlRecordedMessage {
  private final Map<String, List<String>> headers;
  private final Object body;

  YamlRecordedMessage(Map<String, List<String>> headers, Object body) {
    this.headers = headers;
    this.body = body;
  }

  String contentType() {
    List<String> valueList = headers.get(CONTENT_TYPE);
    if (valueList.isEmpty()) {
      return DEFAULT_CONTENT_TYPE;
    }
    String header = valueList.get(0);
    if (isNullOrEmpty(header)) {
      return DEFAULT_CONTENT_TYPE;
    } else {
      return MediaType.parse(header).toString();
    }
  }

  public Map<String, List<String>> headers() {
    return headers;
  }

  public String header(String name) {
    List<String> valueList = headers.get(name);
    if (valueList == null || valueList.isEmpty()) {
      return null;
    }
    return valueList.get(0);
  }

  public List<String> headers(String name) {
    return headers.get(name);
  }

  public Object body() {
    return body;
  }

  abstract Message toImmutable();


  okhttp3.Headers headersForOkHttp(Map<String, List<String>> headers) {
    List<String> headerValues = new ArrayList<>(headers.size());
    for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
      for (String value : entry.getValue()) {
        headerValues.add(entry.getKey());
        headerValues.add(value);
      }
    }
    return Headers.of((String[]) headerValues.toArray());
  }
}

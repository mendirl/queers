package helper;

import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;


public class WSHelper {

    public static WSResponse ask(String url, String... params) {
        WSRequestHolder holder = WS.url(url);

        for (int i = 0; i < params.length - 1; i = i + 2) {
            holder.setQueryParameter(params[i], params[i + 1]);
        }

        F.Promise<WSResponse> promise = holder.get();
        return promise.get(3000);
    }
}

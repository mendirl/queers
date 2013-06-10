package helper;

import play.libs.F;
import play.libs.WS;


public class WSHelper {

    public static WS.Response ask(String url, String... params) {
        WS.WSRequestHolder holder = WS.url(url);

        for (int i = 0; i < params.length - 1; i = i + 2) {
            holder.setQueryParameter(params[i], params[i + 1]);
        }

        F.Promise<WS.Response> promise = holder.get();
        return promise.get();
    }
}

package server.net;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class PageInfoEncoder  implements Encoder.Text<PageInfo>{
    
    @Override
    public String encode(PageInfo inf) throws EncodeException {
        return inf.getJson().toString();
    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("init");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}

package kakapo.client.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHeaderV1 extends BaseHeaderOrContent {

    private String _response;

    public ResponseHeaderV1() {
        // Required no-argument constructor.
    }

    public ResponseHeaderV1(String response) {
        _response = response;
    }

    @Override
    protected void deserializeInternal(DataInputStream dataInputStream) throws IOException {
        _response = dataInputStream.readUTF();
    }

    @Override
    protected void serializeInternal(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(_response);
    }

    public String getResponse() {
        return _response;
    }
}

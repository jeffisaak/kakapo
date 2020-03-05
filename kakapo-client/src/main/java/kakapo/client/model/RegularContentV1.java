package kakapo.client.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegularContentV1 extends BaseHeaderOrContent {

    private byte[] _attachmentData;

    public RegularContentV1() {
        // Required no-argument constructor.
    }

    public RegularContentV1(byte[] attachmentData) {
        _attachmentData = attachmentData;
    }

    @Override
    protected void deserializeInternal(DataInputStream dataInputStream) throws IOException {

        int attachmentLength = dataInputStream.readInt();
        if (attachmentLength > 0) {
            _attachmentData = new byte[attachmentLength];
            int bytesRead = dataInputStream.read(_attachmentData);
            if (bytesRead != attachmentLength) {
                throw new IOException("Attachment data is of incorrect length");
            }
        }
    }

    @Override
    protected void serializeInternal(DataOutputStream dataOutputStream) throws IOException {

        int attachmentLength = 0;
        if (_attachmentData != null) {
            attachmentLength = _attachmentData.length;
        }
        dataOutputStream.writeInt(attachmentLength);
        if (attachmentLength > 0) {
            dataOutputStream.write(_attachmentData);
        }
    }

    public byte[] getAttachmentData() {
        return _attachmentData;
    }
}

package kakapo.client.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegularHeaderV1 extends BaseHeaderOrContent {

    private String _title;
    private String _url;
    private String _message;
    private byte[] _thumbnailData;

    public RegularHeaderV1() {
        // Required no-argument constructor.
    }

    public RegularHeaderV1(String title, String url, String message, byte[] thumbnailData) {
        _title = title;
        _url = url;
        _message = message;
        _thumbnailData = thumbnailData;
    }

    @Override
    protected void deserializeInternal(DataInputStream dataInputStream) throws IOException {

        _title = dataInputStream.readUTF();

        boolean urlPresent = dataInputStream.readBoolean();
        if (urlPresent) {
            _url = dataInputStream.readUTF();
        }

        boolean messagePresent = dataInputStream.readBoolean();
        if (messagePresent) {
            _message = dataInputStream.readUTF();
        }

        int thumbnailLength = dataInputStream.readInt();
        if (thumbnailLength > 0) {
            _thumbnailData = new byte[thumbnailLength];
            int bytesRead = dataInputStream.read(_thumbnailData);
            if (bytesRead != thumbnailLength) {
                throw new IOException("Thumbnail data is of incorrect length");
            }
        }
    }

    @Override
    protected void serializeInternal(DataOutputStream dataOutputStream) throws IOException {

        dataOutputStream.writeUTF(_title);

        dataOutputStream.writeBoolean(_url != null);
        if (_url != null) {
            dataOutputStream.writeUTF(_url);
        }

        dataOutputStream.writeBoolean(_message != null);
        if (_message != null) {
            dataOutputStream.writeUTF(_message);
        }

        int thumbnailLength = 0;
        if (_thumbnailData != null) {
            thumbnailLength = _thumbnailData.length;
        }
        dataOutputStream.writeInt(thumbnailLength);
        if (thumbnailLength > 0) {
            dataOutputStream.write(_thumbnailData);
        }
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public byte[] getThumbnailData() {
        return _thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData) {
        _thumbnailData = thumbnailData;
    }
}

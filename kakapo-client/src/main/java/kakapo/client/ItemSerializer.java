package kakapo.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kakapo.client.model.BaseHeaderOrContent;
import kakapo.client.model.ItemDeserializeException;
import kakapo.client.model.ItemSerializeException;
import kakapo.client.model.RegularContentV1;
import kakapo.client.model.RegularHeaderV1;
import kakapo.client.model.ResponseContentV1;
import kakapo.client.model.ResponseHeaderV1;
import kakapo.client.model.UnknownItemTypeException;

public class ItemSerializer {

    private Map<String, Class<? extends BaseHeaderOrContent>> _itemTypeMap = new HashMap<>();

    public ItemSerializer() {

        // Build the item type map. This map defines the different types of things that we can
        // store and retrieve to and from the Kakapo server. As a client, we can define whatever we
        // want, and other clients needn't support our item types.

        _itemTypeMap.put("com.aptasystems.kakapo.regular.header.v1", RegularHeaderV1.class);
        _itemTypeMap.put("com.aptasystems.kakapo.regular.content.v1", RegularContentV1.class);

        _itemTypeMap.put("com.aptasystems.kakapo.response.header.v1", ResponseHeaderV1.class);
        _itemTypeMap.put("com.aptasystems.kakapo.response.content.v1", ResponseContentV1.class);

    }

    public BaseHeaderOrContent deserialize(byte[] data) throws ItemDeserializeException, UnknownItemTypeException {

        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));

        // First deserialize the item type to figure out how to treat the rest of the data.
        String itemType = null;
        try {
            itemType = inputStream.readUTF();
        } catch (IOException e) {
            throw new ItemDeserializeException(e);
        }

        // Based on the item type, instantiate the appropriate class.
        Class targetClass = _itemTypeMap.get(itemType);

        // If we didn't find a target class for the item type, this means we can't handle
        // whatever we are trying to deserialize.
        if (targetClass == null) {
            throw new UnknownItemTypeException("Unknown item type: " + itemType);
        }

        // Instantiate the target class.
        BaseHeaderOrContent result = null;
        try {
            result = (BaseHeaderOrContent) targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ItemDeserializeException(e);
        }

        // Deserialize.
        try {
            result.deserialize(inputStream);
        } catch (IOException e) {
            throw new ItemDeserializeException(e);
        }

        // Attempt to close the input stream.
        try {
            inputStream.close();
        } catch (IOException e) {
            // Ignore.
        }

        return result;
    }

    public byte[] serialize(BaseHeaderOrContent item) throws ItemSerializeException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        // Serialize the item type. Probably a better way to do this, but this works for now.
        for (String type : _itemTypeMap.keySet()) {
            Class targetClass = _itemTypeMap.get(type);
            if (item.getClass() == targetClass) {
                try {
                    dataOutputStream.writeUTF(type);
                } catch (IOException e) {
                    throw new ItemSerializeException(e);
                }
            }
        }

        // Serialize the item by delegating to the item.
        try {
            item.serialize(dataOutputStream);
        } catch (IOException e) {
            throw new ItemSerializeException(e);
        }

        // Close the stream.
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            // Ignore.
        }

        return byteArrayOutputStream.toByteArray();
    }
}

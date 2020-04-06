# Kakapo Server API V1 Documentation

## Table of Contents

* [Acronyms and Initialisms](#acronyms-and-initialisms)
* [General Information](#general-information)
* [Hex Strings](#hex-strings)
* [Crypto](#crypto)
* [Rate Limiting](#rate-limiting)
* [User-related endpoints](#user-related-endpoints)
* [Server-related endpoints](#server-related-endpoints)
* [Item-related endpoints](#item-related-endpoints)

## Acronyms and Initialisms

| Term | Explanation |
| --- | --- |
| GUID | [Globally Unique Identifiers](https://en.wikipedia.org/wiki/Universally_unique_identifier) are used primarily to represent users. |

## General Information

The Kakapo server at it's core is really just an online storage system with some basic user management and sharing features. Users are identified by GUIDs. Users may upload "items" to be shared with other users. These items are comprised of two segments of encrypted data: a header and content.

The encrypted header is intended to be a preview of the item and may contain a small amount of data while the content is intended to contain more data. For example, if a user shared a photo, the header could contain a thumbnail while the content could contain the full-resolution photo. As a second example, if a user shared a URL and/or a simple text-based message, the header could contain the URL and/or message, and the content could be empty.

How the encrypted data is structured within these two containers is at the client's discretion. There are limits set by the Kakapo server on the size of each of these containers. These limits can be fetched from the server.

The protocol is stateless and authentication occurs for every request, so authentication information must be provided with each request that requires it. Any endpoint that requires the "Kakapo-ID" and "Kakapo-API-Key" headers is one that requires authentication.

In addition to some standard HTTP codes, there are some custom HTTP codes in use. These are HTTP 442 (quota exceeded), 443 (insufficient key length), and 444 (no prekeys available).

## Hex strings

In many cases keys, salts, and nonces are represented as strings. These are typically produced via libsodium's to\_hex() call and may be converted back to a binary representation using libsodium's to\_bin() call.

## Crypto

Kakapo makes use of Libsodium (well, more specifically, the LazySodium Java bindings for Libsodium.

You may see the LibSodiumCrypto class in the kakapo repository for details regarding the specific libsodium calls that are used, but a bit of an explanation regarding the key agreement mechanism is warranted here.

### Key agreement mechanism

As the communication in Kakapo is asynchronous, we need a way to allow key agreement to happen with the recipient(s) offline. We do this through "prekeys" (thank you, Moxie, for explaining this in a way I can understand).

Every user, when connected to the Kakapo server, must generate and upload a set of prekeys. When someone wants to share something with one or more users, they must get prekeys for those users from the server. Using those prekeys, and their own private key that they generate, they generate shared secrets with each recipient.

The client then must generate a group key that will be used to encrypt the shared item, and encrypt that group key with each shared secret for each recipient.

The public key exchange key, prekey ID, encrypted group key, and encrypted content is then sent off to each recipient, who must then use the secret half of the prekey that they have to decrypt the group key and then use the group key to decrypt the message.

## Rate limiting

All endpoints are rate-limited. Rate-limiting uses the [token-bucket algorithm](https://en.wikipedia.org/wiki/Token_bucket).

Rate limiting is done by both source IP address and user GUID (where possible).  The following table contains the currently-configured values for rate limiting.

| Method | Path | Token count | Refill rate |
| --- | --- | --- | --- |
| POST | /api/v1/account/create | 10 | 1/day |
| GET | /api/v1/item/headers | 600 | 10/min |
| | all others | 60 | 1/min |

As a brief explanation, with the create account endpoint: each IP address can make 10 requests to this endpoint at which point the bucket is empty and further requests from the IP address will be denied. Every day, one token is added to the bucket, which means that a further request can be made each day. If three days go by without any account creation requests from the IP address, three requests will go through successfully before further requests are rejected.

With all other endpoints, only the numbers change. The buckets for "all others" initially contain 60 tokens and are refilled at a rate of 1 per minute, with a maximum of 60.

Remember that these are per-IP address and per-user GUID. In the cases where the URL contains a path parameter, they are also per-URL.

The primary reason for rate-limiting is to prevent abuse of the Kakapo network. The primary point of entry for abuse is account creation, which is why that endpoint is so severely restricted. The goal is to balance the need for legitimate users to create accounts with the desire to prevent malicious users from creating vast quanitities of accounts.

These limits will be adjusted as necessary.

## User-related endpoints

### Create a new user account

This call is used to create a new user account. A public key must be provided and the server will generate and return a new GUID and API Key for the user. In any subsequent call that requires authentication, this combination of GUID and API Key must be provided.

The signing public key must be 32 bytes long.

**POST /api/v1/account**

Request Body:

```json
{
	"signingPublicKey" : "<Hex string representation of public key>"
}
```

Response Body:

```json
{
	"guid" : "<GUID for the new account>",
	"apiKey" : "<API Key for the new account>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |
| 443 | The provided public key is not 32 bytes long. |

### Authenticate

Performs an authentication check.

**GET /api/v1/account/&lt;GUID&gt;**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Upload PreKeys

Upload a set of prekeys to the server. The response echos the prekeys back to the client, along with an associated ID, so that the prekey can be referenced in later messaging.

**POST /api/v1/account/&lt;GUID&gt;/preKeys**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Request Body:

```json
{
	"signedPreKeys" : [
		"<Hex string representation of signed prekey public key>",
		"..."
	]
}
```

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```
{
	"idToKeyMap" : {
		"<keyId>": "<Hex string representation of signed prekey public key>",
		"..."
	}
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Delete account

Deletes the account from the server and nulls out all of the account's shared items.

**DELETE /api/v1/account/&lt;GUID&gt;**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch a prekey

**GET /api/v1/account/&lt;Target GUID&gt;/preKey**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```
{
	"preKeyId" : "<The unique ID of the prekey>",
	"signedPreKey" : "<Hex string representation of the prekey signed by the owner's private signing key>"
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | Target user with specified GUID not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch a signing public key

Fetches a signing public key from the server for a particular user's GUID. This signing key can be used to verify the prekeys for the user.

**GET /api/v1/account/&lt;Target GUID&gt;/publicKey**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```json
{
	"signingPublicKey" : "<Hext string representation of the user's signing public key>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | Target user with specified GUID not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch quota

Fetches the maximum quota and currently-used amount (in megabytes).

**GET /api/v1/account/&lt;GUID&gt;/quota**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```
{
	"usedQuota" : <Used quota in bytes>,
	"maxQuota" : <Max quota in bytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Backup account

Uploads (encrypted) account data to the server so that it can be synced on another device. Note that the server does not check to see that the account data is encrypted, but stores it as-is.

This endpoint consumes multipart form data.

**POST /api/v1/account/&lt;GUID&gt;/backup**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Multipart Form Data:

- **json**: The request body in JSON format.
- **data**: The serialized, encrypted account data as a multipart file.

Request Body:

```json
{
	"backupVersionToUpdate" : "<The target backup version to update>",
	"nonce" : "<Nonce used to encrypt the backup data>"
}
```

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```json
{
	"backupVersion" : "<The new backup version stored on the server>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 409 | The specified version in the request body does not match the current backup version on the server. |
| 413 | Payload too large; the maximum size of the uploaded account has been exceeded.
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Get backup version

**GET /account/&lt;GUID&gt;/backup/version**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```json
{
	"backupVersion" : "<The current backup version stored on the server>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Download account data

Downloads (encrypted) account data so that it can be added to another device. The data is streamed back to the client.

**GET /api/v1/account/&lt;GUID&gt;/backup**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.
- **Kakapo-Backup-Version-Number**: The version number of the backup.
- **Kakapo-Backup-Nonce**: The nonce used to encrypt the account data.

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | Backup data not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Blacklist a user

Adds the target user to the blacklist. Items owned by blacklisted users are not included when headers are fetched. Mostly. It's slightly more nuanced than this. Please see the "list item headers" endpoint below for more details.

**POST /api/v1/account/&lt;GUID&gt;/blacklist/&lt;Target GUID&gt;**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```text
No response body
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | Target user not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

## Server-related endpoints

### Fetch server configuration

The server has some configuration parameters that the clients may want to know about. You can fetch them with this endpoint.

**PUT /api/v1/server/config**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```
{
	"quotaPerUser" : <maximum quota per user in megabytes>
	"headerSizeLimit" : <maximum encrypted item header size in bytes>
	"contentSizeLimit" : <maximum encrypted item content size in bytes>
	"accountBackupSizeLimit" : <maximum encrypted account backup size in bytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

## Item-related endpoints

### Upload a new item

Upload a new item with an encrypted header and content payload.

This endpoint consumes multipart form data.

**POST /api/v1/item**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Multipart Form Data:

- **json**: The request body in JSON format.
- **header**: The serialized, encrypted header data as a multipart file.
- **content**: The serialized, encrypted content data as a multipart file.

Request Body:

```
{
	"headerNonce" : "<Hex string representation of the nonce used to encrypt the header data>",
	"contentNonce" : "<Hex string representation of the nonce used to encrypt the content data>",
	"destinations" : [
		{
			"userGuid" : "<The destination user's ID>",
			"preKeyId" : <The id of the prekey used to negotiate a shared secret>,
			"encryptedGroupKey" : "<Hex string representation of the encrypted key that, once decrypted, may be used to decrypt the header and content data>",
			"groupKeyNonce" : "<Hex string representation of the nonce used to encrypte/decrypt the group key>"
		},
		...
	],
	"keyExchangePublicKey" : "<Hex string representation of the key exchange public key used to agree on the secret key that is used to encrypt/decrypt the encrypted group key>",
	"parentItemRemoteId" : "<An optional parent item ID if this is a child item>"
}
```

Response Headers:

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.

Response Body:

```
{
	"itemRemoteId" : <The newly assigned numeric ID of the uploaded item>,
	"usedQuota" : <Used quota in bytes>,
	"maxQuota" : <Max quota in bytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | The specified parent item id was not found, or it is not visible to the user. |
| 413 | Payload too large; the maximum size of the uploaded header or content has been exceeded.
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Delete item

Delete a specific item from the server. The item is marked as deleted and the header and content are nulled out.

**DELETE /api/v1/item/&lt;Item Remote Id&gt;**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Body:

```
{
	"itemRemoteId" : <The ID of the deleted item>,
	"usedQuota" : <Used quota in bytes>,
	"maxQuota" : <Max quota in bytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | Not found; the specified item was either not found or the user does not own the item.
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### List item headers

Fetch a list of item headers that are visible to a specific user. The items are fetched in reverse chronological order, starting with the first item with an ID less than the "count" request parameter.

The "parent" and "id" values are optional. If the "parent" value is specified, only items with the specified parent item ID are fetched. If the "id" value is specified only that single item is fetched. If both are specified, the "parent" value is ignored. 

If the "parent" value is not specified, only "parentless" items are fetched.

A note regarding blacklisted users: When the "parent" (parent item id) value is provided, items from blacklisted users will be included in the response. If the "parent" value is not provided, items from blacklisted users will not be included in the response. The reason for this is that when fetching the children of a specified parent, the item from the blacklisted author may be in the middle of a response tree and it is important for the client to have the complete tree of responses for presentation purposes.

**GET /api/v1/item/headers**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Request Parameters:

- **count**: The number of item headers to fetch, within the range 1-20.
- **last**: The item ID to start fetching headers after
- **parent**: If specified, only fetch items that are children of this item ID (optional)
- **id**: A specific item ID to fetch (optional)

Response Body:

```
{
	"shareItems" : [
		{
			"remoteId" : <the item ID>
			"ownerGuid" : "<the GUID of the owner/author of the item>"
			"parentRemoteId" : <the parent ID of this item (may be null)>
			"markedAsDeleted" : true|false (true if the item has been marked as deleted, false if not),
			"blacklisted" : true|false (true if the item is owned by a blacklisted author, false if not),
			"itemTimestamp" : <item timestamp in GMT (in ms from the epoch (midnight, January 1, 1970))>
			"encryptedHeader" : "<Hex string representation of the encrypted header data>",
			"headerNonce" : "<Hex string representation of the nonce used to encrypt the header>",
			"childcount" : <Number of descendants this item has>,
			"preKeyId" : <The ID of the prekey that was used to negotiate a secret key with which to encrypt the group key>,
			"keyExchangePublicKey" : "<Hex string representation of the key exchange public key that must be used to negotiate the aforementioned secret key>",
			"encryptedGroupkey" : "<Hex string representation of the encrypted group key used to encrypt the header/content data>",
			"groupkeyNonce" : "<Hex string representation of the group key used to encrypt the header/content data>"
		},
		...
	],
	"remainingItemCount" : <the number of remaining item headers that may be fetched>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch item content

Fetch the encrypted content of an item. The data is streamed back to the client.

**GET /api/v1/item/&lt;Item Remote Id&gt;/content**

Request Headers:

- **Kakapo-ID**: The user's GUID, for authentication.
- **Kakapo-API-Key**: the user's API Key, for authentication.

Response Headers:

TODO: I am here.

- **Kakapo-Pre-Keys-Remaining**: The number of unused prekeys remaining on the server.
- **Kakapo-Pre-Key-ID**: The ID of the prekey
- **Kakapo-Key-Exchange-Public-Key**: 
- **Kakapo-Nonce**:
- **Kakapo-Encrypted-Group-Key**:
- **Kakapo-Content-Nonce**:


| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed. |
| 404 | Backup data not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |


Request Body:

```
{
	"ir" : <the ID of the item to fetch>,
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```text
No response body; the encrypted content is streamed back to the client.
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch recipients for an item

Fetch the list of recipients for a given item. This may be useful if, for example, someone is posting a follow-up to an item, as the follow-up should probably be visible to all the users that the original item was visible to.

**GET /api/v1/item/&lt;Item Remote Id&gt;/recipients**

Request Body:

```
{
	"ir" : <the ID of the item to fetch recipients for>,
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```
{
	"r" : [
		{
			"guid" : "<recipient's GUID>",
			"pkr" : "<MIME encoded GPG-compatible public key rings byte array>"
		},
		...
	]
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

# Kakapo Server API V1 Documentation

## Table of Contents

* [Acronyms and Initialisms](#acronyms-and-initialisms)
* [General Information](#general-information)
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

The protocol is stateless and authentication occurs for every request, so authentication information must be provided with each request that requires it. Any endpoint that contains the "sig" entry is one that requires authentication.

Authentication is performed by provision of a digital signature.

In addition to some standard HTTP codes, there are some custom HTTP codes in use. These are HTTP 442 (quota exceeded) and 443 (insufficient key length).

Kakapo uses GPG-compatible public/secret keys. If your client can generate valid GPG-compatible keys they should be fine to use with Kakapo.

## Rate limiting

All endpoints are rate-limited. Rate-limiting uses the [token-bucket algorithm](https://en.wikipedia.org/wiki/Token_bucket).

Rate limiting is done by both source IP address and user GUID (where possible).  The following table contains the currently-configured values for rate limiting.

| Path | Token count | Refill rate |
| --- | --- | --- |
| /api/v1/account/create | 10 | 1/day |
| /api/v1/item/headers | 600 | 10/min |
| all others | 60 | 1/min |

As a brief explanation, with the create account endpoint: each IP address can make 10 requests to this endpoint at which point the bucket is empty and further requests from the IP address will be denied. Every day, one token is added to the bucket, which means that a further request can be made each day. If three days go by without any account creation requests from the IP address, three requests will go through successfully before further requests are rejected.

With all other endpoints, only the numbers change. The buckets for "all others" initially contain 60 tokens and are refilled at a rate of 1 per minute, with a maximum of 60.

Remember that these are per-IP address and per-user GUID.

The primary reason for rate-limiting is to prevent abuse of the Kakapo network. The primary point of entry for abuse is account creation, which is why that endpoint is so severely restricted. The goal is to balance the need for legitimate users to create accounts with the desire to prevent malicious users from creating vast quanitities of accounts.

These limits will be adjusted as necessary.

## User-related endpoints

### Request a GUID for a new user account

Before creating an account, a GUID for the user account must be requested from the server. This is the first part of a two-part operation as the client will need the GUID to create the GPG keypair.

**GET /api/v1/account/requestGuid**

Request Body:

```text
No request body
```

Response Body:

```json
{
	"guid" : "<GUID for the new user>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user within a specific timespan. |

### Create a new user account

Once a GUID has been received it can be paired with a client-created public key and sent back to the server to finish account creation.

Note that when authenticating users, the Kakapo server attempts to look up the user's public key using the user id "guid <guid>". For example, if my GUID were f8d2a8c5-ffec-426a-a587-301b86af769e, the user id used to look up the key in the public key rings data is "f8d2a8c5-ffec-426a-a587-301b86af769e <f8d2a8c5-ffec-426a-a587-301b86af769e>".

**PUT /api/v1/account/create**

Request Body:

```json
{
	"guid" : "<the user's GUID>",
	"pk" : "<MIME encoded GPG-compatible public key rings byte array>"
}
```

Response Body:

```text
No response body
```


| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed or the included public key could not be read |
| 401 | The user for the GUID specified in the request could not be found. |
| 413 | The provided public key rings data was too large. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |
| 443 | Insufficient key length; the submitted public key was generated with a key length that was too small. |

### Authenticate

Performs an authentication check.

**PUT /api/v1/account/authenticate**

Request Body:

```json
{
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```text
No response body
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch a public key

Fetches a public key from the server for a particular user's GUID.

**PUT /api/v1/account/publicKey**

Request Body:

```json
{
	"tg" : "<the target user's GUID>",
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```json
{
	"pkr" : "<MIME encoded GPG-compatible public key rings byte array>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 404 | Target user with specified GUID not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Delete account

Deletes the account from the server and nulls out all of the account's shared items.

**PUT /api/v1/account/delete**

Request Body:

```json
{
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```text
No response body
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Fetch quota

Fetches the maximum quota and currently-used amount (in megabytes).

**PUT /api/v1/account/quota**

Request Body:

```json
{
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```
{
	"uq" : <used quota in megabytes>,
	"mq" : <max quota in megabytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Upload account data

Uploads (encrypted) account data to the server so that it can be downloaded on another device. Note that the server does not check to see that the account data is encrypted, but stores it as-is.

**PUT /api/v1/account/upload**

Request Body:

```json
{
	"ead" : "<MIME-encoded byte array of encrypted account data>",
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```json
{
	"guid" : "<newly-assigned GUID of the uploaded account data>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 413 | Payload too large; the maximum size of the uploaded account has been exceeded.
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Download account data

Downloads (encrypted) account data so that it can be added to another device. When account data is uploaded, the response includes a GUID. This is _not_ the GUID of a user, but a GUID that points to the account data so that it can be downloaded, decrypted, and used.

**PUT /api/v1/account/download**

Request Body:

```json
{
	"guid" : "<the GUID of the uploaded account data>"
}
```

Response Body:

```json
{
	"ead" : "<MIME-encoded byte array of encrypted account data>"
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 404 | Target backup with specified GUID not found. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### Blacklist a user

Adds the target user to the blacklist. Items owned by blacklisted users are not included when headers are fetched. Mostly. It's slightly more nuanced than this. Please see the "list item headers" endpoint below for more details.

**PUT /api/v1/account/blacklist**

Request Body:

```json
{
	"ead" : "<MIME-encoded byte array of encrypted account data>",
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```text
No response body
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

## Server-related endpoints

### Fetch server configuration

The server has some configuration parameters that the clients may want to know about. You can fetch them with this endpoint.

**PUT /api/v1/server/config**

Request Body:

```json
{
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>",
	"tg" : "<the GUID of the user to blacklist>"
}
```

Response Body:

```
{
	"mpkl" : <maximum public key length in bytes>
	"qpu" : <maximum quota per user in megabytes>
	"hsl" : <maximum encrypted item header size in bytes>
	"csl" : <maximum encrypted item content size in bytes>
	"uasl" : <maximum uploaded account size in bytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

## Item-related endpoints

### Upload a new item

Upload a new item with an encrypted header and content payload.

**PUT /api/v1/item/submit**

Request Body:

```
{
	"pir" : <the parent item ID if this is a child item (optional)>,
	"swg" : [
		<GUID 1 of user that this item is to be shared with>
		<GUID 2>
		...
		<GUID n>
	],
	"eh" : "<MIME encoded byte array of the encrypted header>",
	"ec" : "<MIME encoded byte array of the encrypted content>",
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```
{
	"ir" : <the newly assigned numeric ID of the uploaded item>,
	"uq" : <used quota in megabytes>,
	"mq" : <max quota in megabytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 404 | The specified parent item id was not found, or it is not visible to the user. |
| 413 | Payload too large; the maximum size of the uploaded account has been reached. |
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |
| 442 | Quota exceeded; the amount of disk space allocated to the user will be exceeded if this item is persisted. |

### Delete item

Delete a specific item from the server. The item is marked as deleted and the header and content are nulled out.

**PUT /api/v1/item/delete**

Request Body:

```
{
	"ir" : <the ID of the item to fetch>,
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```
{
	"ir" : <the ID of the deleted item>,
	"uq" : <used quota in megabytes>,
	"mq" : <max quota in megabytes>
}
```

| HTTP | Explanation |
| :---: | --- |
| 200 | Ok |
| 400 | The request is malformed. |
| 401 | Authentication failed; may indicate the the user does not exist or that the digital signature failed verification. |
| 404 | Not found; the specified item was either not found or the user does not own the item.
| 429 | Too many requests; there have been too many requests of this type from the IP address and/or user. |

### List item headers

Fetch a list of item headers that are visible to a specific user. The items are fetched in reverse chronological order, starting with the first item with an ID less than the "lkir" value in the request body.

The "pir" and "ir" values are optional. If the "pir" value is specified, only items with the specified parent item ID are fetched. If the "ir" value is specified only that single item is fetched. If both are specified, the "pir" value is ignored. 

If the "pir" value is not specified, only "parentless" items are fetched.

A note regarding blacklisted users: When the "pir" (parent item id) value is provided, items from blacklisted users will be included in the response. If the "pir" value is not provided, items from blacklisted users will not be included in the response. The reason for this is that when fetching the children of a specified parent, the item from the blacklisted author may be in the middle of a response tree and it is important for the client to have the complete tree of responses for presentation purposes.

**PUT /api/v1/item/headers**

Request Body:

```
{
	"ic" : <the number of item headers to fetch, within the range 1-20>,
	"lkir" : <the item ID to start fetching headers after>
	"pir" : <if specified, only fetch items that are children of this item ID (optional)>,
	"ir" : <a specific item ID to fetch (optional)>,
	"guid" : "<the user's GUID>",
	"sig" : "<MIME encoded byte array of the digital signature>"
}
```

Response Body:

```
{
	"si" : [
		{
			"r" : <the item ID>
			"og" : "<the GUID of the owner/author of the item>"
			"pir" : <the parent ID of this item (may be null)>
			"mad" : true|false (true if the item has been marked as deleted, false if not),
			"bl" : true|false (true if the item is owned by a blacklisted author, false if not),
			"it" : <item timestamp in GMT (in ms from the epoch (midnight, January 1, 1970))>
			"eh" : "<MIME encoded byte array of the encrypted header>",
			"ec" : null
		},
		...
	],
	"ric" : <the number of remaining item headers that may be fetched>
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

**PUT /api/v1/item/content**

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

**PUT /api/v1/item/recipients**

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

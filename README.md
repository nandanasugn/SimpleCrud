# SIMPLE CRUD API DOCUMENTATION
## Version: 1.0-SNAPSHOT

### /api/v1/auth/register/customer

#### POST
##### Summary:

Register a new customer

##### Description:

This endpoint registers a new customer in the system. The customer's details should be provided in the request.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/register/admin

#### POST
##### Summary:

Register a new admin

##### Description:

This endpoint allows an ADMIN to register another admin. The admin details should be provided in the request.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| registerRequestDto | query |  | Yes | [RegisterRequestDto](#RegisterRequestDto) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/auth/login

#### POST
##### Summary:

Login a user

##### Description:

This endpoint authenticates a user and returns a token for subsequent requests.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/change-password

#### POST
##### Summary:

Change user password

##### Description:

This endpoint allows a user (ADMIN or CUSTOMER) to change their password. The old and new passwords should be provided in the request.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/auth/customer

#### PATCH
##### Summary:

Update customer information

##### Description:

This endpoint allows a CUSTOMER or ADMIN to update customer information by providing the user's email and updated details.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user_email | query | Email of the customer to update | Yes | string |
| userUpdateRequestDto | query |  | Yes | [UserUpdateRequestDto](#UserUpdateRequestDto) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/auth/admin

#### PATCH
##### Summary:

Update admin information

##### Description:

This endpoint allows an ADMIN to update admin information by providing the user's email and updated details.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| user_email | query | Email of the admin to update | Yes | string |
| userUpdateRequestDto | query |  | Yes | [UserUpdateRequestDto](#UserUpdateRequestDto) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/user/customer

#### GET
##### Summary:

Retrieve all customers

##### Description:

Fetch a paginated list of all customers. Filters by name if provided.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| name | query | Optional name filter for customers | No | string |
| page | query | Page number for pagination | No | integer |
| size | query | Number of records per page | No | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

#### DELETE
##### Summary:

Delete a customer

##### Description:

Delete a customer's record by their email address. Requires ADMIN or CUSTOMER role.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| email | query | Customer's email address | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/user/customer-detail

#### GET
##### Summary:

Get customer details by email

##### Description:

Retrieve detailed information about a customer by their email address.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| email | query | Customer's email address | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/user/admin

#### GET
##### Summary:

Retrieve all admins

##### Description:

Fetch a paginated list of all administrators. Filters by name if provided.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| name | query | Optional name filter for admins | No | string |
| page | query | Page number for pagination | No | integer |
| size | query | Number of records per page | No | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

#### DELETE
##### Summary:

Delete an admin

##### Description:

Delete an administrator's record by their email address. Requires ADMIN role.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| email | query | Admin's email address | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

### /api/v1/files/{subfolder}/{fileName}

#### GET
##### Summary:

Fetch file

##### Description:

Retrieves a file from the specified subfolder by file name. Accessible to users with ADMIN or CUSTOMER roles.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| subfolder | path | Subfolder name where the file is stored | Yes | string |
| fileName | path | Name of the file to fetch | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

# Customer Documentation

This documentation describes the customer registration and update processes for the Loan Tracking system.

## Customer Registration

New customers can be registered using the following endpoint.

### Register Customer
- **URL**: `POST /loan/customers/register` (or `POST /loan/custormers/register`)
- **Method**: `POST`
- **Request Body**: `CustomerDTO` (JSON)
- **Response**: `CustomerDTO` (JSON)

**Example Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+255700000000",
  "address": "Dar es Salaam, Tanzania",
  "nationalId": "123456789-00001",
  "status": "PROSPECT"
}
```

**Validation Rules**:
- `firstName`: Required (cannot be blank)
- `lastName`: Required (cannot be blank)
- `email`: Required, must be a valid email format, and must be unique.
- `nationalId`: Required, must be unique.

---

## Customer Updates

Currently, the system supports updating a customer's profile image and status management.

### Update Customer Image
Upload or update a customer's profile picture.

- **URL**: `POST /loan/customers/{id}/image`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Parameters**:
    - `id`: The UUID of the customer.
    - `image`: The image file to upload.

### Delete Customer Image
Remove a customer's profile picture.

- **URL**: `DELETE /loan/customers/{id}/image`
- **Method**: `DELETE`
- **Parameters**:
    - `id`: The UUID of the customer.

### Get Customer Image
Retrieve a customer's profile picture.

- **URL**: `GET /loan/customers/{id}/image`
- **Method**: `GET`
- **Parameters**:
    - `id`: The UUID of the customer.
- **Response**: `byte[]` (image/jpeg)

---

## Customer Retrieval & Search

### Get All Customers (Paginated)
- **URL**: `GET /loan/customers`
- **Method**: `GET`
- **Query Parameters**:
    - `page`: Page number (default: 0)
    - `size`: Page size (default: 10)
    - `sort`: Sorting criteria (e.g., `firstName,asc`)

### Get Customer by ID
- **URL**: `GET /loan/customers/{id}`
- **Method**: `GET`
- **Parameters**:
    - `id`: The UUID of the customer.

### Search Customers by Email
- **URL**: `POST /loan/customers/search`
- **Method**: `POST`
- **Query Parameters**:
    - `email`: Substring of the email to search for.
    - `page`, `size`: Pagination parameters.

---

## Data Model Reference

### Customer Statuses (`CustomerStatus` Enum)
- `PROSPECT`: Initial status for a potential customer.
- `CUSTOMER`: Active customer status.

### Customer Data Fields
| Field | Type | Description |
| :--- | :--- | :--- |
| `customerId` | String (UUID) | Unique identifier for the customer (Server-generated). |
| `firstName` | String | Customer's first name. |
| `lastName` | String | Customer's last name. |
| `email` | String | Unique email address. |
| `phoneNumber` | String | Contact phone number. |
| `address` | String | Physical address. |
| `nationalId` | String | Unique National Identification Number. |
| `status` | String | `PROSPECT` or `CUSTOMER`. |
| `createdBy` | String | User who created the record (Audit). |
| `createdDate` | DateTime | Timestamp of creation (Audit). |
| `updatedBy` | String | User who last updated the record (Audit). |
| `updatedDate` | DateTime | Timestamp of the last update (Audit). |

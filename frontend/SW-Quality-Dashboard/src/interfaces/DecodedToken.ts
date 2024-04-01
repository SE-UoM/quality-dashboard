interface DecodedToken {
    sub: string; // Subject: email address
    roles: string[]; // Array of roles
    iss: string; // Issuer
    name: string; // Name
    id: string; // ID
    exp: number; // Expiration timestamp (UNIX timestamp)
}

export default DecodedToken;
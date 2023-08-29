import { UserRoles } from '../assets/models';
import jwt from 'jsonwebtoken';
interface DecodedToken {
    sub: string;
    roles: UserRoles[];
    name: string;
    id: string;
}
//should probably be passed as an env variable
let secret = "secret";

export const decodeJWT: (token: string) => DecodedToken | null = (token: string): DecodedToken | null => {
    try {
        const decoded = jwt.verify(token, secret) as DecodedToken;
        return decoded;
    } catch (error) {
        console.error('Error decoding JWT:', error);
        return null;
    }
};




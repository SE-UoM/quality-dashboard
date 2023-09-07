import { UserRoles } from "../assets/models";
import jwt_decode from "jwt-decode";
interface DecodedToken {
  sub: string;
  roles: UserRoles[];
  name: string;
  id: string;
}

const mockToken = {
  sub: "",
};

//should probably be passed as an env variable
let secret = "secret";

export const decodeJWT: (token: string) => DecodedToken | null = (
  token: string
): DecodedToken | null => {
  try {
    const decoded = jwt_decode(token) as DecodedToken;
    return decoded;
  } catch (error) {
    console.error("Error decoding JWT:", error);
    return null;
  }
};

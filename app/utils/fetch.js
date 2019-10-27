import axios from "axios";
import {BACKEND_URL} from "./appConstants";

export default async (method, path, data) => {
    const transport = axios.create({
        withCredentials: true
    });

    return transport({
        method,
        url: `${BACKEND_URL}${path}`,
        data
    });
};
import axios from "axios";
import {BACKEND_URL} from "./appConstants";

export default async (method, path, data) => {
    return axios({
        method,
        url: `${BACKEND_URL}${path}`,
        data
    });
};
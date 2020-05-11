import * as types from "./types";


export const progressStarted = () => ({
    type: types.STARTED_PROGRESS
});


export const progressFinished = () => ({
    type: types.FINISHED_PROGRESS
});
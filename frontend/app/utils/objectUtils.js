export function isObjectModified(newObject, oldObject) {
    return JSON.stringify(newObject) !== JSON.stringify(oldObject);
}

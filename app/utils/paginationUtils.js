export function getMaxValidPageNumber(page, allItemsCount, itemsPerPage) {
    if (page < 1) {
        return false;
    }

    let maxPageNumber;

    if (allItemsCount <= itemsPerPage) {
        maxPageNumber = 1;
    } else {
        maxPageNumber = Math.ceil(allItemsCount / itemsPerPage);
    }

    return maxPageNumber;
}

export function sliceItemsByPage(items, page, itemsPerPage) {
    const firstElementIndex = (page - 1) * itemsPerPage;
    const afterLastElementIndex = itemsPerPage * page;

    return items.slice(firstElementIndex, afterLastElementIndex);
}
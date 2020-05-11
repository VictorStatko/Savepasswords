export function getMaxValidPageNumber(allItemsCount, itemsPerPage) {
    let maxPageNumber;

    if (allItemsCount <= itemsPerPage) {
        maxPageNumber = 1;
    } else {
        maxPageNumber = Math.ceil(allItemsCount / itemsPerPage);
    }

    return maxPageNumber;
}

export function recreatePagination(items, pagination) {
    const maxValidPageNumber = getMaxValidPageNumber(items.length, pagination.size);

    let newPagination = {...pagination};

    if (pagination.page > maxValidPageNumber) {
        newPagination.page = maxValidPageNumber;
    }

    newPagination.total = items.length;

    return newPagination;
}

export function slicePage(items, pagination) {
    return sliceItemsByPage(items, pagination.page, pagination.size);
}

function sliceItemsByPage(items, page, itemsPerPage) {
    const firstElementIndex = (page - 1) * itemsPerPage;
    const afterLastElementIndex = itemsPerPage * page;

    return items.slice(firstElementIndex, afterLastElementIndex);
}
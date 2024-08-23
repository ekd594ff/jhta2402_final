export const getPageList = (page, size, totalPages) => {
    const start = Math.max(1, page - ((page - 1) % size));

    const end = Math.min(page - 1 - ((page - 1) % size) + size, totalPages)

    return Array.from(
        {length: end - start + 1},
        (_, index) => start + index);
}
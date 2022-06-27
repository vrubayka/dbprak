SELECT
    (SELECT COUNT(book_id) from book) as book_num,
    (SELECT COUNT(dvd_id) from dvd) as dvd_num,
    (SELECT COUNT(cd_id) from cd) as cd_num;
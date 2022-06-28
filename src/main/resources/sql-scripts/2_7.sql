SELECT username FROM review
GROUP BY username
HAVING COUNT(username) > 10;